class A {
	a : SELF_TYPE <- self;
	copy() : SELF_TYPE {
		self
	};
	fun(a : A) : SELF_TYPE {
		let x : SELF_TYPE <- self in x
	};
};
class B inherits A {
	create() : SELF_TYPE {
		(new SELF_TYPE).copy()
	};
	createAgain() : SELF_TYPE {
		new SELF_TYPE
	};
	realFun() : A {
		(new A).fun(new SELF_TYPE)
	};
};
class Main {
	main() : A {
		{
			let x : B <- (new B).copy() in 1;
			(new B).create();
			(new A).copy();
			(new B).createAgain();
			(new A).fun(new B);
			(new B);
		}
	};
};
