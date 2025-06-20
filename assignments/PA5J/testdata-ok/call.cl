class A inherits IO {
	number() : Int {
		1
	};
};
class Main inherits A {
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

	-- test child method overrides parent method
	-- test block returns last expression
	number() : Int {
		{
			2;
			3;
		}
	};
	-- test String literal is correctly placed in .data and can be retrieved
	string() : String {
		"some string in Main"
	};
	-- create add method and test to show args
	-- add prefix method into main?
	-- add test.sh to compare ref and my output
	main() : Object {
		{
			-- TODO implement let with init to extract prefix
			print_int("Main.main", number());
			print_string("Main.main", string());
		}
	};
};
