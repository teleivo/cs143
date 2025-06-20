-- tests branching
class Main inherits IO {
	-- TODO case
	ifElse(pred : Bool) : Bool {
		if pred then true else false fi
	};
	ifElseNested(pred : Bool) : Bool {
		if if pred then true else false fi then
			true
		else
			false
		fi
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
			print_bool("Main.ifElseNested", ifElse(true));
			print_bool("Main.ifElseNested", ifElse(false));
			print_bool("Main.ifElseNested", ifElseNested(true));
			print_bool("Main.ifElseNested", ifElseNested(false));
		}
	};
};
