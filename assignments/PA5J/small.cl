class A {
	aAttr1: Int <- 1;
	aAttr2 : Int <- 11;
	aAttr3 : Bool <- true;
	a(par:Int) : Int { par };
};
class B inherits A {
	b() : Int { 1 };
	bAttr1: String <- "wow";
	bAttr2: A;
};
class Main {
	main() : Bool {
		true
	};
};
