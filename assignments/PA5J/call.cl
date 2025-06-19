class A {
	identity() : Int {
		1
	};
};
class Main inherits A {
	foo() : Int {
		identity()
	};
	identity() : Int {
		2
	};
	main() : Int {
		identity()
	};
};
