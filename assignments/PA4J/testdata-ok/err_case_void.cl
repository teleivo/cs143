class A {};

class Main inherits IO {
	case_on_void(var : Object) : SELF_TYPE {
		case var of
			a : A => out_string("chosen branch A\n");
		esac
	};
	main() : Object {
		let x : A in case_on_void(x)
	};
};
