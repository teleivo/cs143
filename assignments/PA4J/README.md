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

* support SELF_TYPE
  * add all illegal places where SELF_TYPE cannot be used to ./testdata-err/self-type.cl
    * add static dispatch to disallowed test
  * handle self in object expression?
  * check I support all places where its allowed

attention
> A use of SELF_TYPE always refers to any subtype of the
current class
– The exception is the type checking of dispatch. The method return
type of SELF_TYPE might have nothing to do with the current
class

* fix remaining todos

* run test.sh against examples comparing the dump types output
* test scope in its own .cl test class

* fix the symbolic links in each assignment. The mycoolc, my... use `./lexer` and so forth which
don't always exist so fail. As they are prefixed with my I guess the intetion is they only use my
implementations. How to then add the reference impl next to it

