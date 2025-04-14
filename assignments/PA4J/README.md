# Programming Assignment: Semantic analyzer

This is my implementation of the assignment described in `./README`. The
[PA4.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

TODO: repeat what I did for the other stages
I ran my parser against all `.cl` files from all assignments. I collected some more test input in
`./testdata-ok/`.

* build my parser using `make semant`
* run it
  * against some cool code using `./mysemant code.cl`
  * against my test samples `./test.sh`
  * against all the course examples `./test.sh ../../examples`

TODO: repeat what I did for the other stages
I diffed my lexer in [PA2](../PA2J/) against the reference lexer. I wanted to do the same with my
parser but the reference implementation hangs forever.

## TODO

* build the class graph
* fail if inherits from class that does not exist
  * use separate class table? using the symbol table? as described in the type checking?
    classes only have one scope so enter once and never exit. Or just use a hashmap

3.2 Inheritance

There is a distinguished class Object. If a class definition does not specify a parent class, then the
class inherits from Object by default. A class may inherit only from a single class; this is aptly called
“single inheritance.”2 The parent-child relation on classes defines a graph. This graph may not contain
cycles. For example, if C inherits from P, then P must not inherit from C. Furthermore, if C inherits from
P, then P must have a class definition somewhere in the program.

Because Cool has single inheritance, it follows that if both of these restrictions are satisfied, then the inheritance graph forms a tree with Object
as the root.

In addition to Object, Cool has four other basic classes: Int, String, Bool, and IO. The basic classes
are discussed in Section 8.

9 Main Class

Every program must have a class Main.

Furthermore, the Main class must have a method main that
takes no formal parameters. The main method must be defined in class Main (not inherited from another
class). A program is executed by evaluating (new Main).main().


* fix the symbolic links in each assignment. The mycoolc, my... use `./lexer` and so forth which
don't always exist so fail. As they are prefixed with my I guess the intetion is they only use my
implementations. How to then add the reference impl next to it

Looks like we cannot inherit from String, Int, Bool but we can inherit from IO and Object
(explicitly)

```
class B inherits String {};

Class B cannot inherit class String.

class B inherits Int {};
Class B cannot inherit class Int.

class B inherits Bool {};
Class B cannot inherit class Bool.
```

```
class B inherits Z {};

testdata/inheritance.cl:3: Class B inherits from an undefined class Z.
```

```
class A inherits B {};
class C inherits B {};
class B inherits A {};

testdata/inheritance.cl:3: Class B, or an ancestor of B, is involved in an inheritance cycle.
testdata/inheritance.cl:2: Class C, or an ancestor of C, is involved in an inheritance cycle.
testdata/inheritance.cl:1: Class A, or an ancestor of A, is involved in an inheritance cycle.
```

