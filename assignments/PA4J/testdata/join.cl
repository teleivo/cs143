class A {};
class B inherits A {};
class C inherits B {};
class D inherits B {};
class E inherits C {};
class F inherits C {};
class G inherits IO {};
class H inherits G {};
class J inherits G {};
class K inherits G {};
class X inherits H {};
class Main {
	main() : Int {
		{
			-- join is idempotent
			if (true) then
				new A
			else
				new A
			fi;
			-- join is commutative
			if (true) then
				new A
			else
				new B
			fi;
			if (true) then
				new B
			else
				new A
			fi;
			-- least common is Object
			if (true) then
				new X
			else
				new B
			fi;
			-- least common is G
			if (true) then
				new K
			else
				new X
			fi;
			let x : Int <- 10 in
			case x of
				a : Int => "a";
				b : Bool => "b";
				c : String => 1;
				d : X => "d";
				o : Object => "default";
			esac;
			let x : Int <- 10 in
			case x of
				a : Int => new D;
				b : Bool => new C;
				c : String => new E;
				d : X => new F;
			esac;
			1;
		}
	};
};
