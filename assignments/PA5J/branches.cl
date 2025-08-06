Class Book inherits IO {};
Class Article inherits Book {};
Class BookList inherits IO {};
class A {};
Class Nil inherits BookList {};
class B inherits IO {};
Class Cons inherits BookList {};
class C inherits B {};

Class Main inherits IO {
	class_with_catchall(var : Object) : SELF_TYPE {
		case var of
			a : IO => out_string("Class type is now IO\n");
			-- b : BookList => out_string("Class type is now BookList\n");
			-- o : Object => out_string("Oooops\n");
		esac
	};
    main() : Object {
			class_with_catchall(new A)
    };
};
