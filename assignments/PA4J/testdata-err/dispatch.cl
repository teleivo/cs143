class C {
	init(x : Int, y : Bool) : Int {
		1
	};
	a() : Bool {
		4
	};
	b() : A {
		new B
	};
};
class A {};
class B inherits A {};
Class Main {
	main(): Int {
		{
			(new C).what("foo", 1, 2);
			(new C).init("foo", 1, 2);
			(new C).init("foo", 1);
		}
	};
};
