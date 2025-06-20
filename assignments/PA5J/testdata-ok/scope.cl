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

--	print_int() : Object {
--		{
--			out_int(b);
--		}
--	};
	-- TODO implement args using the environment
	print_int(b: Int) : Object {
		{
			out_int(b);
		}
	};

	main() : Object {
		{
	--		print_int();
			print_int(7);
		}
	};
};
