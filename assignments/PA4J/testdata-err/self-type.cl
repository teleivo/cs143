class A inherits B {};
class B {
	-- SELF_TYPE is not allowed as parameter
	b(x: SELF_TYPE) : SELF_TYPE {
		new B
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
