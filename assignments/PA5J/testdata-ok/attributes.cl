-- tests mostly initialization of attributes
class A {
  a : Int <- b; -- should be 0 as bs' initializer will not have run yet
  b : Int <- 10;
  c : Int <- b; -- should be 10 as bs' initializer will have run
  a() : Int { a };
  b() : Int { b };
  c() : Int { c };
};
class B {
  a : A;
  b : A <- new A;
  a() : A { a };
  b() : A { b };
};
class C inherits A {
	e: Int <- b; -- should be 10 as As' initializers will have run
	e() : Int { e };
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
			-- print_int("Main A.a()", (new A).a());
			-- print_int("Main A.b()", (new A).b());
			-- print_int("Main A.c()", (new A).c());
			-- print_int("Main C.e()", (new C).e());
			print_bool("Main B.a() isvoid", isvoid ((new B).a()));
			print_bool("Main B.b() isvoid", isvoid ((new B).b()));
		}
	};
};
