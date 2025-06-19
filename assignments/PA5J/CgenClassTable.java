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

// This is a project skeleton file

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

/**
 * This class is used for representing the inheritance tree during code generation. You will need to
 * fill in some of its methods and potentially extend it in other useful ways.
 */
class CgenClassTable extends SymbolTable {
  /** All classes in the program, represented as CgenNode */
  private final Vector<CgenNode> nds;

  // Class tags per class name stored in the prototype objects and used to refer to classes.
  private final Map<String, Integer> classTags;
  // Object size per class name to calculate attribute offsets in the object layout.
  private final Map<String, Integer> objectSizes;
  // Dispatch table per class name to get the method offsets. Using a list is obviously not the
  // fastest way but that is not the goal here.
  private final Map<String, List<String>> dispatchTable;

  // Represents the environment mapping identifiers to store locations as described in the
  // operational semantics of the Cool manual.
  // private final SymbolTable environment;

  /** This is the stream to which assembly instructions are output */
  private final PrintStream s;

  private final int objectclasstag = 0;
  private final int ioclasstag = 1;
  private final int intclasstag = 2;
  private final int boolclasstag = 3;
  private final int stringclasstag = 4;

  // The following methods emit code for constants and global
  // declarations.

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
    AbstractTable.inttable.addString("0");

    AbstractTable.stringtable.codeStringTable(stringclasstag, s);
    AbstractTable.inttable.codeStringTable(intclasstag, s);
    codeBools(boolclasstag);
  }

  /**
   * Creates data structures representing basic Cool classes (Object, IO, Int, Bool, String). Please
   * note: as is this method does not do anything useful; you will need to edit it to make if do
   * what you want.
   */
  private void installBasicClasses() {
    AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

    // A few special class names are installed in the lookup table
    // but not the class list.  Thus, these classes exist, but are
    // not part of the inheritance hierarchy.  No_class serves as
    // the parent of Object and the other special classes.
    // SELF_TYPE is the self class; it cannot be redefined or
    // inherited.  prim_slot is a class known to the code generator.

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
    this.classTags.put(TreeConstants.Object_.getString(), objectclasstag);

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
    this.classTags.put(TreeConstants.IO.getString(), ioclasstag);

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
    this.classTags.put(TreeConstants.Int.getString(), intclasstag);

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
    this.classTags.put(TreeConstants.Bool.getString(), boolclasstag);

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
    this.classTags.put(TreeConstants.Str.getString(), stringclasstag);
  }

  // The following creates an inheritance graph from
  // a list of classes.  The graph is implemented as
  // a tree of `CgenNode', and class names are placed
  // in the base class symbol table.

  private void installClass(CgenNode nd) {
    AbstractSymbol name = nd.getName();
    if (probe(name) != null) return;
    nds.addElement(nd);
    addId(name, nd);
    this.classTags.put(name.getString(), this.classTags.size());
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
    this.dispatchTable = new HashMap<>(basicClassNumber + cls.getLength());

    this.s = str;

    enterScope();
    if (Flags.cgen_debug) System.out.println("Building CgenClassTable");

    installBasicClasses();
    installClasses(cls);
    buildInheritanceTree();

    code();

    exitScope();
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
    s.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      StringSymbol className =
          (StringSymbol)
              AbstractTable.stringtable.lookup(((CgenNode) e.nextElement()).getName().getString());
      s.print(CgenSupport.WORD);
      className.codeRef(s);
      s.println();
    }
  }

  private void codeClassObjectTable() {
    s.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      AbstractSymbol className = ((CgenNode) e.nextElement()).getName();
      s.print(CgenSupport.WORD);
      CgenSupport.emitProtObjRef(className, s);
      s.println();
      s.print(CgenSupport.WORD);
      CgenSupport.emitInitRef(className, s);
      s.println();
    }
  }

  /*
   * Emits the dispatch table as described in the Cool runtime manual. Method declarations in a
   * child class take precedence over the parent. This fact is used by the reference code
   * generator to only put the child methods into the dispatch table on override. I am not making
   * this optimization here so dispatch tables and offsets into the dispatch tables will differ
   * with the reference implementation. This is certainly doable though :)
   */
  private void codeDispatchTable() {
    Stack<class_c> hierarchy = new Stack<>();
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      CgenNode cls = (CgenNode) e.nextElement();

      AbstractSymbol name = cls.getName();
      CgenSupport.emitDispTableRef(name, s);
      s.print(CgenSupport.LABEL);

      dispatchTable.put(name.toString(), new ArrayList<String>());

      hierarchy.push(cls);
      while (hasParent(cls)) {
        hierarchy.push(cls.getParentNd());
        cls = cls.getParentNd();
      }

      while (!hierarchy.empty()) {
        class_c cur = hierarchy.pop();
        for (Enumeration f = cur.features.getElements(); f.hasMoreElements(); ) {
          Feature feature = ((Feature) f.nextElement());
          if (feature instanceof method m) {
            s.print(CgenSupport.WORD);
            CgenSupport.emitMethodRef(cur.getName(), m.name, s);
            s.println();

            // method names are added in the dispatch table order without class name
            // prefix so they can be found by a dispatch which only has access to its
            // name
            dispatchTable.get(name.toString()).add(m.name.toString());
          }
        }
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
      s.print(classTags.get(className.getString()));
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
        ((IntSymbol) AbstractTable.inttable.lookup(0)).codeRef(proto);
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
                ((IntSymbol) AbstractTable.inttable.lookup(0)).codeRef(proto);
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

    // TODO(ivo) all attributes are visible to the initializers via the environment. If they are
    // accessed before their initializer is run they are visible using their defaults. Meaning
    // first all are visible using defaults, then initializers are run in greates ancester order
    // I think. Am I doing this correctly? Come up with a code example to show it works as
    // expected. The environment here is just a dummy one as I have to pass one but am unsure if
    // the one created for methods should be created first and reused here or how reuse would
    // even look like.
    SymbolTable environment = new SymbolTable();

    // emit code to evaluate attribute initializers, attributes without one get their defaults
    // via the proto objects
    for (Enumeration f = cls.features.getElements(); f.hasMoreElements(); ) {
      Feature feature = ((Feature) f.nextElement());
      if (feature instanceof attr a) {
        if (a.init != null && !(a.init instanceof no_expr)) {
          a.init.code(cls, environment, dispatchTable, s);
          // store initialization value into corresponding attribute
          CgenSupport.emitStore(CgenSupport.ACC, attrNumber, CgenSupport.SELF, s);
        }
        attrNumber++;
      }
    }

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

  record Location(int offset, String sourceRegister) {}

  private void codeMethods() {
    for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
      CgenNode cls = (CgenNode) e.nextElement();
      // basic class methods apart from the init() are provided by the SPIM runtime
      if (cls.basic()) {
        continue;
      }

      // TODO(ivo) cleanup mess once its working
      // add all attributes and their locations to the environment
      SymbolTable environment = new SymbolTable();
      environment.enterScope();
      // the class attributes either start after the object header or after the parents object
      // size. See object layout in the Cool runtime doc Figure 2: Example object layout for Child.
      int attrNumber = CgenSupport.DEFAULT_OBJFIELDS;
      if (hasParent(cls)) {
        AbstractSymbol name = cls.getParentNd().getName();
        attrNumber = objectSizes.get(name.toString());
      }
      for (Enumeration f = cls.features.getElements(); f.hasMoreElements(); ) {
        Feature feature = ((Feature) f.nextElement());
        if (feature instanceof attr attr) {
          environment.addId(attr.name, new Location(attrNumber, CgenSupport.SELF));
        }
        attrNumber++;
      }

      for (Enumeration f = cls.features.getElements(); f.hasMoreElements(); ) {
        Feature feature = ((Feature) f.nextElement());
        if (feature instanceof method m) {
          environment.enterScope();
          codeMethod(environment, cls, m);
          environment.exitScope();
        }
      }

      environment.exitScope();
    }
  }

  private void codeMethod(SymbolTable environment, CgenNode cls, method m) {
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

    // TODO(ivo) who is responsible for the missing bit?
    // move	$s0 $a0
    // >>> missing
    // lw	$a0 12($fp)
    // <<<
    // sw	$a0 0($sp)
    // addiu	$sp $sp -4
    // move	$a0 $s0
    // TODO(ivo) for all the formals
    m.expr.code(cls, environment, dispatchTable, s);

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
}
