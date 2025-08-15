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
Class Main inherits IO {
	a() : Int { 1 };

	-- test helpers
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
	print_bool(prefix : String, arg : Bool) : Object {
		{
			print_prefix(prefix);
			print_bool_val(arg);
			print_line();
		}
	};
	print_bool_val(arg : Bool) : Object {
		if arg then
			out_string("true")
		else
			out_string("false")
		fi
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
	main(): Object {
		{
			print_int("Main.a()", a());
			print_int("new A.a()", (new A).a(1, 2));
			print_int("new B.a()", (new B).a(1, 2));
			print_int("new B@A.a()", (new B)@A.a(1, 2));
		}
	};
};
