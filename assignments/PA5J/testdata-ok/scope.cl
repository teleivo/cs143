class A inherits IO {
	a : Int <- 1;
	a() : Int{1};
	b() : Int{1};
	c() : Int{1};
};
class Main inherits A {
	b : Int <- 2;
	a() : Int{2};
	d() : Int{2};
	let_without_initializer() : Int {
		let x : Int in x
	};
	let_with_initializer() : Int {
		let x : Int <- 44 in x
	};
	let_hides_parameter(x : Int) : Int {
		let x : Int <- 44 in x
	};
	let_hides_attribute() : Int {
		let b : Int <- 44 in b
	};
	let_initializer_access_attribute() : Int {
		let b : Int <- b + 1 in b
	};
	let_nested() : Int {
		let a : Int <- 1 in
			let a : Int <- a+1 in
				let a : Int <- a+1 in a
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
			print_int("let_without_initializer", let_without_initializer());
			print_int("let_with_initializer", let_with_initializer());
			print_int("let_hides_parameter", let_hides_parameter(10));
			print_int("let_hides_attribute", let_hides_attribute());
			print_int("let_initializer_access_attribute", let_initializer_access_attribute());
			print_int("let_nested", let_nested());
		}
	};
};
