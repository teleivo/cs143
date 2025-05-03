# Programming Assignment: Semantic analyzer

This is my implementation of the assignment described in `./README`. The
[PA4.pdf](https://web.stanford.edu/class/cs143/handouts/PA4.pdf) contains more details.

I ran my semantic analyzer against
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

* build my semantic analyzer using `make semant`
* run it
  * against some Cool code using `./mysemant code.cl`
  * against my test samples of valid Cool `./test.sh`
  * against my test samples of invalid Cool `./test-err.sh`
  * against all the course examples `./test.sh ../../examples`

## TODO

* fix remaining todos

* fix the symbolic links in each assignment. The mycoolc, my... use `./lexer` and so forth which
don't always exist so fail. As they are prefixed with my I guess the intetion is they only use my
implementations. How to then add the reference impl next to it

## Limitations and improvements

I took the course suggestion to heart: don't spend much time on optimizing the amount of passes as
there is plenty of logic to get right 😅. There is definitely lots of room for improvement!

I would also put more effort into cleaning the code up for production. For example
* add `hashCode` to the `AbstractSymbol` for using it in hash based data structures
* probably extract some more helpers to add semantic errors
* try using the `SymbolTable` for methods as well. I felt like I would have to re-run the method
declaration logic as the `SymbolTable` cannot be copied. Maybe my understanding is wrong of the
method environment. It is global but per class with all class and inherited methods.
