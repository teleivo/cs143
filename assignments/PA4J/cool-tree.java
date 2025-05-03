// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
  public static final Class elementClass = Formal.class;

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

  /** Appends "Formal" element to this list */
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

  public abstract void code(PrintStream s);
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
 * Defines AST constructor 'programc'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class programc extends Program {
  protected Classes classes;
  private int semantErrors;
  private PrintStream errorStream;
  private ClassTable classTable;
  private final Set<String> baseClasses = Set.of("Object", "IO", "Int", "Bool", "String");
  private Class_ main;
  private boolean hasMainMethod;
  private SymbolTable objects;

  /**
   * Creates "programc" AST node.
   *
   * @param lineNumber the line in the source file from which this node came.
   * @param a0 initial value for classes
   */
  public programc(int lineNumber, Classes a1) {
    super(lineNumber);
    classes = a1;
  }

  @Override
  public TreeNode copy() {
    return new programc(lineNumber, (Classes) classes.copy());
  }

  @Override
  public void dump(PrintStream out, int n) {
    out.print(Utilities.pad(n) + "programc\n");
    classes.dump(out, n + 2);
  }

  @Override
  public void dump_with_types(PrintStream out, int n) {
    dump_line(out, n);
    out.println(Utilities.pad(n) + "_program");
    for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
      // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
      ((Class_) e.nextElement()).dump_with_types(out, n + 2);
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
    ClassTable classTable = new ClassTable(classes);
    if (classTable.errors()) {
      System.err.println("Compilation halted due to static semantic errors.");
      System.exit(1);
    }

    this.errorStream = System.err;
    this.classTable = classTable;
    this.objects = new SymbolTable();

    checkClasses(classTable.graph, TreeConstants.Object_.toString());

    if (this.main == null) {
      this.semantError().println("Class Main is not defined.");
    } else if (!this.hasMainMethod) {
      this.semantError(this.main.getFilename(), this.main)
          .println("No 'main' method in class Main.");
    }

    if (this.errors()) {
      System.err.println("Compilation halted due to static semantic errors.");
      System.exit(1);
    }
  }

  private void checkClasses(Map<String, List<String>> graph, String className) {
    class_c cls = classTable.classes.get(className);
    objects.enterScope();

    declareFeatures(cls);
    checkClass(cls);

    for (String neighbour : graph.get(className)) {
      checkClasses(graph, neighbour);
    }

    objects.exitScope();
  }

  private void declareFeatures(class_c cls) {
    objects.addId(TreeConstants.self, TreeConstants.SELF_TYPE);

    if (cls.name == TreeConstants.Main) {
      this.main = cls;
    }

    Set<String> classMethods = new HashSet<>();
    for (Enumeration e = cls.features.getElements(); e.hasMoreElements(); ) {
      Feature feature = ((Feature) e.nextElement());
      if (feature instanceof attr a) {
        if (a.name == TreeConstants.self) {
          this.semantError(cls.getFilename(), a)
              .println("'" + a.name + "' cannot be the name of an attribute.");
        } else if (objects.probe(a.name) != null) {
          this.semantError(cls.getFilename(), a)
              .println("Attribute " + a.name + " is multiply defined in class.");
        } else if (objects.lookup(a.name) != null) {
          this.semantError(cls.getFilename(), a)
              .println("Attribute " + a.name + " is an attribute of an inherited class.");
        } else {
          objects.addId(a.name, a.type_decl);
        }
      } else if (feature instanceof method m) {
        if (cls.name == TreeConstants.Main && m.name == TreeConstants.main_meth) {
          this.hasMainMethod = true;
        }
        if (cls.name == TreeConstants.Main
            && m.name == TreeConstants.main_meth
            && m.formals.getElements().hasMoreElements()) {
          this.semantError(cls.getFilename(), cls)
              .println("'main' method in class Main should have no arguments.");
        }

        if (classMethods.contains(m.name.toString())) {
          this.semantError(cls.getFilename(), m)
              .println("Method " + m.name + " is multiply defined.");
        } else {
          classMethods.add(m.name.toString());

          method parentMethod = lookupMethod(cls.getParent(), m.name);
          if (parentMethod != null) {
            if (parentMethod.formals.getLength() != m.formals.getLength()) {
              this.semantError(cls.getFilename(), m)
                  .println(
                      "Incompatible number of formal parameters in redefined method "
                          + m.name
                          + ".");
              continue;
            }
            if (m.return_type != parentMethod.return_type) {
              this.semantError(cls.getFilename(), m)
                  .println(
                      "In redefined method "
                          + m.name
                          + ", return type "
                          + m.return_type
                          + " is different from original return type "
                          + parentMethod.return_type
                          + ".");
            }
            for (int i = 0; i < parentMethod.formals.getLength(); i++) {
              formalc p = (formalc) parentMethod.formals.getNth(i);
              formalc c = (formalc) m.formals.getNth(i);
              if (c.type_decl != p.type_decl) {
                this.semantError(cls.getFilename(), m)
                    .println(
                        "In redefined method "
                            + m.name
                            + ", parameter type "
                            + c.type_decl
                            + " is different from original type "
                            + p.type_decl);
              }
            }
          }
        }
      }
    }
  }

  private void checkClass(class_c cls) {
    if (cls.name == TreeConstants.Main) {
      this.main = cls;
    }

    for (Enumeration e = cls.features.getElements(); e.hasMoreElements(); ) {
      Feature feature = ((Feature) e.nextElement());
      if (feature instanceof attr a) {
        checkType(cls, objects, a.init);
        if (!(a.init instanceof no_expr) && !conforms(cls, a.init.get_type(), a.type_decl)) {
          this.semantError(cls.getFilename(), a)
              .println(
                  "Inferred type "
                      + a.init.get_type()
                      + " of initialization of attribute "
                      + a.name
                      + " does not conform to declared type "
                      + a.type_decl
                      + ".");
        }
      } else if (feature instanceof method m) {
        objects.enterScope();
        for (Enumeration c = m.formals.getElements(); c.hasMoreElements(); ) {
          formalc f = (formalc) c.nextElement();

          if (f.type_decl == TreeConstants.SELF_TYPE) {
            this.semantError(cls.getFilename(), m)
                .println(
                    "Formal parameter "
                        + f.name
                        + " cannot have type "
                        + TreeConstants.SELF_TYPE
                        + ".");
          }

          objects.addId(f.name, f.type_decl);
        }

        checkType(cls, objects, m.expr);
        if (!conforms(cls, m.expr.get_type(), m.return_type)) {
          this.semantError(cls.getFilename(), m)
              .println(
                  "Inferred return type "
                      + m.expr.get_type()
                      + " of method "
                      + m.name
                      + " does not conform to declared return type "
                      + m.return_type
                      + ".");
        }
        objects.exitScope();
      }
    }
  }

  private void checkType(Class_ cls, SymbolTable objects, Expression expr) {
    // assuming no expression leads to No_type
    if (expr == null || expr instanceof no_expr) {
      expr.set_type(TreeConstants.No_type);
      return;
    } else if (expr instanceof int_const) {
      expr.set_type(TreeConstants.Int);
      return;
    } else if (expr instanceof bool_const) {
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof string_const) {
      expr.set_type(TreeConstants.Str);
      return;
    } else if (expr instanceof new_ e) {
      if (e.type_name == TreeConstants.SELF_TYPE) {
        e.set_type(TreeConstants.SELF_TYPE);
      } else {
        e.set_type(e.type_name);
      }
      return;
    } else if (expr instanceof isvoid e) {
      expr.set_type(TreeConstants.Bool);
      checkType(cls, objects, e.e1);
      return;
    } else if (expr instanceof comp e) {
      checkType(cls, objects, e.e1);
      if (e.e1.get_type() != TreeConstants.Bool) {
        this.semantError(cls.getFilename(), e)
            .println("Argument of 'not' has type " + e.e1.get_type() + " instead of Bool.");
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof neg e) {
      checkType(cls, objects, e.e1);
      if (e.e1.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("Argument of '~' has type " + e.e1.get_type() + " instead of Int.");
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof lt e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " < " + e.e2.get_type());
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof leq e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " <= " + e.e2.get_type());
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof plus e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " + " + e.e2.get_type());
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Int);
      return;
    } else if (expr instanceof sub e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " - " + e.e2.get_type());
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Int);
      return;
    } else if (expr instanceof mul e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " * " + e.e2.get_type());
      }
      expr.set_type(TreeConstants.Int);
      return;
    } else if (expr instanceof divide e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      if (e.e1.get_type() != TreeConstants.Int || e.e2.get_type() != TreeConstants.Int) {
        this.semantError(cls.getFilename(), e)
            .println("non-Int arguments: " + e.e1.get_type() + " / " + e.e2.get_type());
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Int);
      return;
    } else if (expr instanceof eq e) {
      checkType(cls, objects, e.e1);
      checkType(cls, objects, e.e2);
      Set<AbstractSymbol> basicTypes =
          Set.of(TreeConstants.Int, TreeConstants.Str, TreeConstants.Bool);
      boolean isBasic =
          basicTypes.contains(e.e1.get_type()) || basicTypes.contains(e.e2.get_type());
      if (isBasic && e.e1.get_type() != e.e2.get_type()) {
        this.semantError(cls.getFilename(), e).println("Illegal comparison with a basic type.");
        expr.set_type(TreeConstants.No_type);
        return;
      }
      expr.set_type(TreeConstants.Bool);
      return;
    } else if (expr instanceof block e) {
      // blocks do not seem to create new scopes
      for (int i = 0; i < e.body.getLength(); i++) {
        checkType(cls, objects, (Expression) e.body.getNth(i));
      }
      // blocks must have at least one expression which is enforced by the parser
      e.set_type(((Expression) e.body.getNth(e.body.getLength() - 1)).get_type());
      return;
    } else if (expr instanceof object e) {
      AbstractSymbol type = (AbstractSymbol) objects.lookup(e.name);
      if (type == null) {
        this.semantError(cls.getFilename(), e).println("Undeclared identifier " + e.name + ".");
        expr.set_type(TreeConstants.Object_);
        return;
      }
      e.set_type(type);
      return;
    } else if (expr instanceof assign e) {
      if (e.name == TreeConstants.self) {
        this.semantError(cls.getFilename(), e).println("Cannot assign to 'self'.");
      }

      AbstractSymbol type = (AbstractSymbol) objects.lookup(e.name);
      if (type == null) {
        this.semantError(cls.getFilename(), e)
            .println("Assignment to undeclared variable " + e.name + ".");
        expr.set_type(TreeConstants.No_type);
        return;
      }
      checkType(cls, objects, e.expr);
      if (!conforms(cls, e.expr.get_type(), type)) {
        this.semantError(cls.getFilename(), e)
            .println(
                "Type "
                    + e.expr.get_type()
                    + " of assigned expression does not conform to declared type "
                    + type
                    + " of identifier "
                    + e.name
                    + ".");
        expr.set_type(TreeConstants.No_type);
        return;
      }
      e.set_type(e.expr.get_type());
      return;
    } else if (expr instanceof cond e) {
      checkType(cls, objects, e.pred);
      if (e.pred.get_type() != TreeConstants.Bool) {
        this.semantError(cls.getFilename(), e)
            .println("Predicate of 'if' does not have type Bool.");
        // I assume that we can still type check the body regardless of ill-typed predicates
      }
      checkType(cls, objects, e.then_exp);
      checkType(cls, objects, e.else_exp);
      e.set_type(joinTypes(cls, e.then_exp.get_type(), e.else_exp.get_type()));
      return;
    } else if (expr instanceof loop e) {
      checkType(cls, objects, e.pred);
      if (e.pred.get_type() != TreeConstants.Bool) {
        this.semantError(cls.getFilename(), e).println("Loop condition does not have type Bool.");
        // I assume that we can still type check the body regardless of ill-typed predicates
      }
      checkType(cls, objects, e.body);
      e.set_type(TreeConstants.Object_);
      return;
    } else if (expr instanceof let e) {
      if (e.identifier == TreeConstants.self) {
        this.semantError(cls.getFilename(), e)
            .println("'self' cannot be bound in a 'let' expression.");
      }

      AbstractSymbol t0 = e.type_decl;
      if (e.type_decl == TreeConstants.SELF_TYPE) {
        t0 = TreeConstants.SELF_TYPE;
      }

      if (e.init != null) { // initialization expression is optional
        checkType(cls, objects, e.init);
        if (!conforms(cls, e.init.get_type(), t0)) {
          this.semantError(cls.getFilename(), e)
              .println(
                  "Inferred type "
                      + e.init.get_type()
                      + " of initialization of "
                      + e.identifier
                      + " does not conform to identifier's declared type "
                      + e.type_decl
                      + ".");
          expr.set_type(TreeConstants.No_type);
          return;
        }
      }

      objects.enterScope();
      objects.addId(e.identifier, t0);
      checkType(cls, objects, e.body);
      objects.exitScope();
      e.set_type(e.body.get_type());
      return;
    } else if (expr instanceof typcase e) {
      checkType(cls, objects, e.expr);

      Set<AbstractSymbol> branchTypes = new HashSet<>();
      Set<AbstractSymbol> branchExprTypes = new HashSet<>();
      Set<branch> duplicateBranches = new HashSet<>();
      for (Enumeration c = e.cases.getElements(); c.hasMoreElements(); ) {
        branch b = (branch) c.nextElement();
        if (branchTypes.contains(b.type_decl)) {
          duplicateBranches.add(b);
        }
        branchTypes.add(b.type_decl);

        if (!this.classTable.classes.containsKey(b.type_decl.toString())) {
          this.semantError(cls.getFilename(), b)
              .println("Class " + b.type_decl + " of case branch is undefined.");
        }

        objects.enterScope();
        if (b.name == TreeConstants.self) {
          this.semantError(cls.getFilename(), b).println("'self' bound in 'case'.");
        } else {
          objects.addId(b.name, b.type_decl);
        }
        checkType(cls, objects, b.expr);
        branchExprTypes.add(b.expr.get_type());
        objects.exitScope();
      }

      for (branch b : duplicateBranches) {
        this.semantError(cls.getFilename(), b)
            .println("Duplicate branch " + b.type_decl + " in case statement.");
      }

      e.set_type(joinTypes(cls, branchExprTypes));
      return;
    } else if (expr instanceof dispatch e) {
      AbstractSymbol targetClass;
      // expr is optional - shorthand for self.<id>(<expr>,...,<expr>)
      checkType(cls, objects, e.expr);
      targetClass = e.expr.get_type();
      // TODO or should lookupMethod support SELF_TYPE
      if (e.expr.get_type() == TreeConstants.SELF_TYPE) {
        targetClass = cls.getName();
      }

      AbstractSymbol targetMethodName = e.name;
      method target = lookupMethod(targetClass, targetMethodName);
      if (target == null) {
        this.semantError(cls.getFilename(), e)
            .println("Dispatch to undefined method " + targetMethodName + ".");
        e.set_type(TreeConstants.No_type);
        return;
      }

      // TODO how does the short-hand notation influence this?
      if (target.return_type == TreeConstants.SELF_TYPE) {
        e.set_type(e.expr.get_type());
      } else {
        e.set_type(target.return_type);
      }

      if (target.formals.getLength() != e.actual.getLength()) {
        this.semantError(cls.getFilename(), e)
            .println("Method " + targetMethodName + " called with wrong number of arguments.");
        return;
      }
      for (int i = 0; i < target.formals.getLength(); i++) {
        formalc t = (formalc) target.formals.getNth(i);
        Expression a = (Expression) e.actual.getNth(i);
        checkType(cls, objects, a);
        if (!conforms(cls, a.get_type(), t.type_decl)) {
          this.semantError(cls.getFilename(), e)
              .println(
                  "In call of method "
                      + targetMethodName
                      + ", type "
                      + a.get_type()
                      + " of parameter "
                      + t.name
                      + " does not conform to declared type "
                      + t.type_decl
                      + ".");
        }
      }
    } else if (expr instanceof static_dispatch e) {
      checkType(cls, objects, e.expr);

      // m@T(E1,…,En) - T cannot be SELF_TYPE
      if (e.type_name == TreeConstants.SELF_TYPE) {
        this.semantError(cls.getFilename(), e).println("Static dispatch to SELF_TYPE.");
        e.set_type(TreeConstants.No_type);
        return;
      }

      if (!conforms(cls, e.expr.get_type(), e.type_name)) {
        this.semantError(cls.getFilename(), e)
            .println(
                "Expression type "
                    + e.expr.get_type()
                    + " does not conform to declared static dispatch type "
                    + e.type_name
                    + ".");
        e.set_type(TreeConstants.No_type);
        return;
      }

      AbstractSymbol targetClass = e.type_name;
      AbstractSymbol targetMethodName = e.name;
      method target = lookupMethod(targetClass, targetMethodName);
      if (target == null) {
        this.semantError(cls.getFilename(), e)
            .println("Dispatch to undefined method " + targetMethodName + ".");
        e.set_type(TreeConstants.No_type);
        return;
      }

      if (target.return_type == TreeConstants.SELF_TYPE) {
        e.set_type(e.expr.get_type());
      } else {
        e.set_type(target.return_type);
      }

      if (target.formals.getLength() != e.actual.getLength()) {
        this.semantError(cls.getFilename(), e)
            .println("Method " + targetMethodName + " called with wrong number of arguments.");
        return;
      }
      for (int i = 0; i < target.formals.getLength(); i++) {
        formalc t = (formalc) target.formals.getNth(i);
        Expression a = (Expression) e.actual.getNth(i);
        checkType(cls, objects, a);
        if (!conforms(cls, a.get_type(), t.type_decl)) {
          this.semantError(cls.getFilename(), e)
              .println(
                  "In call of method "
                      + targetMethodName
                      + ", type "
                      + a.get_type()
                      + " of parameter "
                      + t.name
                      + " does not conform to declared type "
                      + t.type_decl
                      + ".");
        }
      }
    }
  }

  /*
   * Indicates if class a <= ancestor.
   * See Cool Manual Definition 4.1 (Conformance).
   * In addition, No_type which is assigned to ill-typed expressions conforms to any class to
   * prevent cascading errors.
   **/
  private boolean conforms(Class_ cls, AbstractSymbol a, AbstractSymbol ancestor) {
    if (a == ancestor) { // includes SELF_TYPE_C ≤ SELF_TYPE_C
      return true;
    }
    if (a == TreeConstants.No_type) {
      return true;
    }
    if (a == TreeConstants.Object_) {
      return false;
    }
    if (ancestor == TreeConstants.SELF_TYPE) {
      return false;
    }
    if (a == TreeConstants.SELF_TYPE) { // SELF_TYPEC ≤ T1 if C ≤ T1
      return conforms(cls, cls.getName(), ancestor);
    }

    class_c aCls = this.classTable.classes.get(a.toString());
    return conforms(cls, aCls.parent, ancestor);
  }

  private AbstractSymbol joinTypes(Class_ cls, Set<AbstractSymbol> types) {
    boolean first = true;
    AbstractSymbol common = null;
    for (AbstractSymbol a : types) {
      if (first) {
        common = a;
        first = false;
      } else {
        common = joinTypes(cls, common, a);
      }

      // no need to join more types if two of them only have Object as their least common
      // ancestor
      if (common == TreeConstants.Object_) {
        return common;
      }
    }
    return common;
  }

  /*
   * Join static types by walking the paths from the Object root until their nodes differ.
   * See Cool Manual 7.5 Conditionals.
   *
   **/
  private AbstractSymbol joinTypes(Class_ cls, AbstractSymbol a, AbstractSymbol b) {
    // No_type ≤ C for all types C
    if (a == TreeConstants.No_type) {
      return b;
    }
    if (b == TreeConstants.No_type) {
      return a;
    }
    if (a == TreeConstants.SELF_TYPE && b == TreeConstants.SELF_TYPE) {
      return TreeConstants.SELF_TYPE;
    }
    if (a == TreeConstants.SELF_TYPE) { // lub(SELF_TYPEC, T1) = lub(C, T1)
      a = cls.getName();
    }
    if (b == TreeConstants.SELF_TYPE) { // lub(T1, SELF_TYPEC) = lub(C, T1)
      b = cls.getName();
    }

    List<AbstractSymbol> pathA = rootPath(a);
    List<AbstractSymbol> pathB = rootPath(b);
    int i = pathA.size() - 1;
    int j = pathB.size() - 1;
    AbstractSymbol common = null;
    while (i >= 0 && j >= 0 && pathA.get(i) == pathB.get(j)) {
      common = pathA.get(i);
      i--;
      j--;
    }
    return common;
  }

  private List<AbstractSymbol> rootPath(AbstractSymbol a) {
    List<AbstractSymbol> path = new ArrayList<>();
    rootPath(a, path);
    return path;
  }

  private void rootPath(AbstractSymbol a, List<AbstractSymbol> result) {
    result.add(a);

    if (a == TreeConstants.Object_) {
      return;
    }
    class_c cls = this.classTable.classes.get(a.toString());
    rootPath(cls.parent, result);
  }

  private method lookupMethod(AbstractSymbol className, AbstractSymbol methodName) {
    if (className == TreeConstants.No_type) {
      return null;
    }
    if (className == TreeConstants.No_class) {
      return null;
    }

    // TODO Bool has no methods so I get an NPE. I think I should add it
    // assuming the class exists
    // System.out.println(className + " " + methodName);
    // System.out.println(this.classTable.methods.get(className.toString()));
    method m = this.classTable.methods.get(className.toString()).get(methodName.toString());
    if (m != null) {
      return m;
    }
    // method does not exist if not found in Object
    if (className == TreeConstants.Object_) {
      return null;
    }

    return lookupMethod(this.classTable.classes.get(className.toString()).parent, methodName);
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

  /**
   * Prints the file name and the line number of the given tree node.
   *
   * <p>Also increments semantic error count.
   *
   * @param filename the file name
   * @param t the tree node
   * @return a print stream to which the rest of the error message is to be printed.
   */
  public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
    errorStream.print(filename + ":" + t.getLineNumber() + ": ");
    return semantError();
  }

  /**
   * Increments semantic error count and returns the print stream for error messages.
   *
   * @return a print stream to which the error message is to be printed.
   */
  public PrintStream semantError() {
    semantErrors++;
    return errorStream;
  }

  /** Returns true if there are any static semantic errors. */
  public boolean errors() {
    return semantErrors != 0;
  }
}

/**
 * Defines AST constructor 'class_c'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
  protected AbstractSymbol name;
  protected AbstractSymbol parent;
  protected Features features;
  protected AbstractSymbol filename;

  /**
   * Creates "class_c" AST node.
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
    out.print(Utilities.pad(n) + "class_c\n");
    dump_AbstractSymbol(out, n + 2, name);
    dump_AbstractSymbol(out, n + 2, parent);
    features.dump(out, n + 2);
    dump_AbstractSymbol(out, n + 2, filename);
  }

  @Override
  public AbstractSymbol getFilename() {
    return filename;
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
  public Features getFeatures() {
    return features;
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
}

/**
 * Defines AST constructor 'method'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
  protected AbstractSymbol name;
  protected Formals formals;
  protected AbstractSymbol return_type;
  protected Expression expr;

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
      ((Formal) e.nextElement()).dump_with_types(out, n + 2);
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
  protected AbstractSymbol name;
  protected AbstractSymbol type_decl;
  protected Expression init;

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
  protected AbstractSymbol name;
  protected AbstractSymbol type_decl;

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
  protected AbstractSymbol name;
  protected AbstractSymbol type_decl;
  protected Expression expr;

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
  protected AbstractSymbol name;
  protected Expression expr;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'static_dispatch'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
  protected Expression expr;
  protected AbstractSymbol type_name;
  protected AbstractSymbol name;
  protected Expressions actual;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'dispatch'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
  protected Expression expr;
  protected AbstractSymbol name;
  protected Expressions actual;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'cond'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
  protected Expression pred;
  protected Expression then_exp;
  protected Expression else_exp;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'loop'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
  protected Expression pred;
  protected Expression body;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'typcase'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
  protected Expression expr;
  protected Cases cases;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'block'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
  protected Expressions body;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'let'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
  protected AbstractSymbol identifier;
  protected AbstractSymbol type_decl;
  protected Expression init;
  protected Expression body;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'plus'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
  protected Expression e1;
  protected Expression e2;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'sub'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
  protected Expression e1;
  protected Expression e2;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'mul'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
  protected Expression e1;
  protected Expression e2;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'divide'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
  protected Expression e1;
  protected Expression e2;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'neg'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
  protected Expression e1;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'lt'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
  protected Expression e1;
  protected Expression e2;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'eq'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
  protected Expression e1;
  protected Expression e2;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'leq'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
  protected Expression e1;
  protected Expression e2;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'comp'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
  protected Expression e1;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'int_const'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
  protected AbstractSymbol token;

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
   * Generates code for this expression. This method method is provided to you as an example of code
   * generation.
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {
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
  protected Boolean val;

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
   * Generates code for this expression. This method method is provided to you as an example of code
   * generation.
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {
    CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
  }
}

/**
 * Defines AST constructor 'string_const'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
  protected AbstractSymbol token;

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
   * Generates code for this expression. This method method is provided to you as an example of code
   * generation.
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {
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
  protected AbstractSymbol type_name;

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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'isvoid'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
  protected Expression e1;

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
  public void code(PrintStream s) {}
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
  public void code(PrintStream s) {}
}

/**
 * Defines AST constructor 'object'.
 *
 * <p>See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
  protected AbstractSymbol name;

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
   * Generates code for this expression. This method is to be completed in programming assignment 5.
   * (You may add or remove parameters as you wish.)
   *
   * @param s the output stream
   */
  @Override
  public void code(PrintStream s) {}
}
