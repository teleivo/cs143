-- error due to cycle: Z -> Z
class Z inherits Z {};
-- error due to cycle: A -> B -> C -> A
class A inherits B {};
class B inherits C {};
class C inherits A {};
class M inherits IO {};
class X {};
class Y inherits Object {};
-- error due to cycle: E -> F -> H -> J -> K -> G -> E
class F inherits E {};
class N inherits IO {};
class H inherits F {};
class J inherits H {};
class K inherits J {};
class G inherits K {};
class E inherits G {};
