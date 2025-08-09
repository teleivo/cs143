# Programming Assignment: Code generator

This is my implementation of the assignment described in `./README`. The
[PA5.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

I ran my code generator against

TODO update the following

* all valid Cool `.cl` files from all previous assignments
* some more test input of valid `./testdata-ok/` and invalid `./testdata-err/` Cool code
* and the `../../examples/`

Note: you might need to add a `Main` class like this

```
class Main {
	main() : Bool {
		true
	};
};
```

to the example Cool code out there. The semantic analysis will otherwise fail.

To try yourself

* build my semantic analyzer using `make cgen`
* run it
  * against some Cool code using `./mycoolc -o code.s code.cl`
  * against my test samples of valid Cool `./test.sh`
  * against my test samples of invalid Cool `./test-err.sh`
  * against all the course examples `./test.sh ../../examples`

## TODO

* implement missing expression
  * case
    * fix case_no_match test, why is Main in a0 at the time the _case_abort is called?
    * write tests for the complex class tree with all kinds of branch combinations and input
    expressions as well as show the clone env at work
    * write nested case expression
  * add tests for the error cases
    * do I need to do some void checks for example in the arith operators? some comparison to 0 and
      then some kind of abort
    * move error cases to testdata-ok? or merge them into testdata as here the same script works on
      both
  * what changes are needed to be able to use the GC? how can I also test that in test.sh?
  * what else?

* I implemented clone in SymbolTable but it looks as if its implemented in a gitignored file so
mention this somewhere

```java
  @Override
  public Object clone() {
    return new SymbolTable((Stack) tbl.clone());
  }
```

* how to get the example programs to do something useful? They did not even have a main before I
added one
* why is -4 not valid according to the reference parser?
* get fibonacci to work
  * or print sequence of fibonacci using the Cons from the manual?

* up to now I ran my generator with the reference tool chain: run only my implementation in the `my`
case in test.sh

## Limitations and improvements

I took the course suggestion to heart: don't spend much time on optimizing the amount of passes as
there is plenty of logic to get right 😅. There is definitely lots of room for improvement!

