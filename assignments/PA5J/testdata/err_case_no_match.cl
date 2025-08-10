class A {};
class B {};

class Main inherits IO {
	case_no_match(var : B) : SELF_TYPE {
		case var of
			a : A => out_string("chosen branch A\n");
		esac
	};
	main() : Object {
		{
			case_no_match(new B);
		}
	};
};
