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
			-- TODO implement let with init to extract prefix
			print_string("Main.main", a());
			print_string("Main.main", b());
			print_string("Main.main", c());
			print_string("Main.main", d());
			print_int("Main.main", numberA());
			print_int("Main.main", numberB(11));
		}
	};
};
