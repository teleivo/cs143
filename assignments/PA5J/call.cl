class A inherits IO {
	number() : Int {
		1
	};
};
class Main inherits A {
	-- test child method overrides parent method
	number() : Int {
		2
	};
	-- test String literal is correctly placed in .data and can be retrieved
	string() : String {
		"B"
	};
	-- implement block then call and print string()
	-- create add method and test to show args
	-- add prefix method into main?
	-- add test.sh to compare ref and my output
	main() : SELF_TYPE {
		out_int(number())
	};
};
