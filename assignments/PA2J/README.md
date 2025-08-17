# Programming Assignment: Scanner

This is my implementation of the assignment described in `./README`. The
[PA1.pdf](https://web.stanford.edu/class/cs143/handouts/PA1.pdf) contains more details.

I ran my lexer against all `.cl` files from all assignments. I collected some more test input in
`./testdata/`.

To try yourself

* build my lexer using `make lexer`
* run it
  * against some Cool code using `./lexer code.cl`
  * against my test samples `./test.sh`
  * against all the course examples `./test.sh ../../examples`

## Limitations

`./testdata/strings-eof.cl` shows that the reference implementation incorrectly returns `ERROR
"Unterminated string constant"` instead of `ERROR "EOF in string constant"` as required by
[PA1.pdf](https://web.stanford.edu/class/cs143/handouts/PA1.pdf) 4.1 Error handling. At first I was
able to implement this case correctly using a `STRING` state and `%eofval` code. This did however
cause my implementation to not lex some `../../examples/` correctly. On the one hand I need to match
unterminated strings only until the end of the line to adhere to

> Report only the first string constant error to occur, and then
resume lexing after the end of the string. The end of the string is defined as either
1. the beginning of the next line if an unescaped newline occurs at any point in the string;

and not match too greedily. On the other hand I need to detect that there is no next character
anymore inside of my `lexString` method. I do not know of such a possibility. This might also be why
the reference implementation behaves in the same way :shrug:.
