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
	case_scopes(a : Int) : Object {
		{
			print_int("before let/cases", a);
			let a : Int <- a-1 in
				{
					print_int("in let/before first case", a);
					case a-1 of
						a : Int => {
							print_int("a in branch Int/before second case", a);
							case a-1 of
								a : Int => {
									print_int("a in branch Int/in second case", a);
									a <- a-1; -- assign
									print_int("a in branch Int/in second case/after assign", a);
								};
							esac;
							print_int("after second case", a);
						};
						a : Bool => print_bool("a in branch bool", a);
					esac;
					print_int("a in let/after cases", a);
				};
			print_int("a arg after let/cases", a);
		}
	};
	case_with_object_branch(var : Object) : SELF_TYPE {
		case var of
			a : A => out_string("branch A\n");
			b : IO => out_string("branch IO\n");
			c : B => out_string("branch B\n");
			d : Object => out_string("branch Object\n");
		esac
	};
	case_without_object_branch(var : Object) : SELF_TYPE {
		case var of
			a : A => out_string("branch A\n");
			b : IO => out_string("branch IO\n");
			c : B => out_string("branch B\n");
		esac
	};
	case_value(var : Object) : Int {
		case var of
			a : A => 1;
			b : Nil => 2;
			c : Cons => 3;
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
			case_with_object_branch(new Book);
			case_with_object_branch(new Article);
			case_with_object_branch(new BookList);
			case_with_object_branch(new A);
			case_with_object_branch(new Nil);
			case_with_object_branch(new B);
			case_with_object_branch(new Cons);
			case_with_object_branch(new C);
			case_with_object_branch(10);
			case_with_object_branch(true);

			case_without_object_branch(new Book);
			case_without_object_branch(new Article);
			case_without_object_branch(new BookList);
			case_without_object_branch(new A);
			case_without_object_branch(new Nil);
			case_without_object_branch(new B);
			case_without_object_branch(new Cons);
			case_without_object_branch(new C);

			case_scopes(10);

			print_int("case_value", case_value(new Nil));
		}
	};
};
