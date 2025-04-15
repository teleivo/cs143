-- error due to inheriting from prohibited base classes
class A inherits String {};
class B inherits Int {};
class C inherits Bool {};
-- that is allowed
class D inherits IO {};
-- error due to redefining classes
class X {};
class Y {};
class Y {};
class X {};
-- error due to inheriting from class that does not exist
class M inherits N {};
