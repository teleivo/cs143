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

* start with generating the fixed labels things shown in Figure 3 of runtime
  * .data section
  * tags for all classes
    * _int_tag
    * _bool_tag
    * _string_tag
    * no IO? why?
  * class_nameTab
  * bool_const0
  * _protObj for all classes in the fixed labels table at least
  * class_objTab refering to _protObj and _init or other methods?
  * dispatch table for each class
  * .text section
    * with init methods
  * get `small.cl` to compile with my code and run successfully
* get a function call to compile and run
* get a recursive call to work that takes input like fact or fibonacci
* get branches to work
* look at error handling like in a case statement
* handle void
* get a loop to work

## Limitations and improvements

I took the course suggestion to heart: don't spend much time on optimizing the amount of passes as
there is plenty of logic to get right 😅. There is definitely lots of room for improvement!

