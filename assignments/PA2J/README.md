# Programming Assignment: Scanner

This is my implementation of the assignment described in `./README`. The
[PA1.pdf](https://web.stanford.edu/class/cs143/handouts/PA1.pdf) contains more details.

I ran my lexer against all `.cl` files from all assignments. I collected some more test input in
`./testdata/`. You can run the reference implementation of the scanner against mine by first
building the lexer via `make lexer` and then running

```sh
./test.sh
```

There are some noticeable differences between my and the reference implementation.

* `./testdata/strings-eof.cl`: is incorrectly implemented by the reference implementation it returns
`ERROR "Unterminated string constant"` instead of `ERROR "EOF in string constant"`.
* `./testdata/strings-unescaped-newline.cl`: the reference implementation does not handle this well
IMHO. The reference implementation wrongly lexes `OK` as `TYPEID`. I think it does so according to
the assignment but leads to cascading errors by not consuming the entire string.
* `./testdata/strings-carriage-return.cl`: the carriage return increases the line number accessible
to me via `yyline` which leads me to report an invalid line number compared to the reference
implementation. I think this might be due to the Java implementation (jlex?) I rely on. I don't know
of an easy way to fix this.

