-- tests several expressions
-- * plus
-- * if
-- * =
class Main inherits IO {
	add(a : Int, b : Int) : Int {
		a + b
	};
	sum_to(n : Int) : Int {
		if n = 1 then
			1
		else
			add(n, sum_to(n-1))
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
			print_int("Main.main", add(5, 12));
			print_int("Main.main", sum_to(1));
			print_int("Main.main", sum_to(56));
		}
	};
};
