class A {
	a: Int;
	b: Int;
	b: Int;
	a() : Int {
		1
	};
	a() : Int {
		1
	};
	c() : Int {
		1
	};
	d() : Bool {
		1
	};
	e(a: Int) : Bool {
		true
	};
	f(a: Int) : Bool {
		true
	};
	d: SELF_TYPE;
};
class B inherits A {
	a: Int;
	b: Int;
	-- this is ok
	b() : Int {
		1
	};
	-- this is ok
	c() : Int {
		1
	};
	-- this is not ok as return type differs from inherited method
	d() : Int {
		1
	};
	-- this is not ok as it must have the same number of formals
	e(a: Int, b: Int) : Bool {
		true
	};
	-- this is not ok as the formal type differs from the inherited method ones
	f(a: Bool) : Bool {
		true
	};
	d1: C;
};
class C {};
