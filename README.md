# CS143 Compilers

This repo contains my solutions to the [CS143 Compilers](https://web.stanford.edu/class/cs143/)
class assignments in Java.

Try solving them yourself before you peek :grin:.

## Setup

1. Get the Cool Language reference manual and other resources from
   https://web.stanford.edu/class/cs143.
2. Get the code like the reference compiler and MIPS simulator from the Stanford Compilers course on
   https://www.edx.org.
3. I did not run the code in a VM as suggested in the edx course.
   https://github.com/afterthat97/cool-compiler helped me in setting up the missing dependencies.
4. I also had to add `./bin/java_cup` with an absolute path to the CUP library. You will need to
   adjust that path.
5. Ensure you have at least Java 17 installed if you want to run my code.

## Testing

Each assignment

* **PA2J (Lexer)**
* **PA3J (Parser)**
* **PA4J (Semantic Analyzer)**
* **PA5J (Code Generator)**

includes

* a `README` which guides you through the assignments template code
* a `Makefile` to build the assignment
* my `README.md` with instructions and notes specific to my solution
* my `test.sh` (some also a `test-err.sh`) script that verifies my implementation against the
reference implementation

The `examples-ok` directory contains additional Cool programs based on the course example code. I've
added `main()` methods so the example code is actually running and can automatically be tested using
`test.sh`.

## Assignment Numbers

The assignment numbers are offset by 1. Even though the assignment number might be 4 on the website
and or PDF, the directory will be named "PA5J". I have no clue why that is.

## Limitations and improvements

I took the course suggestion to heart 😅
> don't spend much time on optimizing the amount of passes as there is plenty of logic to get right

There is definitely lots of room for improvement!

Some improvements I or you :yum: could make
* use proper generics in the CS143 template code like `Enumeration`
* expand the stack in one go for all things that the current activation needs to store instead of
pushing/incrementing every single item
* store local variables in registers before using the stack (register allocation)
* only store local variables that are actually used (dead code elimination)
* ...

## License

This repo contains my solutions to the [CS143 Compilers](https://web.stanford.edu/class/cs143/)
class assignments in Java that relies on code from the course. The code can be obtained from the
Stanford Compilers course on https://www.edx.org/. The license is present in a header in all Java
classes and copied into [LICENSE](./LICENSE).

