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

## Notes

**Making example programs useful:**
The `../../examples-ok/` directory contains Cool programs with meaningful `Main` classes that exercise the example code and produce useful output. These examples can be tested with your compiler using `./test.sh ../../examples-ok/` to verify your implementation produces the same output as the reference compiler.

## Limitations and improvements

I took the course suggestion to heart: don't spend much time on optimizing the amount of passes as
there is plenty of logic to get right 😅. There is definitely lots of room for improvement!

