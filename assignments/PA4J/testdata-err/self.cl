class A {
	self: Int;
-- TODO if I would addId(self, SELF_TYPE);
-- conform(cls, ST, ST) would return true in the cases I need it to
-- and I would get this error
-- but what is the scope? are there places where it would not be defined?
-- ./testdata-err/self.cl:5: Type Int of assigned expression does not conform to declared type SELF_TYPE of identifier self.
-- TODO illegal: bind self in a case, or as a formal parameter
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
};
class Main {
	main() : Int {
		1
	};
};
