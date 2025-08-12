/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Vector;

/**
 * This class is used for representing the inheritance tree during code generation. You will need to
 * fill in some of its methods and potentially extend it in other useful ways.
 */
class CgenClassTable extends SymbolTable {
  /** All classes in the program, represented as CgenNode */
  private final Vector<CgenNode> nds;

  // Range of class tags used for checking the case branches against the runtime type.
  record Range(int min, int max) {}
  ;

  // Class tags ranges per class name to check the the dynamic (runtime) class tag against a case
  // branch class tag range, generate the prototype objects and used to refer to classes. The
  // range is [itself itself|max child].
  private final Map<String, Range> classTags;
  // Object size per class name to calculate attribute offsets in the object layout.
  private final Map<String, Integer> objectSizes;
  // Dispatch tables per class name to get the method offsets.
  private final Map<String, Map<String, DispatchTableEntry>> dispatchTables;

  record DispatchTableEntry(method method, String methodRef, int offset) {}
  ;

  /** This is the stream to which assembly instructions are output */
  private final PrintStream s;

  private int objectclasstag;
  private int ioclasstag;
  private int intclasstag;
  private int boolclasstag;
  private int stringclasstag;

  // The following methods emit code for constants and global declarations.

  /** Emits code to start the .data segment and to declare the global names. */
  private void codeGlobalData() {
    // The following global names must be defined first.
    s.print("\t.data\n" + CgenSupport.ALIGN);
    s.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitProtObjRef(TreeConstants.Main, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitProtObjRef(TreeConstants.Int, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitProtObjRef(TreeConstants.Str, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    BoolConst.falsebool.codeRef(s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    BoolConst.truebool.codeRef(s);
    s.println("");
    s.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
    s.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
    s.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

    // We also need to know the tag of the Int, String, and Bool classes
    // during code generation.
    s.println(CgenSupport.INTTAG + CgenSupport.LABEL + CgenSupport.WORD + intclasstag);
    s.println(CgenSupport.BOOLTAG + CgenSupport.LABEL + CgenSupport.WORD + boolclasstag);
    s.println(CgenSupport.STRINGTAG + CgenSupport.LABEL + CgenSupport.WORD + stringclasstag);
  }

  /** Emits code to start the .text segment and to declare the global names. */
  private void codeGlobalText() {
    s.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
    s.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
    s.println(CgenSupport.WORD + 0);
    s.println("\t.text");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitInitRef(TreeConstants.Main, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitInitRef(TreeConstants.Int, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitInitRef(TreeConstants.Str, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitInitRef(TreeConstants.Bool, s);
    s.println("");
    s.print(CgenSupport.GLOBAL);
    CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, s);
    s.println("");
  }

  /** Emits code definitions for boolean constants. */
  private void codeBools(int classtag) {
    BoolConst.falsebool.codeDef(classtag, s);
    BoolConst.truebool.codeDef(classtag, s);
  }

  /** Generates GC choice constants (pointers to GC functions) */
  private void codeSelectGc() {
    s.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
    s.println("_MemMgr_INITIALIZER:");
    s.println(CgenSupport.WORD + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

    s.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
    s.println("_MemMgr_COLLECTOR:");
    s.println(CgenSupport.WORD + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

    s.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
    s.println("_MemMgr_TEST:");
    s.println(CgenSupport.WORD + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
  }

  /**
   * Emits code to reserve space for and initialize all of the constants. Class names should have
   * been added to the string table (in the supplied code, is is done during the construction of the
   * inheritance graph), and code for emitting string constants as a side effect adds the string's
   * length to the integer table. The constants are emmitted by running through the stringtable and
   * inttable and producing code for each entry.
   */
  private void codeConstants() {
    // Add constants that are required by the code generator.
    AbstractTable.stringtable.addString("");
    AbstractTable.inttable.addInt(0);

    AbstractTable.stringtable.codeStringTable(stringclasstag, s);
    AbstractTable.inttable.codeStringTable(intclasstag, s);
    codeBools(boolclasstag);
  }

  /** Creates data structures representing basic Cool classes (Object, IO, Int, Bool, String). */
  private void installBasicClasses() {
    AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

    // A few special class names are installed in the lookup table but not the class list. Thus,
    // these classes exist, but are not part of the inheritance hierarchy. No_class serves as the
    // parent of Object and the other special classes. SELF_TYPE is the self class; it cannot be
    // redefined or inherited. prim_slot is a class known to the code generator.
    addId(
        TreeConstants.No_class,
        new CgenNode(
            new class_c(
                0, TreeConstants.No_class, TreeConstants.No_class, new Features(0), filename),
            CgenNode.Basic,
            this));

    addId(
        TreeConstants.SELF_TYPE,
        new CgenNode(
            new class_c(
                0, TreeConstants.SELF_TYPE, TreeConstants.No_class, new Features(0), filename),
            CgenNode.Basic,
            this));

    addId(
        TreeConstants.prim_slot,
        new CgenNode(
            new class_c(
                0, TreeConstants.prim_slot, TreeConstants.No_class, new Features(0), filename),
            CgenNode.Basic,
            this));

    // The Object class has no parent class. Its methods are
    //        cool_abort() : Object    aborts the program
    //        type_name() : Str        returns a string representation
    //                                 of class name
    //        copy() : SELF_TYPE       returns a copy of the object

    class_c Object_class =
        new class_c(
            0,
            TreeConstants.Object_,
            TreeConstants.No_class,
            new Features(0)
                .appendElement(
                    new method(
                        0,
                        TreeConstants.cool_abort,
                        new Formals(0),
                        TreeConstants.Object_,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.type_name,
                        new Formals(0),
                        TreeConstants.Str,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.copy,
                        new Formals(0),
                        TreeConstants.SELF_TYPE,
                        new no_expr(0))),
            filename);

    installClass(new CgenNode(Object_class, CgenNode.Basic, this));

    // The IO class inherits from Object. Its methods are
    //        out_string(Str) : SELF_TYPE  writes a string to the output
    //        out_int(Int) : SELF_TYPE      "    an int    "  "     "
    //        in_string() : Str            reads a string from the input
    //        in_int() : Int                "   an int     "  "     "

    class_c IO_class =
        new class_c(
            0,
            TreeConstants.IO,
            TreeConstants.Object_,
            new Features(0)
                .appendElement(
                    new method(
                        0,
                        TreeConstants.out_string,
                        new Formals(0)
                            .appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Str)),
                        TreeConstants.SELF_TYPE,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.out_int,
                        new Formals(0)
                            .appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Int)),
                        TreeConstants.SELF_TYPE,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.in_string,
                        new Formals(0),
                        TreeConstants.Str,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.in_int,
                        new Formals(0),
                        TreeConstants.Int,
                        new no_expr(0))),
            filename);

    installClass(new CgenNode(IO_class, CgenNode.Basic, this));

    // The Int class has no methods and only a single attribute, the
    // "val" for the integer.

    class_c Int_class =
        new class_c(
            0,
            TreeConstants.Int,
            TreeConstants.Object_,
            new Features(0)
                .appendElement(
                    new attr(0, TreeConstants.val, TreeConstants.prim_slot, new no_expr(0))),
            filename);

    installClass(new CgenNode(Int_class, CgenNode.Basic, this));

    // Bool also has only the "val" slot.
    class_c Bool_class =
        new class_c(
            0,
            TreeConstants.Bool,
            TreeConstants.Object_,
            new Features(0)
                .appendElement(
                    new attr(0, TreeConstants.val, TreeConstants.prim_slot, new no_expr(0))),
            filename);

    installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

    // The class Str has a number of slots and operations:
    //       val                              the length of the string
    //       str_field                        the string itself
    //       length() : Int                   returns length of the string
    //       concat(arg: Str) : Str           performs string concatenation
    //       substr(arg: Int, arg2: Int): Str substring selection

    class_c Str_class =
        new class_c(
            0,
            TreeConstants.Str,
            TreeConstants.Object_,
            new Features(0)
                .appendElement(new attr(0, TreeConstants.val, TreeConstants.Int, new no_expr(0)))
                .appendElement(
                    new attr(0, TreeConstants.str_field, TreeConstants.prim_slot, new no_expr(0)))
                .appendElement(
                    new method(
                        0, TreeConstants.length, new Formals(0), TreeConstants.Int, new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.concat,
                        new Formals(0)
                            .appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Str)),
                        TreeConstants.Str,
                        new no_expr(0)))
                .appendElement(
                    new method(
                        0,
                        TreeConstants.substr,
                        new Formals(0)
                            .appendElement(new formalc(0, TreeConstants.arg, TreeConstants.Int))
                            .appendElement(new formalc(0, TreeConstants.arg2, TreeConstants.Int)),
                        TreeConstants.Str,
                        new no_expr(0))),
            filename);

    installClass(new CgenNode(Str_class, CgenNode.Basic, this));
  }

  // The following creates an inheritance graph from a list of classes. The graph is implemented as
  // a tree of `CgenNode', and class names are placed in the base class symbol table.

  private void installClass(CgenNode nd) {
    AbstractSymbol name = nd.getName();
    if (probe(name) != null) return;
    nds.addElement(nd);
    addId(name, nd);
  }

  private void installClasses(Classes cs) {
    for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
      installClass(new CgenNode((class_c) e.nextElement(), CgenNode.NotBasic, this));
    }
  }

  private void buildInheritanceTree() {
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      setRelations((CgenNode) e.nextElement());
    }
  }

  private void setRelations(CgenNode nd) {
    CgenNode parent = (CgenNode) probe(nd.getParent());
    nd.setParentNd(parent);
    parent.addChild(nd);
  }

  /** Constructs a new class table and invokes the code generator */
  public CgenClassTable(Classes cls, PrintStream str) {
    this.nds = new Vector<CgenNode>();
    int basicClassNumber = 5;
    this.classTags = new HashMap<>(basicClassNumber + cls.getLength());
    this.objectSizes = new HashMap<>(basicClassNumber + cls.getLength());
    this.dispatchTables = new HashMap<>(basicClassNumber + cls.getLength());
    this.s = str;

    enterScope();
    if (Flags.cgen_debug) System.out.println("Building CgenClassTable");

    installBasicClasses();
    installClasses(cls);
    buildInheritanceTree();

    generateClassTags(root(), 0);

    code();

    exitScope();
  }

  /**
   * Generate class tags and class tag ranges using dfs assigning class tags as if the tree would be
   * a search tree. Each class will have a class tag range of {@code [itself itself|max child]}
   * (inclusive). The class tag range is then used in case branches to test against the dynamic
   * class tag of the case expression.
   *
   * <p>The class tags for the base classes differ from the ones in the originial template code. I
   * first tried to make it work but the algorithm is way easier if class tags are arranged like in
   * a "binary" search tree with contiguous tag numbers.
   */
  private int generateClassTags(CgenNode node, int tag) {
    if (node == null) {
      return tag;
    }

    int max = tag;
    for (Enumeration n = node.getChildren(); n.hasMoreElements(); ) {
      CgenNode child = (CgenNode) n.nextElement();

      max = generateClassTags(child, max + 1);
    }

    if (TreeConstants.Object_.equals(node.getName())) {
      objectclasstag = tag;
    } else if (TreeConstants.IO.equals(node.getName())) {
      ioclasstag = tag;
    } else if (TreeConstants.Int.equals(node.getName())) {
      intclasstag = tag;
    } else if (TreeConstants.Bool.equals(node.getName())) {
      boolclasstag = tag;
    } else if (TreeConstants.Str.equals(node.getName())) {
      stringclasstag = tag;
    }
    String name = node.getName().getString();
    classTags.put(name, new Range(tag, max));
    return max;
  }

  /** Main code generation method. */
  public void code() {
    if (Flags.cgen_debug) System.out.println("coding global data");
    codeGlobalData();

    if (Flags.cgen_debug) System.out.println("choosing gc");
    codeSelectGc();

    if (Flags.cgen_debug) System.out.println("coding constants");
    codeConstants();

    codeClassNameTable();
    codeClassObjectTable();
    codeDispatchTable();
    codePrototypeObjects();

    if (Flags.cgen_debug) System.out.println("coding global text");
    codeGlobalText();

    codeInitMethods();
    codeMethods();
  }

  private void codeClassNameTable() {
    List<CgenNode> ordered = nodesInClassTagOrder();

    s.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
    for (CgenNode n : ordered) {
      StringSymbol className =
          (StringSymbol) AbstractTable.stringtable.lookup(n.getName().getString());
      s.print(CgenSupport.WORD);
      className.codeRef(s);
      s.println();
    }
  }

  private void codeClassObjectTable() {
    List<CgenNode> ordered = nodesInClassTagOrder();

    s.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
    for (CgenNode n : ordered) {
      AbstractSymbol className = n.getName();
      s.print(CgenSupport.WORD);
      CgenSupport.emitProtObjRef(className, s);
      s.println();
      s.print(CgenSupport.WORD);
      CgenSupport.emitInitRef(className, s);
      s.println();
    }
  }

  private List<CgenNode> nodesInClassTagOrder() {
    List<CgenNode> ordered = new ArrayList<>(classTags.size());
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      ordered.add((CgenNode) e.nextElement());
    }
    ordered.sort(
        (n1, n2) -> {
          CgenClassTable.Range r1 = classTags.get(n1.getName().getString());
          CgenClassTable.Range r2 = classTags.get(n2.getName().getString());
          return r1.min() - r2.min();
        });
    return ordered;
  }

  /*
   * Emits the dispatch table as described in the Cool runtime manual. Method declarations in a
   * child class take precedence over the parent. This fact is used to only put the child methods
   * into the dispatch table on override.
   */
  private void codeDispatchTable() {
    collectDispatchTables(root(), dispatchTables);
    emitDispatchTables();
  }

  private void collectDispatchTables(
      CgenNode n, Map<String, Map<String, DispatchTableEntry>> dispatchTables) {
    if (n == null) {
      return;
    }

    collectDispatchTable(n, dispatchTables);

    for (Enumeration children = n.getChildren(); children.hasMoreElements(); ) {
      collectDispatchTables((CgenNode) children.nextElement(), dispatchTables);
    }
  }

  /**
   * Using a linked hash map so the insertion order is kept for each class and its parent methods. I
   * don't think this matters as long as the offsets are generated correctly but it does make
   * comparisons with the reference implementation easier.
   */
  private void collectDispatchTable(
      class_c cls, Map<String, Map<String, DispatchTableEntry>> dispatchTables) {
    Map<String, DispatchTableEntry> dispatchTable;
    if (isRoot(cls)) {
      dispatchTable = new LinkedHashMap<>();
    } else {
      Map<String, DispatchTableEntry> parentDispTable =
          dispatchTables.get(cls.getParent().getString());
      dispatchTable = new LinkedHashMap<>(parentDispTable);
    }
    dispatchTables.put(cls.name.getString(), dispatchTable);

    for (Enumeration features = cls.features.getElements(); features.hasMoreElements(); ) {
      Feature feature = ((Feature) features.nextElement());
      if (feature instanceof method m) {
        int offset = dispatchTable.size();
        // either the method ends up at the end of the dispatch table or replaces one of its
        // parent (this is how the reference implementation does it)
        if (dispatchTable.containsKey(m.name.getString())) {
          offset = dispatchTable.get(m.name.getString()).offset;
        }
        dispatchTable.put(
            m.name.toString(),
            new DispatchTableEntry(m, CgenSupport.methodRef(cls.getName(), m.name), offset));
      }
    }
  }

  private void emitDispatchTables() {
    for (Entry<String, Map<String, DispatchTableEntry>> dispatchTable : dispatchTables.entrySet()) {

      CgenSupport.emitDispTableRef(dispatchTable.getKey(), s);
      s.print(CgenSupport.LABEL);

      for (Map.Entry<String, DispatchTableEntry> entry : dispatchTable.getValue().entrySet()) {
        s.print(CgenSupport.WORD);
        s.print(entry.getValue().methodRef);
        s.println();
      }
    }
  }

  private void codePrototypeObjects() {
    Stack<class_c> hierarchy = new Stack<>();
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      // put -1 before any prototype for GC
      s.print(CgenSupport.WORD);
      s.print(-1);
      s.println();

      CgenNode cls = (CgenNode) e.nextElement();
      AbstractSymbol className = cls.getName();
      CgenSupport.emitProtObjRef(className, s);
      s.print(CgenSupport.LABEL);

      s.print(CgenSupport.WORD);
      CgenClassTable.Range range = classTags.get(className.getString());
      s.print(range.min);
      s.println();

      s.print(CgenSupport.WORD);
      // the minimum object size is a word for class tag, size and dispatch table pointer
      // printing of attribute layout needs to be delayed as the size is printed first but
      // depends on the attributes in the hierarchy
      int objectSize = CgenSupport.DEFAULT_OBJFIELDS;
      StringBuilder proto = new StringBuilder();
      proto.append('\n');

      proto.append(CgenSupport.WORD);
      CgenSupport.emitDispTableRef(className, proto);
      proto.append('\n');

      if (TreeConstants.Bool.equals(className)) {
        objectSize++;
        proto.append(CgenSupport.WORD);
        proto.append(0); // default value of false
        proto.append('\n');
      } else if (TreeConstants.Int.equals(className)) {
        objectSize++;
        proto.append(CgenSupport.WORD);
        proto.append(0);
        proto.append('\n');
      } else if (TreeConstants.Str.equals(className)) {
        objectSize += 2;
        proto.append(CgenSupport.WORD);
        // pointer to string length (of zero)
        ((IntSymbol) AbstractTable.inttable.lookup("0")).codeRef(proto);
        proto.append('\n');
        proto.append(CgenSupport.WORD);
        proto.append(0); // empty string
        proto.append('\n');
      } else { // non-basic class + IO and Object which have no attributes
        hierarchy.push(cls);
        while (hasParent(cls)) {
          hierarchy.push(cls.getParentNd());
          cls = cls.getParentNd();
        }

        while (!hierarchy.empty()) {
          class_c cur = hierarchy.pop();
          for (Enumeration f = cur.features.getElements(); f.hasMoreElements(); ) {
            Feature feature = ((Feature) f.nextElement());
            if (feature instanceof attr a) {
              objectSize++;

              proto.append(CgenSupport.WORD);
              if (TreeConstants.Bool.equals(a.type_decl)) {
                BoolConst.falsebool.codeRef(proto);
              } else if (TreeConstants.Int.equals(a.type_decl)) {
                ((IntSymbol) AbstractTable.inttable.lookup("0")).codeRef(proto);
              } else if (TreeConstants.Str.equals(a.type_decl)) {
                ((StringSymbol) AbstractTable.stringtable.lookup("")).codeRef(proto);
              } else {
                proto.append(0); // set to void
              }
              proto.append('\n');
            }
          }
        }
      }

      s.print(objectSize);
      objectSizes.put(className.toString(), objectSize);
      s.print(proto.toString());
    }
  }

  private void codeInitMethods() {
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      codeInitMethod((CgenNode) e.nextElement());
    }
  }

  /**
   * codeInitMethod emits the class init methods which are called by the runtime when a new instance
   * is created. The generated code has similar setup/cleanup to a method definition. This init has
   * no formals. This init has nothing to do with the Cons.init() method shown in the Cool Manual
   * 3.1. That was very confusing :joy:.
   *
   * <p>The parent class init method is called before the classes attribute initializers are
   * evaluated to set their attributes.
   */
  private void codeInitMethod(CgenNode cls) {
    CgenSupport.emitInitMethodDef(cls, s);

    // store the callee saved registers on the stack
    CgenSupport.emitPush(CgenSupport.FP, s);
    CgenSupport.emitPush(CgenSupport.SELF, s);
    CgenSupport.emitPush(CgenSupport.RA, s);
    // set FP of the current activation to RA
    CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, s);

    // save self
    CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);

    // call to init() of parent is emitted first which in turn will call its parent at runtime
    // up to and including Object
    if (hasParent(cls)) {
      CgenNode parent = cls.getParentNd();
      CgenSupport.emitJal(CgenSupport.initMethodRef(parent.getName()), s);
    }

    // the class attributes either start after the object header or after the parents object
    // size. See object layout in the Cool runtime doc Figure 2: Example object layout for Child.
    int attrNumber = CgenSupport.DEFAULT_OBJFIELDS;
    if (hasParent(cls)) {
      AbstractSymbol name = cls.getParentNd().getName();
      attrNumber = objectSizes.get(name.toString());
    }

    // 1. all attributes are visible to the initializers via the environment
    // 2. initializers are run in greates ancestor + code order
    // 3. if an attribute is thus accessed before its initializer has run, its default will be seen
    SymbolTable env = new SymbolTable();
    env.enterScope();
    addAttributes(env, cls);

    // emit code to evaluate attribute initializers, attributes without one get their defaults
    // via the proto objects
    for (Enumeration f = cls.features.getElements(); f.hasMoreElements(); ) {
      Feature feature = ((Feature) f.nextElement());
      if (feature instanceof attr a) {
        if (a.init != null && !(a.init instanceof no_expr)) {
          a.init.code(cls, env, classTags, dispatchTables, s);
          // store initialization value into corresponding attribute
          CgenSupport.emitStore(CgenSupport.ACC, attrNumber, CgenSupport.SELF, s);
        }
        attrNumber++;
      }
    }

    env.exitScope();

    // restore self
    CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);

    // restore callee saved registers from the stack
    CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, s);
    // restore stack to hold invariant of stack being unchanged by method calls
    // this is 3 words for fp, ra
    CgenSupport.emitPop(3, s);
    CgenSupport.emitReturn(s);
  }

  private boolean hasParent(CgenNode cls) {
    return cls.getParentNd() != null && !cls.getParentNd().getName().equals(TreeConstants.No_class);
  }

  record Address(int offset, String sourceRegister) {}

  record Register(String name) {}

  private void codeMethods() {
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      CgenNode cls = (CgenNode) e.nextElement();
      // basic class methods apart from the init() are provided by the SPIM runtime
      if (cls.basic()) {
        continue;
      }

      // add all attributes and their locations to the environment
      SymbolTable env = new SymbolTable();
      env.enterScope();
      addAttributes(env, cls);

      for (Enumeration f = cls.features.getElements(); f.hasMoreElements(); ) {
        Feature feature = ((Feature) f.nextElement());
        if (feature instanceof method m) {
          env.enterScope();
          int argOffset = 3;
          // need to add locations in reverse as args are pushed in increasing order (1.,
          // 2., ...) by a dispatch (see Cool runtime document)
          for (int i = m.formals.getLength() - 1; i >= 0; i--) {
            formalc form = (formalc) m.formals.getNth(i);
            // the arg is put onto the stack and will be available by the calle via the framepointer
            env.addId(form.name, new Address(argOffset, CgenSupport.FP));
            argOffset++;
          }

          codeMethod(env, cls, m);

          env.exitScope();
        }
      }

      env.exitScope();
    }
  }

  // I could create an attribute offset map like the dispatchTables as I am redoing this work in at
  // least two places.
  private void addAttributes(SymbolTable env, CgenNode cls) {
    Stack<class_c> hierarchy = new Stack<>();
    hierarchy.push(cls);
    while (hasParent(cls)) {
      hierarchy.push(cls.getParentNd());
      cls = cls.getParentNd();
    }

    // the class attributes either start after the object header or after the parents object
    // size. See object layout in the Cool runtime doc Figure 2: Example object layout for Child.
    int attrNumber = CgenSupport.DEFAULT_OBJFIELDS;
    while (!hierarchy.empty()) {
      class_c cur = hierarchy.pop();
      for (Enumeration f = cur.features.getElements(); f.hasMoreElements(); ) {
        Feature feature = ((Feature) f.nextElement());
        if (feature instanceof attr attr) {
          env.addId(attr.name, new Address(attrNumber, CgenSupport.SELF));
          attrNumber++;
        }
      }
    }
  }

  private void codeMethod(SymbolTable env, CgenNode cls, method m) {
    CgenSupport.emitMethodRef(cls.getName(), m.name, s);
    s.print(CgenSupport.LABEL);

    // store the callee saved registers on the stack
    CgenSupport.emitPush(CgenSupport.FP, s);
    CgenSupport.emitPush(CgenSupport.SELF, s);
    CgenSupport.emitPush(CgenSupport.RA, s);
    // set FP of the current activation to RA
    CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, s);

    // move self stored in a0 into s0 as the result of evaluating an expression will be put into
    // a0
    CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);

    m.expr.code(cls, env, classTags, dispatchTables, s);

    // restore callee saved registers from the stack
    CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, s);
    // restore stack to hold invariant of stack being unchanged by method calls
    // this is 3 words for fp, ra, s0 pushed to by the callee + actual parameters pushed to by the
    // caller
    CgenSupport.emitPop(3 + m.formals.getLength(), s);
    CgenSupport.emitReturn(s);
  }

  /** Gets the root of the inheritance tree */
  public CgenNode root() {
    return (CgenNode) probe(TreeConstants.Object_);
  }

  private static boolean isRoot(class_c cls) {
    return cls.getName() == TreeConstants.Object_;
  }
}
