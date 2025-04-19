class C {
	a: Int <- ~ true;
	b: Bool <- true < 1;
	c: Bool <- "foo" <= 1;
	d: Bool <- not 3;
	e: Int <- false;
	f: Int <- "foo" + true;
	g: Int <- "foo" - true;
	h: Int <- "foo" * 2;
	j: Int <- "foo" / false;
};

Class Main {
	main(): Int {
		1
	};
};
