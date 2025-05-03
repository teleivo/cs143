class A {
	-- ok to refer to another attribute in attribute initialization
	-- even if that attribute is declared later on
	b: Int <- a;
	-- ok to refer to a method in an attribute initialization
	a: Int <- c();
	c() : Int {
		1
	};
};
class B inherits A {
	-- ok to refer to an inherited method in an attribute initialization
	b1: Int <- c();
	c: Int <- b;
};
class Main {
	main() : Bool {
		true
	};
};
