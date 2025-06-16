class A {
	aAttr1: Int <- 1;
	aAttr2 : Int <- 1;
	aAttr3 : Bool <- true;
	a() : Int { 1 };
};
class B inherits A {
	b() : Int { 1 };
	bAttr1: String <- "wow";
};
class Main {
	main() : Bool {
		true
	};
};
