# Programming Assignment: Code generator

This is my implementation of the assignment described in `./README`. The
[PA5.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

I ran my code generator against

* all valid Cool `.cl` files from all previous assignments
* some more test input of valid and invalid Cool code `./testdata`
* and the `../../examples/`

Note: you'll need to add a `Main` class like this

```cool
class Main {
	main() : Bool {
		true
	};
};
```

to the example Cool code out there. The semantic analysis will otherwise fail.

I implemented clone in `SymbolTable` to clone the Cool environment for each case branch which
can define a different identifier. You need to add this method

```java
  @Override
  public Object clone() {
    return new SymbolTable((Stack) tbl.clone());
  }
```

To then try yourself

* build my semantic analyzer using `make cgen`
* run it
  * against some Cool code using `./mycoolc -o code.s code.cl`
  * against my test samples of valid/invalid Cool `./test.sh`
  * against all the course examples `./test.sh ../../examples`

## TODO

* fix bug in gc_test.cl
* get fibonacci to work
  * or print sequence of fibonacci using the Cons from the manual?
* why is -4 not valid according to the reference parser?
* how to get the example programs to do something useful? They did not even have a main before I
added one
* up to now I ran my generator with the reference tool chain: run only my implementation in the `my`
case in test.sh
* fix main README.md todos

## Limitations and improvements

I took the course suggestion to heart: don't spend much time on optimizing the amount of passes as
there is plenty of logic to get right 😅. There is definitely lots of room for improvement!

