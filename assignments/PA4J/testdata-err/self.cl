class A {
	self: Int;
	a(b: Int) : SELF_TYPE {
		{
			self <- 2;
			let self : SELF_TYPE <- 1 in 1;
			-- ok
			case self of
				a : Int => "int";
				o : Object => "default";
			esac;
			-- not ok
			case var of
				self : Int => "int";
				a : Foo => "int";
				o : Object => "default";
			esac;
			self;
		}
	};
	b(a: SELF_TYPE) : Int {
		1
	};
};
class Main {
	main() : Int {
		1
	};
};
