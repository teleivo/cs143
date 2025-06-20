-- tests arithmetic comparison operators
class Main inherits IO {
	eqInt(a : Int, b : Int) : Bool {
		a = b
	};
	eqBool(a : Bool, b : Bool) : Bool {
		a = b
	};
	eqString(a : String, b : String) : Bool {
		a = b
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
			print_bool("Main.main", eqInt(5, 5));
			print_bool("Main.main", eqInt(6, 5));
			print_bool("Main.main", eqBool(true,true));
			print_bool("Main.main", eqBool(false,false));
			print_bool("Main.main", eqString("yes","yes"));
			print_bool("Main.main", eqString("yes","no"));
		}
	};
};
