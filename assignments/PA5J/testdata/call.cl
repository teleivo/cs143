-- tests mainly dispatch
-- * the behavior of overriding methods
-- * that args are evaluated and can be accessed
-- * that attributes can be accessed
-- * that parameters take precedence over attributes
-- * String literals are correctly placed in .data and can be retrieved
class A inherits IO {
	aNumber : Int <- 1;
	a() : String {
		"a from A"
	};
	b() : String {
		"b from A"
	};
	c() : String {
		"c from A"
	};
	numberA() : Int {
		aNumber
	};
	test_assign(a: Int, b: Int) : Int {
		{
			b <- a;
			b;
		}
	};
};
class B inherits A {
	a() : String {
		"a from B"
	};
	b() : String {
		self@A.b()
	};
};
class Main inherits A {
	bNumber : Int <- 10;
	-- test child method overrides parent method
	-- test block returns last expression
	a() : String {
		{
			"do not return this one";
			"a from Main";
		}
	};
	-- test String literal is correctly placed in .data and can be retrieved
	d() : String {
		"d from Main"
	};
	-- test parameter overrides attribute
	numberB(bNumber : Int) : Int {
		bNumber
	};

	print_int(prefix : String, arg : Int) : Object {
		{
			print_prefix(prefix);
			out_int(arg);
			print_line();
		}
	};
	print_string(prefix : String, arg : String) : Object {
		{
			print_prefix(prefix);
			out_string(arg);
			print_line();
		}
	};
	print_prefix(prefix : String) : Object {
		{
			out_string(prefix);
			out_string(": ");
		}
	};
	print_line() : Object {
		out_string("\n")
	};
	main() : Object {
		{
			print_string("Main.a()", a());
			print_string("Main.b()", b());
			print_string("Main.c()", c());
			print_string("Main.d()", d());
			print_string("Main String.concat", "foo".concat("bar"));
			print_string("Main String.substr", "foobar".substr(0, 3));
			print_string("new B.a()", new B.a());
			print_string("new B@A.a()", new B@A.a());
			print_string("new B.b()", new B.b());
			print_int("String.length()", "foo".length());
			print_int("Main.numberA()", numberA());
			print_int("Main.numberB()", numberB(11));
			print_int("Main.test_assign()", test_assign(10, 20));
		}
	};
};
