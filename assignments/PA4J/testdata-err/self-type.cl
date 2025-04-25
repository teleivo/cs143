class A inherits B {};
class B {
	b(x: SELF_TYPE) : SELF_TYPE {
		self
	};
};
class Main {
	main() : Bool {
		{
			(new B).b(new A);
			true;
		}
	};
};
