# Programming Assignment: Semantic analyzer

This is my implementation of the assignment described in `./README`. The
[PA4.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

I ran my parser against all `.cl` files from all assignments. I collected some more test input in
`./testdata/`.

* build my parser using `make semant`
* run it
  * against some Cool code using `./mysemant code.cl`
  * against my test samples of valid Cool `./test.sh`
  * against my test samples of invalid Cool `./test-err.sh`
  * against all the course examples `./test.sh ../../examples`

## TODO

* lookup of attributes in hierarchy is not correct
  * do I have a test showing you cannot override an attribute but can override a method?

> In the case that a parent and child both define the same method name, then the definition
given in the child class takes precedence. It is illegal to redefine attribute names. Furthermore, for type
safety, it is necessary to place some restrictions on how methods may be redefined (see Section 6).

* fix remaining todos

* run test.sh against examples comparing the dump types output
* test scope in its own .cl test class

* fix the symbolic links in each assignment. The mycoolc, my... use `./lexer` and so forth which
don't always exist so fail. As they are prefixed with my I guess the intetion is they only use my
implementations. How to then add the reference impl next to it

