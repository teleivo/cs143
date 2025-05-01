class A inherits B {
	do() : Bool {
		true
	};
};
class B {
	-- SELF_TYPE is not allowed as parameter
	b(x: SELF_TYPE) : SELF_TYPE {
		new B
	};
};
class C inherits IO {};
class Main {
	main() : Bool {
		{
			(new B).b(new A);
			(new B)@SELF_TYPE.b(new A);
			(new C)@A.do();
			true;
		}
	};
};
