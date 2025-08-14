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
		let x : Int in let y : Int in let a : Int in let b : Int in let c : Int in let d : Int in
		let f : Int in let t : Int in let u : Int in let i : Int in let o : Int in let z : Int in d
	};
	let_without_initializer_bool() : Bool {
		let x : Bool in x
	};
	let_without_initializer_string() : String {
		let x : String in x
	};
	let_without_initializer_object() : A {
		let x : A in x
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
	let_nested_hide(a : Int) : Object {
		{
			print_int("before lets", a);
			let a : Int <- a-1 in
				{
					print_int("inside first let", a);
					let a : Int <- a-1 in
						{
							print_int("inside second let", a);
							let a : Int <- a-1 in
								{
									print_int("inside third let", a);
								};
							print_int("after third let", a);
						};
					print_int("after second let", a);
				};
			print_int("after lets", a);
		}
	};
	let_nested(a : Int) : Object {
		{
			print_int("before lets a", a);
			let a : Int <- a-1 in
				{
					print_int("inside first let a", a);
					let b : Int <- 3 in
						{
							print_int("inside second let a", a);
							print_int("inside second let b", b);
						};
					print_int("after second let a", a);
				};
			print_int("after lets a", a);
		}
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
			print_bool("let_without_initializer_bool", let_without_initializer_bool());
			print_string("let_without_initializer_string", let_without_initializer_string());
			print_bool("let_without_initializer_object", isvoid let_without_initializer_object());
			print_int("let_with_initializer", let_with_initializer());
			print_int("let_hides_parameter", let_hides_parameter(10));
			print_int("let_hides_attribute", let_hides_attribute());
			print_int("let_initializer_access_attribute", let_initializer_access_attribute());
			let_nested_hide(10);
			-- TODO move fix.cl test over here when done
			-- let_nested(10);
		}
	};
};
