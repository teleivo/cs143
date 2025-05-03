class A {
a: Bool <- not true;

add() : Int {
  1+2+3
};
mult() : Int {
  1*2*3
};
mix() : Int {
  1+2*3+4
};
};
-- adding it only so semantic analysis passes
class Main {
	main() : Bool {
		true
	};
};
