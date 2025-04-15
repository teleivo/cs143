import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class may be used to contain the semantic information such as the inheritance graph. You may
 * use it or not as you like: it is only here to provide a container for the supplied methods.
 */
class ClassTable {
  private int semantErrors;
  private PrintStream errorStream;

  /**
   * Creates data structures representing basic Cool classes (Object, IO, Int, Bool, String). Please
   * note: as is this method does not do anything useful; you will need to edit it to make if do
   * what you want.
   */
  private void installBasicClasses() {
    AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

    // The following demonstrates how to create dummy parse trees to
    // refer to basic Cool classes.  There's no need for method
    // bodies -- these are already built into the runtime system.

    // IMPORTANT: The results of the following expressions are
    // stored in local variables.  You will want to do something
    // with those variables at the end of this method to make this
    // code meaningful.

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

    /* Do somethind with Object_class, IO_class, Int_class,
    Bool_class, and Str_class here */

  }

  public ClassTable(Classes cls) {
    this.semantErrors = 0;
    this.errorStream = System.err;

    boolean hasMain = false;
    Set<String> declared = new HashSet<String>();
    Set<String> prohibited = Set.of("String", "Int", "Bool");

    for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
      class_c cl = (class_c) e.nextElement();

      // duplicate class declarations
      if (declared.contains(cl.name.toString())) {
        this.semantError(cl.filename, cl).println("Class " + cl.name + " was previously defined.");
      } else {
        declared.add(cl.name.toString());
      }

      if ("Main".equals(cl.name.getString())) {
        hasMain = true;
      }
    }

    declared.add("Object");
    declared.add("IO");

    // build graph using adjacency list
    Map<String, List<String>> graph = new HashMap<>();
    graph.put("Object", new ArrayList<>(List.of("IO"))); // root of inheritance with builtin child
    graph.put("IO", new ArrayList<>()); // only builtin class one can inherit from other than Object
    for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
      class_c cl = (class_c) e.nextElement();

      if (prohibited.contains(cl.parent.toString())) {
        this.semantError(cl.filename, cl)
            .println("Class " + cl.name + " cannot inherit class " + cl.parent + ".");
      } else if (!declared.contains(cl.parent.toString())) {
        this.semantError(cl.filename, cl)
            .println("Class " + cl.name + " inherits from an undefined class " + cl.parent + ".");
      } else {
        graph.putIfAbsent(cl.name.toString(), new ArrayList<>());
        graph.putIfAbsent(cl.parent.toString(), new ArrayList<>());
        graph.get(cl.parent.toString()).add(cl.name.toString());
      }
    }

    // only detect cycles if we at least know all classes are defined
    if (this.semantErrors > 0) {
      return;
    }

    // using dfs to find cycles, note that I need to do a dfs over all classes as the hierarchy
    // might not be a single strongly connected component. In fact if a class is part of the
    // Object hierarchy it is not involved in a cycle. If it is not part of that hierarchy it
    // forms a cycle inside a separate component.
    Set<String> cycles = new HashSet<>();
    Set<String> finished = new HashSet<>(graph.size());
    for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
      Set<String> visited = new HashSet<>(graph.size());
      String vertex = entry.getKey();
      if (hasCycle(graph, finished, visited, vertex)) {
        cycles.addAll(visited);
      }
    }

    // this is the 3rd iteration over classes which I could optimize to iterating over cycles if
    // I would collect class_c instead of only class names. I am to lazy to do that :joy:
    for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
      class_c cl = (class_c) e.nextElement();
      if (cycles.contains(cl.name.toString())) {
        this.semantError(cl.filename, cl)
            .println(
                "Class "
                    + cl.name
                    + ", or an ancestor of "
                    + cl.name
                    + ", is involved in an inheritance cycle.");
      }
    }

    if (this.semantErrors > 0) {
      return;
    }

    if (!hasMain) {
      this.semantError().println("Class Main is not defined.");
    }
  }

  /**
   * Find cycles in graph starting from given vertex. Returns true if cycle is found with vertices
   * that are part of the cycle collected in visited. Vertices are marked as finished when a cycle
   * has been detected or all neighbours have been visited so that we don't revisit the connected
   * component.
   */
  private static boolean hasCycle(
      Map<String, List<String>> graph, Set<String> finished, Set<String> visited, String vertex) {
    if (finished.contains(vertex)) {
      return false; // processed this component already
    }

    if (visited.contains(vertex)) {
      return true; // found cycle
    }

    visited.add(vertex);

    for (String neighbour : graph.get(vertex)) {
      if (hasCycle(graph, finished, visited, neighbour)) {
        finished.add(vertex);
        return true;
      }
    }

    finished.add(vertex);
    return false;
  }

  /**
   * Prints line number and file name of the given class.
   *
   * <p>Also increments semantic error count.
   *
   * @param c the class
   * @return a print stream to which the rest of the error message is to be printed.
   */
  public PrintStream semantError(class_c c) {
    return semantError(c.getFilename(), c);
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
