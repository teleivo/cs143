class A {
	copy() : SELF_TYPE {
		self
	};
};
class B inherits A {
	create() : SELF_TYPE {
		(new SELF_TYPE).copy()
	};
	createAgain() : SELF_TYPE {
		new SELF_TYPE
	};
};
class Main {
	main() : B {
		{
			(new B);
			-- let x : B <- (new B).copy() in 1;
			-- (new B).create();
			-- (new A).copy();
			(new B).createAgain();
		}
	};
};
