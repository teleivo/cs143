class A inherits IO {
	a : Int <- 1;
};
class Main inherits A {
	b : Int <- 2;

	print_int(b: Int) : Object {
		{
			out_int(b);
		}
	};

	main() : Object {
		{
			print_int(7);
		}
	};
};
