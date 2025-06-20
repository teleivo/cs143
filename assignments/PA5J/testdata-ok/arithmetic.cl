-- tests arithmetic operators
class Main inherits IO {
	-- TODO test equality on void
	-- TODO test equality on instance once I have new implemented
	eq_int(a : Int, b : Int) : Bool {
		a = b
	};
	eq_bool(a : Bool, b : Bool) : Bool {
		a = b
	};
	eq_string(a : String, b : String) : Bool {
		a = b
	};
	less_than(a : Int, b : Int) : Bool {
		a < b
	};
	less_than_equal(a : Int, b : Int) : Bool {
		a <= b
	};
	not_op(a : Bool) : Bool {
		not a
	};

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
	main() : Object {
		{
			print_bool("Main.eq_int", eq_int(5, 5));
			print_bool("Main.eq_int", eq_int(6, 5));
			print_bool("Main.eq_bool", eq_bool(true,true));
			print_bool("Main.eq_bool", eq_bool(false,false));
			print_bool("Main.eq_string", eq_string("yes","yes"));
			print_bool("Main.eq_string", eq_string("yes","no"));

			print_bool("Main.less_than", less_than(5, 6));
			print_bool("Main.less_than", less_than(6, 6));
			print_bool("Main.less_than", less_than(7, 6));
			print_bool("Main.less_than_equal", less_than_equal(5, 6));
			print_bool("Main.less_than_equal", less_than_equal(6, 6));
			print_bool("Main.less_than_equal", less_than_equal(7, 6));

			print_bool("Main.not_op", not_op(true));
			print_bool("Main.not_op", not_op(false));
		}
	};
};
