-- tests case expression
class Book inherits IO {};
class Article inherits Book {};
class BookList inherits IO {};
class A {};
class Nil inherits BookList {};
class B inherits IO {};
class Cons inherits BookList {};
class C inherits B {};

class Main inherits IO {
   class_with_object_branch(var : Object) : SELF_TYPE {
      case var of
		 a : A => out_string("branch A\n");
		 b : IO => out_string("branch IO\n");
		 c : B => out_string("branch B\n");
		 d : Object => out_string("branch Object\n");
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
			class_with_object_branch(new Book);
			class_with_object_branch(new Article);
			class_with_object_branch(new BookList);
			class_with_object_branch(new A);
			class_with_object_branch(new Nil);
			class_with_object_branch(new B);
			class_with_object_branch(new Cons);
			class_with_object_branch(new C);
			class_with_object_branch(10);
			class_with_object_branch(true);
		}
	};
};
