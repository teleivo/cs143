class A {
	a(x : Int, y : Int) : Int {
		1
	};
};
class B inherits A {
	a(x : Int, y : Int) : Int {
		2
	};
};
Class Main {
	a() : Int { 1 };
	main(): Int {
		{
			a();
			(new A).a(1, 2);
			(new B).a(1, 2);
			(new B)@A.a(1, 2);
		}
	};
};
