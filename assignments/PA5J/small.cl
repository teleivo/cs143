class A {
	aAttr1: Int <- 1;
	aAttr2 : Int <- 11;
	aAttr3 : Bool <- true;
	a(par:Int) : Int { par };
};
class B inherits A {
	b() : Int { 23 + 24 };
	bAttr1: String <- "wow";
	bAttr2: A;
};
class C inherits B {
	cAttr1 : Int;
	cAttr2 : Int <- 12;
	cAttr3 : Int;
	init(in3 : Int) : Int {
		1
	};
};
class Main {
	main() : Bool {
		true
	};
};
