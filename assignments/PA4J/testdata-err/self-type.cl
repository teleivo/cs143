class A inherits B {};
class B {
	b(x: SELF_TYPE) : SELF_TYPE {
		self
	};
};
-- TODO test: Actual arguments can be SELF_TYPE
-- TODO test: test SELF_TYPE in the dispatch expression
-- Which class is used to find the declaration of f?: it is safe to use the class where the dispatch appears
class Main {
	main() : Bool {
		{
			(new B).b(new A);
			true;
		}
	};
};
