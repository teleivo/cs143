class Main inherits IO {
	fib(n : Int) : Int {
		if n < 2 then
			n
		else
			fib(n-1) + fib(n-2)
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
			print_int("fib(0)", fib(0));
			print_int("fib(1)", fib(1));
			print_int("fib(2)", fib(2));
			print_int("fib(3)", fib(3));
			print_int("fib(4)", fib(4));
			print_int("fib(20)", fib(20));
			print_int("fib(127)", fib(127));
		}
	};
};
