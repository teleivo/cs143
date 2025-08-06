class Book inherits IO {};
class Article inherits Book {};
class BookList inherits IO {};
class A {};
class Nil inherits BookList {};
class B inherits IO {};
class Cons inherits BookList {};
class C inherits B {};

-- tests case
class Main inherits IO {
   class_with_catchall(var : Object) : SELF_TYPE {
      case var of
		 a : IO => out_string("Class type is now A\n");
		 -- b : B => out_string("Class type is now B\n");
		 -- o : Object => out_string("Oooops\n");
      esac
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
			-- class_with_catchall(new A);
			class_with_catchall(new B);
			-- class_with_catchall(10);
		}
	};
};
