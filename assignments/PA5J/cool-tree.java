// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
  protected Program(int lineNumber) {
    super(lineNumber);
  }

  public abstract void dump_with_types(PrintStream out, int n);

  public abstract void semant();

  public abstract void cgen(PrintStream s);
}

/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
  protected Class_(int lineNumber) {
    super(lineNumber);
  }

  public abstract void dump_with_types(PrintStream out, int n);

  public abstract AbstractSymbol getName();

  public abstract AbstractSymbol getParent();

  public abstract AbstractSymbol getFilename();

  public abstract Features getFeatures();
}

/**
 * Defines list phylum Classes
 *
 * <p>See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Classes extends ListNode {
  public static final Class elementClass = Class_.class;

  /** Returns class of this lists's elements */
  @Override
  public Class getElementClass() {
    return elementClass;
  }

  protected Classes(int lineNumber, Vector elements) {
    super(lineNumber, elements);
  }

  /** Creates an empty "Classes" list */
  public Classes(int lineNumber) {
    super(lineNumber);
  }

  /** Appends "Class_" element to this list */
  public Classes appendElement(TreeNode elem) {
    addElement(elem);
    return this;
  }

  @Override
  public TreeNode copy() {
    return new Classes(lineNumber, copyElements());
  }
}

/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
  protected Feature(int lineNumber) {
    super(lineNumber);
  }

  public abstract void dump_with_types(PrintStream out, int n);
}

/**
 * Defines list phylum Features
 *
 * <p>See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Features extends ListNode {
  public static final Class elementClass = Feature.class;

  /** Returns class of this lists's elements */
  @Override
  public Class getElementClass() {
    return elementClass;
  }

  protected Features(int lineNumber, Vector elements) {
    super(lineNumber, elements);
  }

  /** Creates an empty "Features" list */
  public Features(int lineNumber) {
    super(lineNumber);
  }

  /** Appends "Feature" element to this list */
  public Features appendElement(TreeNode elem) {
    addElement(elem);
    return this;
  }

  @Override
  public TreeNode copy() {
    return new Features(lineNumber, copyElements());
  }
}

/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
  protected Formal(int lineNumber) {
    super(lineNumber);
  }

  public abstract void dump_with_types(PrintStream out, int n);
}

/**
 * Defines list phylum Formals
 *
 * <p>See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {
  public static final Class elementClass = formalc.class;

  /** Returns class of this lists's elements */
  @Override
  public Class getElementClass() {
    return elementClass;
  }

  protected Formals(int lineNumber, Vector elements) {
    super(lineNumber, elements);
  }

  /** Creates an empty "Formals" list */
  public Formals(int lineNumber) {
    super(lineNumber);
  }

  /** Appends "formalc" element to this list */
  public Formals appendElement(TreeNode elem) {
    addElement(elem);
    return this;
  }

  @Override
  public TreeNode copy() {
    return new Formals(lineNumber, copyElements());
  }
}

/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
  protected Expression(int lineNumber) {
    super(lineNumber);
  }

  private AbstractSymbol type = null;

  public AbstractSymbol get_type() {
    return type;
  }

  public Expression set_type(AbstractSymbol s) {
    type = s;
    return this;
  }

  public abstract void dump_with_types(PrintStream out, int n);

  public void dump_type(PrintStream out, int n) {
    if (type != null) {
      out.println(Utilities.pad(n) + ": " + type.getString());
    } else {
      out.println(Utilities.pad(n) + ": _no_type");
    }
  }

  public abstract void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s);
}

/**
 * Defines list phylum Expressions
 *
 * <p>See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
  public static final Class elementClass = Expression.class;

  /** Returns class of this lists's elements */
  @Override
  public Class getElementClass() {
    return elementClass;
  }

  protected Expressions(int lineNumber, Vector elements) {
    super(lineNumber, elements);
  }

  /** Creates an empty "Expressions" list */
  public Expressions(int lineNumber) {
    super(lineNumber);
  }

  /** Appends "Expression" element to this list */
  public Expressions appendElement(TreeNode elem) {
    addElement(elem);
    return this;
  }

  @Override
  public TreeNode copy() {
    return new Expressions(lineNumber, copyElements());
  }
}

/** Defines simple phylum Case */
abstract class Case extends TreeNode {
  protected Case(int lineNumber) {
    super(lineNumber);
  }

  public abstract void dump_with_types(PrintStream out, int n);
}

/**
 * Defines list phylum Cases
 *
 * <p>See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {
  public static final Class elementClass = Case.class;

  /** Returns class of this lists's elements */
  @Override
  public Class getElementClass() {
    return elementClass;
  }

  protected Cases(int lineNumber, Vector elements) {
    super(lineNumber, elements);
  }

  /** Creates an empty "Cases" list */
  public Cases(int lineNumber) {
    super(lineNumber);
  }

  /** Appends "Case" element to this list */
  public Cases appendElement(TreeNode elem) {
    addElement(elem);
    return this;
  }

  @Override
  public TreeNode copy() {
    return new Cases(lineNumber, copyElements());
  }
}

/**
 * Defines AST constructor 'program'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class program extends Program {
  public Classes classes;

  /**
   * Creates "program" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for classes
   */
  public program(int lineNumber, Classes a1) {
    super(lineNumber);
    classes = a1;
  }

  @Override
  public TreeNode copy() {
    return new program(lineNumber, (Classes) classes.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "program\n");
    classes.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_program");
    for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
      ((Class_) e.nextElement()).dump_with_types(out, n + 1);
    }
  }

  /**
   * This method is the entry point to the semantic checker. You will need to complete it in
   * programming assignment 4.
   *
   * <p>Your checker should do the following two things:
   *
   * <ol>
   *   <li>Check that the program is semantically correct
   *   <li>Decorate the abstract syntax tree with type information by setting the type field in each
   *       Expression node. (see tree.h)
   * </ol>
   *
   * <p>You are free to first do (1) and make sure you catch all semantic errors. Part (2) can be
   * done in a second stage when you want to test the complete compiler.
   */
  @Override
  public void semant() {
    /* ClassTable constructor may do some semantic analysis */
    ClassTable classTable = new ClassTable(classes);

    /* some semantic analysis code may go here */

    if (classTable.errors()) {
      System.err.println("Compilation halted due to static semantic errors.");
      System.exit(1);
    }
  }

  /**
   * This method is the entry point to the code generator. All of the work of the code generator
   * takes place within CgenClassTable constructor.
   *
   * @param s the output stream
   * @see CgenClassTable
   */
  @Override
  public void cgen(PrintStream s) {
    CgenClassTable codegen_classtable = new CgenClassTable(classes, s);
  }
}

/**
 * Defines AST constructor 'class_'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
  public AbstractSymbol name;
  public AbstractSymbol parent;
  public Features features;
  public AbstractSymbol filename;

  /**
   * Creates "class_" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for parent
   * @param a2 initial value for features
   * @param a3 initial value for filename
   */
  public class_c(
      int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
    super(lineNumber);
    name = a1;
    parent = a2;
    features = a3;
    filename = a4;
  }

  @Override
  public TreeNode copy() {
    return new class_c(
        lineNumber,
        copy_AbstractSymbol(name),
        copy_AbstractSymbol(parent),
        (Features) features.copy(),
        copy_AbstractSymbol(filename));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "class_\n");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, parent);
    features.dump(out, n + 2);
    dump_AbstractSymbol(out, n + 2, filename);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_class");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, parent);
    out.print(Utilities.pad(n + 2) + "\"");
    Utilities.printEscapedString(out, filename.getString());
    out.println("\"\n" + Utilities.pad(n + 2) + "(");
    for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
      ((Feature) e.nextElement()).dump_with_types(out, n + 2);
    }
    out.println(Utilities.pad(n + 2) + ")");
  }

  @Override
  public AbstractSymbol getName() {
    return name;
  }

  @Override
  public AbstractSymbol getParent() {
    return parent;
  }

  @Override
  public AbstractSymbol getFilename() {
    return filename;
  }

  @Override
  public Features getFeatures() {
    return features;
  }
}

/**
 * Defines AST constructor 'method'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
  public AbstractSymbol name;
  public Formals formals;
  public AbstractSymbol return_type;
  public Expression expr;

  /**
   * Creates "method" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for formals
   * @param a2 initial value for return_type
   * @param a3 initial value for expr
   */
  public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
    super(lineNumber);
    name = a1;
    formals = a2;
    return_type = a3;
    expr = a4;
  }

  @Override
  public TreeNode copy() {
    return new method(
        lineNumber,
        copy_AbstractSymbol(name),
        (Formals) formals.copy(),
        copy_AbstractSymbol(return_type),
        (Expression) expr.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "method\n");
    dump_AbstractSymbol(out, n + 2, name);
    formals.dump(out, n + 2);
    dump_AbstractSymbol(out, n + 2, return_type);
    expr.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_method");
    dump_AbstractSymbol(out, n + 2, name);
    for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
      ((formalc) e.nextElement()).dump_with_types(out, n + 2);
    }
    dump_AbstractSymbol(out, n + 2, return_type);
    expr.dump_with_types(out, n + 2);
  }
}

/**
 * Defines AST constructor 'attr'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
  public AbstractSymbol name;
  public AbstractSymbol type_decl;
  public Expression init;

  /**
   * Creates "attr" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for type_decl
   * @param a2 initial value for init
   */
  public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
    super(lineNumber);
    name = a1;
    type_decl = a2;
    init = a3;
  }

  @Override
  public TreeNode copy() {
    return new attr(
        lineNumber,
        copy_AbstractSymbol(name),
        copy_AbstractSymbol(type_decl),
        (Expression) init.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "attr\n");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
    init.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_attr");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
    init.dump_with_types(out, n + 2);
  }
}

/**
 * Defines AST constructor 'formalc'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formalc extends Formal {
  public AbstractSymbol name;
  public AbstractSymbol type_decl;

  /**
   * Creates "formalc" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for type_decl
   */
  public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
    super(lineNumber);
    name = a1;
    type_decl = a2;
  }

  @Override
  public TreeNode copy() {
    return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "formalc\n");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_formal");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
  }
}

/**
 * Defines AST constructor 'branch'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class branch extends Case {
  public AbstractSymbol name;
  public AbstractSymbol type_decl;
  public Expression expr;

  /**
   * Creates "branch" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for type_decl
   * @param a2 initial value for expr
   */
  public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
    super(lineNumber);
    name = a1;
    type_decl = a2;
    expr = a3;
  }

  @Override
  public TreeNode copy() {
    return new branch(
        lineNumber,
        copy_AbstractSymbol(name),
        copy_AbstractSymbol(type_decl),
        (Expression) expr.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "branch\n");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
    expr.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_branch");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, type_decl);
    expr.dump_with_types(out, n + 2);
  }
}

/**
 * Defines AST constructor 'assign'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
  public AbstractSymbol name;
  public Expression expr;

  /**
   * Creates "assign" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   * @param a1 initial value for expr
   */
  public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
    super(lineNumber);
    name = a1;
    expr = a2;
  }

  @Override
  public TreeNode copy() {
    return new assign(lineNumber, copy_AbstractSymbol(name), (Expression) expr.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "assign\n");
    dump_AbstractSymbol(out, n + 2, name);
    expr.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_assign");
    dump_AbstractSymbol(out, n + 2, name);
    expr.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'static_dispatch'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
  public Expression expr;
  public AbstractSymbol type_name;
  public AbstractSymbol name;
  public Expressions actual;

  /**
   * Creates "static_dispatch" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for expr
   * @param a1 initial value for type_name
   * @param a2 initial value for name
   * @param a3 initial value for actual
   */
  public static_dispatch(
      int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
    super(lineNumber);
    expr = a1;
    type_name = a2;
    name = a3;
    actual = a4;
  }

  @Override
  public TreeNode copy() {
    return new static_dispatch(
        lineNumber,
        (Expression) expr.copy(),
        copy_AbstractSymbol(type_name),
        copy_AbstractSymbol(name),
        (Expressions) actual.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "static_dispatch\n");
    expr.dump(out, n + 2);
    dump_AbstractSymbol(out, n + 2, type_name);
    dump_AbstractSymbol(out, n + 2, name);
    actual.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_static_dispatch");
    expr.dump_with_types(out, n + 2);
    dump_AbstractSymbol(out, n + 2, type_name);
    dump_AbstractSymbol(out, n + 2, name);
    out.println(Utilities.pad(n + 2) + "(");
    for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
      ((Expression) e.nextElement()).dump_with_types(out, n + 2);
    }
    out.println(Utilities.pad(n + 2) + ")");
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'dispatch'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
  public Expression expr;
  public AbstractSymbol name;
  public Expressions actual;

  /**
   * Creates "dispatch" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for expr
   * @param a1 initial value for name
   * @param a2 initial value for actual
   */
  public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
    super(lineNumber);
    expr = a1;
    name = a2;
    actual = a3;
  }

  @Override
  public TreeNode copy() {
    return new dispatch(
        lineNumber,
        (Expression) expr.copy(),
        copy_AbstractSymbol(name),
        (Expressions) actual.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "dispatch\n");
    expr.dump(out, n + 2);
    dump_AbstractSymbol(out, n + 2, name);
    actual.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_dispatch");
    expr.dump_with_types(out, n + 2);
    dump_AbstractSymbol(out, n + 2, name);
    out.println(Utilities.pad(n + 2) + "(");
    for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
      ((Expression) e.nextElement()).dump_with_types(out, n + 2);
    }
    out.println(Utilities.pad(n + 2) + ")");
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    // evaluate actual parameters and put them on the stack where the callee expects them
    for (Enumeration actuals = actual.getElements(); actuals.hasMoreElements(); ) {
      Expression actual = ((Expression) actuals.nextElement());
      actual.code(cls, env, dispatchTables, s);
      CgenSupport.emitPush(CgenSupport.ACC, s);
    }

    // evaluate e0 (if any) and set self to v0
    // TODO I am not sure I am doing this 100% correctly according to the operational semantics.
    // The body is supposed to be evaluated in a fresh environment based of the attributes which
    // should be the attributes of v0's class after having evaluated e0. Right now I think its
    // only correct for dispatches to self (when there is no e0).
    String dispatchClass;
    if (expr != null && !(expr instanceof no_expr)) {
      expr.code(cls, env, classTags, dispatchTables, s);
      // TODO load class
      dispatchClass = "";
    } else {
      // restore self
      CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
      dispatchClass = cls.getName().getString();
    }

    int label = CgenSupport.generateLocalLabel();
    // handle dispatch on void
    CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, label, s);
    CgenSupport.emitLoadString(CgenSupport.ACC, (StringSymbol) cls.getFilename(), s);
    CgenSupport.emitLoadImm(CgenSupport.T1, this.getLineNumber(), s);
    CgenSupport.emitJal(CgenSupport.DISPATCH_ABORT, s);
    // handle non-void dispatch
    CgenSupport.emitLabelDef(label, s);
<<<<<<< Updated upstream
    // load offset to the objects (self) dispatch table
    CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DISPTABLE_OFFSET, CgenSupport.ACC, s);
    // add the offset to the right method in the dispatch table
    int dispatchOffset =
        dispatchTables.get(cls.getName().getString()).get(name.getString()).offset();
    CgenSupport.emitLoad(CgenSupport.T1, dispatchOffset, CgenSupport.T1, s);
=======
    // load method into register via method label to deal with e0 and no e0 equally
    CgenSupport.emitLoadAddress(
        CgenSupport.T1, CgenSupport.methodRef(dispatchClass, name.getString()), s);
>>>>>>> Stashed changes
    CgenSupport.emitJalr(CgenSupport.T1, s);
  }
}

/**
 * Defines AST constructor 'cond'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
  public Expression pred;
  public Expression then_exp;
  public Expression else_exp;

  /**
   * Creates "cond" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for pred
   * @param a1 initial value for then_exp
   * @param a2 initial value for else_exp
   */
  public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
    super(lineNumber);
    pred = a1;
    then_exp = a2;
    else_exp = a3;
  }

  @Override
  public TreeNode copy() {
    return new cond(
        lineNumber,
        (Expression) pred.copy(),
        (Expression) then_exp.copy(),
        (Expression) else_exp.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "cond\n");
    pred.dump(out, n + 2);
    then_exp.dump(out, n + 2);
    else_exp.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_cond");
    pred.dump_with_types(out, n + 2);
    then_exp.dump_with_types(out, n + 2);
    else_exp.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    pred.code(cls, env, classTags, dispatchTables, s);
    int elseLabel = CgenSupport.generateLocalLabel();
    int fiLabel = CgenSupport.generateLocalLabel();
    CgenSupport.emitLoadBool(CgenSupport.T1, BoolConst.truebool, s);
    CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.T1, elseLabel, s);
    then_exp.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitBranch(fiLabel, s);
    CgenSupport.emitLabelDef(elseLabel, s);
    else_exp.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitLabelDef(fiLabel, s);
  }
}

/**
 * Defines AST constructor 'loop'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
  public Expression pred;
  public Expression body;

  /**
   * Creates "loop" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for pred
   * @param a1 initial value for body
   */
  public loop(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    pred = a1;
    body = a2;
  }

  @Override
  public TreeNode copy() {
    return new loop(lineNumber, (Expression) pred.copy(), (Expression) body.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "loop\n");
    pred.dump(out, n + 2);
    body.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_loop");
    pred.dump_with_types(out, n + 2);
    body.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'typcase'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
  public Expression expr;
  public Cases cases;

  /**
   * Creates "typcase" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for expr
   * @param a1 initial value for cases
   */
  public typcase(int lineNumber, Expression a1, Cases a2) {
    super(lineNumber);
    expr = a1;
    cases = a2;
  }

  @Override
  public TreeNode copy() {
    return new typcase(lineNumber, (Expression) expr.copy(), (Cases) cases.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "typcase\n");
    expr.dump(out, n + 2);
    cases.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_typcase");
    expr.dump_with_types(out, n + 2);
    for (Enumeration e = cases.getElements(); e.hasMoreElements(); ) {
      ((Case) e.nextElement()).dump_with_types(out, n + 2);
    }
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'block'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
  public Expressions body;

  /**
   * Creates "block" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for body
   */
  public block(int lineNumber, Expressions a1) {
    super(lineNumber);
    body = a1;
  }

  @Override
  public TreeNode copy() {
    return new block(lineNumber, (Expressions) body.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "block\n");
    body.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_block");
    for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
      ((Expression) e.nextElement()).dump_with_types(out, n + 2);
    }
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
      Expression exp = (Expression) e.nextElement();
      // a block results in the last expression which is handled by this setting a0 and not
      // restoring a0 to self
      exp.code(cls, env, classTags, dispatchTables, s);
    }
  }
}

/**
 * Defines AST constructor 'let'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
  public AbstractSymbol identifier;
  public AbstractSymbol type_decl;
  public Expression init;
  public Expression body;

  /**
   * Creates "let" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for identifier
   * @param a1 initial value for type_decl
   * @param a2 initial value for init
   * @param a3 initial value for body
   */
  public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
    super(lineNumber);
    identifier = a1;
    type_decl = a2;
    init = a3;
    body = a4;
  }

  @Override
  public TreeNode copy() {
    return new let(
        lineNumber,
        copy_AbstractSymbol(identifier),
        copy_AbstractSymbol(type_decl),
        (Expression) init.copy(),
        (Expression) body.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "let\n");
    dump_AbstractSymbol(out, n + 2, identifier);
    dump_AbstractSymbol(out, n + 2, type_decl);
    init.dump(out, n + 2);
    body.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_let");
    dump_AbstractSymbol(out, n + 2, identifier);
    dump_AbstractSymbol(out, n + 2, type_decl);
    init.dump_with_types(out, n + 2);
    body.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'plus'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "plus" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public plus(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new plus(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "plus\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_plus");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitPush(CgenSupport.ACC, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // copy e2 int object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    // get the value of the freshly copied e2 into t3
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitAdd(CgenSupport.T2, CgenSupport.T2, CgenSupport.T3, s);
    // update the result objects int attribute with the sum
    CgenSupport.emitStore(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitPop(1, s);
  }
}

/**
 * Defines AST constructor 'sub'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "sub" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public sub(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new sub(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "sub\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_sub");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    // TODO there might be a way to extract the binary arith operator code gen but not sure if its
    // worth it
    e1.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitPush(CgenSupport.ACC, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // TODO(ivo) do I need to do something to setup the new activation record? like store the fp
    // or so?
    // copy e2 int object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    // get the value of the freshly copied e2 into t3
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitSub(CgenSupport.T2, CgenSupport.T2, CgenSupport.T3, s);
    // update the result objects int attribute with the sum
    CgenSupport.emitStore(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitPop(1, s);
  }
}

/**
 * Defines AST constructor 'mul'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "mul" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public mul(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new mul(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "mul\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_mul");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitPush(CgenSupport.ACC, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // copy e2 int object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    // get the value of the freshly copied e2 into t3
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitMul(CgenSupport.T2, CgenSupport.T2, CgenSupport.T3, s);
    // update the result objects int attribute with the sum
    CgenSupport.emitStore(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitPop(1, s);
  }
}

/**
 * Defines AST constructor 'divide'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "divide" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public divide(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new divide(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "divide\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_divide");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitPush(CgenSupport.ACC, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // copy e2 int object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    // get the value of the freshly copied e2 into t3
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitDiv(CgenSupport.T2, CgenSupport.T2, CgenSupport.T3, s);
    // update the result objects int attribute with the sum
    CgenSupport.emitStore(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitPop(1, s);
  }
}

/**
 * Defines AST constructor 'neg'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
  public Expression e1;

  /**
   * Creates "neg" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   */
  public neg(int lineNumber, Expression a1) {
    super(lineNumber);
    e1 = a1;
  }

  @Override
  public TreeNode copy() {
    return new neg(lineNumber, (Expression) e1.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "neg\n");
    e1.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_neg");
    e1.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    // copy e1 int object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // get the value of the freshly copied e1 into t1
    CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    CgenSupport.emitNeg(CgenSupport.T1, CgenSupport.T1, s);
    // update the result objects int attribute
    CgenSupport.emitStore(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
  }
}

/**
 * Defines AST constructor 'lt'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "lt" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public lt(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new lt(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "lt\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_lt");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    // evaluate e1 then e2
    e1.code(cls, env, classTags, dispatchTables, s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // get the value of e2 into t3 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);

    // compare
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
    int endLabel = CgenSupport.generateLocalLabel();
    CgenSupport.emitBlt(CgenSupport.T2, CgenSupport.T3, endLabel, s);
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
    CgenSupport.emitLabelDef(endLabel, s);
  }
}

/**
 * Defines AST constructor 'eq'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "eq" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public eq(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new eq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "eq\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_eq");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    CgenSupport.emitMove(CgenSupport.T2, CgenSupport.ACC, s);
    // equality test whether the objects passed in $t1 and $t2 have the same primitive type
    // {Int,String,Bool} and the same value. If they do, the value in $a0 is returned, otherwise
    // $a1 is returned.
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
    CgenSupport.emitLoadBool(CgenSupport.A1, BoolConst.falsebool, s);
    CgenSupport.emitJal(CgenSupport.EQUALITY_TEST, s);
  }
}

/**
 * Defines AST constructor 'leq'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
  public Expression e1;
  public Expression e2;

  /**
   * Creates "leq" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   * @param a1 initial value for e2
   */
  public leq(int lineNumber, Expression a1, Expression a2) {
    super(lineNumber);
    e1 = a1;
    e2 = a2;
  }

  @Override
  public TreeNode copy() {
    return new leq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "leq\n");
    e1.dump(out, n + 2);
    e2.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_leq");
    e1.dump_with_types(out, n + 2);
    e2.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    // evaluate e1 then e2
    e1.code(cls, env, classTags, dispatchTables, s);
    // get the value of e1 into t2 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    e2.code(cls, env, classTags, dispatchTables, s);
    // get the value of e2 into t3 (load reference to int object, then retrieve its attribute)
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    CgenSupport.emitLoad(CgenSupport.T3, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);

    // compare
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
    int endLabel = CgenSupport.generateLocalLabel();
    CgenSupport.emitBleq(CgenSupport.T2, CgenSupport.T3, endLabel, s);
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
    CgenSupport.emitLabelDef(endLabel, s);
  }
}

/**
 * Defines AST constructor 'comp'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
  public Expression e1;

  /**
   * Creates "comp" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   */
  public comp(int lineNumber, Expression a1) {
    super(lineNumber);
    e1 = a1;
  }

  @Override
  public TreeNode copy() {
    return new comp(lineNumber, (Expression) e1.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "comp\n");
    e1.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_comp");
    e1.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    e1.code(cls, env, classTags, dispatchTables, s);
    // get the value of e1 into t2 (load reference to bool object, then retrieve its attribute)
    CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);
    CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
    // I need to return a bool so, maybe test if that is true if so return the false constant,
    // ...
    // compare to zero
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
    int endLabel = CgenSupport.generateLocalLabel();
    CgenSupport.emitBeqz(CgenSupport.T2, endLabel, s);
    CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
    CgenSupport.emitLabelDef(endLabel, s);
  }
}

/**
 * Defines AST constructor 'int_const'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
  public AbstractSymbol token;

  /**
   * Creates "int_const" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for token
   */
  public int_const(int lineNumber, AbstractSymbol a1) {
    super(lineNumber);
    token = a1;
  }

  @Override
  public TreeNode copy() {
    return new int_const(lineNumber, copy_AbstractSymbol(token));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "int_const\n");
    dump_AbstractSymbol(out, n + 2, token);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_int");
    dump_AbstractSymbol(out, n + 2, token);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    CgenSupport.emitLoadInt(
        CgenSupport.ACC, (IntSymbol) AbstractTable.inttable.lookup(token.getString()), s);
  }
}

/**
 * Defines AST constructor 'bool_const'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
  public Boolean val;

  /**
   * Creates "bool_const" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for val
   */
  public bool_const(int lineNumber, Boolean a1) {
    super(lineNumber);
    val = a1;
  }

  @Override
  public TreeNode copy() {
    return new bool_const(lineNumber, copy_Boolean(val));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "bool_const\n");
    dump_Boolean(out, n + 2, val);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_bool");
    dump_Boolean(out, n + 2, val);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
  }
}

/**
 * Defines AST constructor 'string_const'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
  public AbstractSymbol token;

  /**
   * Creates "string_const" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for token
   */
  public string_const(int lineNumber, AbstractSymbol a1) {
    super(lineNumber);
    token = a1;
  }

  @Override
  public TreeNode copy() {
    return new string_const(lineNumber, copy_AbstractSymbol(token));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "string_const\n");
    dump_AbstractSymbol(out, n + 2, token);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_string");
    out.print(Utilities.pad(n + 2) + "\"");
    Utilities.printEscapedString(out, token.getString());
    out.println("\"");
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    CgenSupport.emitLoadString(
        CgenSupport.ACC, (StringSymbol) AbstractTable.stringtable.lookup(token.getString()), s);
  }
}

/**
 * Defines AST constructor 'new_'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
  public AbstractSymbol type_name;

  /**
   * Creates "new_" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for type_name
   */
  public new_(int lineNumber, AbstractSymbol a1) {
    super(lineNumber);
    type_name = a1;
  }

  @Override
  public TreeNode copy() {
    return new new_(lineNumber, copy_AbstractSymbol(type_name));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "new_\n");
    dump_AbstractSymbol(out, n + 2, type_name);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_new");
    dump_AbstractSymbol(out, n + 2, type_name);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    // TODO implement SELF_TYPE
    // get the proto object put into a0
    CgenSupport.emitLoadAddress(CgenSupport.ACC, CgenSupport.protoObjRef(type_name), s);
    // copy proto object which will then be returned in a0
    CgenSupport.emitJal(CgenSupport.methodRef(TreeConstants.Object_, TreeConstants.copy), s);
    // call initializer
    CgenSupport.emitJal(CgenSupport.initMethodRef(type_name), s);
  }
}

/**
 * Defines AST constructor 'isvoid'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
  public Expression e1;

  /**
   * Creates "isvoid" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for e1
   */
  public isvoid(int lineNumber, Expression a1) {
    super(lineNumber);
    e1 = a1;
  }

  @Override
  public TreeNode copy() {
    return new isvoid(lineNumber, (Expression) e1.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "isvoid\n");
    e1.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_isvoid");
    e1.dump_with_types(out, n + 2);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'no_expr'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {
  /**
   * Creates "no_expr" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   */
  public no_expr(int lineNumber) {
    super(lineNumber);
  }

  @Override
  public TreeNode copy() {
    return new no_expr(lineNumber);
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "no_expr\n");
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_no_expr");
    dump_type(out, n);
  }

  /**
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    throw new UnsupportedOperationException("not implemented");
  }
}

/**
 * Defines AST constructor 'object'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
  public AbstractSymbol name;

  /**
   * Creates "object" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for name
   */
  public object(int lineNumber, AbstractSymbol a1) {
    super(lineNumber);
    name = a1;
  }

  @Override
  public TreeNode copy() {
    return new object(lineNumber, copy_AbstractSymbol(name));
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "object\n");
    dump_AbstractSymbol(out, n + 2, name);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_object");
    dump_AbstractSymbol(out, n + 2, name);
    dump_type(out, n);
  }

  /**
   * Generates code for this expression.
   *
   * @param s the output stream
   */
  @Override
  public void code(
      Class_ cls,
      SymbolTable env,
      Map<Integer, String> classTags,
      Map<String, Map<String, CgenClassTable.DispatchTableEntry>> dispatchTables,
      PrintStream s) {
    CgenClassTable.Location location = (CgenClassTable.Location) env.lookup(name);
    CgenSupport.emitLoad(CgenSupport.ACC, location.offset(), location.sourceRegister(), s);
  }
}
