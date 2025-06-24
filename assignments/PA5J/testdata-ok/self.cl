-- tests mostly self and SELF_TYPE
class A {
	a : Int <- 10;
	a() : Int { a };
	b : A <- self;
	b() : A { b };
	new_self() : SELF_TYPE { new SELF_TYPE };
	get_self() : SELF_TYPE { self };
};
class B inherits A {
	c : Int <- 20;
	a() : Int { c };
};
class Main inherits IO {
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
			-- these work
			-- print_int("Main A.new_self().a()", (new A).new_self().a());
			-- print_int("Main A.get_self().a()", (new A).get_self().a());
			-- print_int("Main A.b().a()", (new A).b().a());
			-- TODO this fails
			print_int("Main B.new_self().a()", (new B).new_self().a());
			-- these two work
			-- print_int("Main B.get_self().a()", (new B).get_self().a());
			-- print_int("Main B.b().a()", (new B).b().a());
		}
	};
};
