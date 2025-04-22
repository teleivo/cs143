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
		if 1 then
			"what?"
		else
			true
		fi;
		while "me?" loop
			"yes"
		pool;
		let x : Int in true;
		let x : Int <- "wrong" in true;
		case var of
			a : Int => "must have distinct types";
			b : Int => "must have distinct types";
			c : Bool => "must have distinct types";
			d : Bool => "must have distinct types";
			d : Bool => "must have distinct types";
			d : Str => "class is undefined but var name can be reused";
			d : Str => "class is undefined but var name can be reused";
			o : Object => "default";
		esac;
		1;
	};
};

Class Main {
	main(): Int {
		1
	};
};
