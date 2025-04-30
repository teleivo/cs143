class A inherits B {};
class B {
	-- TODO move this into a success test
	a : SELF_TYPE <- self;
	-- SELF_TYPE is not allowed as parameter
	b(x: SELF_TYPE) : SELF_TYPE {
		new B
	};
};
	-- TODO move this into a success test
-- TODO test: Actual arguments can be SELF_TYPE
-- TODO test: test SELF_TYPE in the dispatch expression
-- Which class is used to find the declaration of f?: it is safe to use the class where the dispatch appears
class Main {
	main() : Bool {
		{
			-- TODO move this into a success test
			--let x : SELF_TYPE <- self;
			(new B).b(new A);
			true;
		}
	};
};
