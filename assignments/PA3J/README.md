# Programming Assignment: Parser

This is my implementation of the assignment described in `./README`. The
[PA2.pdf](https://web.stanford.edu/class/cs143/handouts/PA2.pdf) contains more details.

I ran my parser against all `.cl` files from all assignments. I collected some more test input in
`./testdata/`. You can run the reference implementation of the parser against mine by first
building the lexer via `make parser` and then running

## TODO

* finish method
* continue with expressions starting with let
* test all expressions so far once method is in

* think about error handling in constructs I can currently parse

> Your test file bad.cl should have some instances that illustrate the errors from which your parser can
recover. To receive full credit, your parser should recover in at least the following situations:
• If there is an error in a class definition but the class is terminated properly and the next class is
syntactically correct, the parser should be able to restart at the next class definition.
• Similarly, the parser should recover from errors in features (going on to the next feature), a let binding
(going on to the next variable), and an expression inside a {...} block.

like this one maybe? if the attribute name is a TYPEID

> Feature names must begin with a lowercase letter. No method name may be defined multiple times in
a class, and no attribute name may be defined multiple times in a class, but a method and an
attribute may have the same name.

* how to run the reference parser against my testdata? can I diff them? or not due to linenr

* do classes need to end in a SEMI? the current class rule says so but the grammar does not
> An occasional source of confusion in Cool is the use of semi-colons (“;”). Semi-colons are used
as terminators in lists of expressions (e.g., the block syntax above) and not as expression
separators. Semi-colons also terminate other Cool constructs, see Section 11 for details.
