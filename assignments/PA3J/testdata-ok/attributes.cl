class A {
	a(): Int { 1 };
	b(v : Int) : Bool {
		not true
	};
	add(first: Int, second: Int) : Int {
		(* the last one will be returned *)
		{
			first - second;
			first * second;
			first / second;
			first + second;
		}
	};
};

class B {
	a: Int;
	b: String <- "very cool";
	b(): String { b };
	c: Bool <- false;
	d: Bool <- true;
	e: Int <- 10;
	comp(first: Int) : Int {
		~ first
	};
	f: Int <- { 10; 12; 14; };
};
class Main {
	main() : Int {
		1
	};
};
