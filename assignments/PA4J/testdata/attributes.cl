class C {
	a: Int <- ~ true;
	b: Bool <- true < 1;
	c: Bool <- "foo" <= 1;
	d: Bool <- not 3;
	e: Int <- false;
	f: Int <- "foo" + true;
	g: Int <- "foo" - true;
	h: Int <- "foo" * 2;
	i: Int <- "foo" / false;
	j: Bool <- 1 = true;
	k: Bool <- "foo" = true;
	l: Bool <- 1 = "foo";
	m: Bool <- 1 = 2;
	n: Bool <- false = true;
	o: Bool <- "foo" = "foo";
	p: String;
	q: Int <- {
		z <- 3;
		z;
		b <- 2;
		1;
	};
};

Class Main {
	main(): Int {
		1
	};
};
