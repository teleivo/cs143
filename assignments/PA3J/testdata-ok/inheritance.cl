class C {};
class B inherits C {};
class A inherits B {
};
-- adding it only so semantic analysis passes
class Main {
	main() : Bool {
		true
	};
};
