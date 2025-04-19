# Programming Assignment: Semantic analyzer

This is my implementation of the assignment described in `./README`. The
[PA4.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

I ran my parser against all `.cl` files from all assignments. I collected some more test input in
`./testdata/`.

* build my parser using `make semant`
* run it
  * against some cool code using `./mysemant code.cl`
  * against my test samples `./test.sh`
  * against all the course examples `./test.sh ../../examples`

## TODO

* how to start with type checking? should I put the Classes into my simple graph?

* Main:
  * [done] Every program must have a class Main.
  * [do that in ClassTable itself?] Furthermore, the Main class must have a method main that
  takes no formal parameters. The main method must be defined in class Main (not inherited from another
  class). A program is executed by evaluating (new Main).main().

* fix the symbolic links in each assignment. The mycoolc, my... use `./lexer` and so forth which
don't always exist so fail. As they are prefixed with my I guess the intetion is they only use my
implementations. How to then add the reference impl next to it

* adapt test.sh: either create a test-err.sh or make it work for errors and valid programs. Right
now I just capture stderr

* should I use AbstractSymbol as my keys for classes?
