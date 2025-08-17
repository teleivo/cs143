(*
 * Copyright (c) 2000 The Regents of the University of California.
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose, without fee, and without written agreement is hereby granted,
 * provided that the above copyright notice and the following two
 * paragraphs appear in all copies of this software.
 *
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *   The class A2I provides integer-to-string and string-to-integer
 * conversion routines.  To use these routines, either inherit them
 * in the class where needed, have a dummy variable bound to
 * something of type A2I, or simpl write (new A2I).method(argument).
 *)


(*
   c2i   Converts a 1-character string to an integer.  Aborts
         if the string is not "0" through "9"
*)
class A2I {

     c2i(char : String) : Int {
	if char = "0" then 0 else
	if char = "1" then 1 else
	if char = "2" then 2 else
        if char = "3" then 3 else
        if char = "4" then 4 else
        if char = "5" then 5 else
        if char = "6" then 6 else
        if char = "7" then 7 else
        if char = "8" then 8 else
        if char = "9" then 9 else
        { abort(); 0; }  -- the 0 is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   i2c is the inverse of c2i.
*)
     i2c(i : Int) : String {
	if i = 0 then "0" else
	if i = 1 then "1" else
	if i = 2 then "2" else
	if i = 3 then "3" else
	if i = 4 then "4" else
	if i = 5 then "5" else
	if i = 6 then "6" else
	if i = 7 then "7" else
	if i = 8 then "8" else
	if i = 9 then "9" else
	{ abort(); ""; }  -- the "" is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   a2i converts an ASCII string into an integer.  The empty string
is converted to 0.  Signed and unsigned strings are handled.  The
method aborts if the string does not represent an integer.  Very
long strings of digits produce strange answers because of arithmetic
overflow.

*)
     a2i(s : String) : Int {
        if s.length() = 0 then 0 else
	if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
        if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
           a2i_aux(s)
        fi fi fi
     };

(*
  a2i_aux converts the usigned portion of the string.  As a programming
example, this method is written iteratively.
*)
     a2i_aux(s : String) : Int {
	(let int : Int <- 0 in
           {
               (let j : Int <- s.length() in
	          (let i : Int <- 0 in
		    while i < j loop
			{
			    int <- int * 10 + c2i(s.substr(i,1));
			    i <- i + 1;
			}
		    pool
		  )
	       );
              int;
	    }
        )
     };

(*
    i2a converts an integer to a string.  Positive and negative
numbers are handled correctly.
*)
    i2a(i : Int) : String {
	if i = 0 then "0" else
        if 0 < i then i2a_aux(i) else
          "-".concat(i2a_aux(i * ~1))
        fi fi
    };

(*
    i2a_aux is an example using recursion.
*)
    i2a_aux(i : Int) : String {
        if i = 0 then "" else
	    (let next : Int <- i / 10 in
		i2a_aux(next).concat(i2c(i - next * 10))
	    )
        fi
    };

};
-- adding it only so semantic analysis passes
class Main inherits IO {
	main() : Object {
		(let converter : A2I <- new A2I in
			{
				out_string("Testing A2I conversion methods:\n");
				out_string("c2i(\"5\") = ").out_int(converter.c2i("5")).out_string("\n");
				out_string("i2c(7) = \"").out_string(converter.i2c(7)).out_string("\"\n");
				out_string("a2i(\"-42\") = ").out_int(converter.a2i("-42")).out_string("\n");
				out_string("i2a(123) = \"").out_string(converter.i2a(123)).out_string("\"\n");
				out_string("a2i(\"0\") = ").out_int(converter.a2i("0")).out_string("\"\n");
				out_string("i2a(-999) = \"").out_string(converter.i2a(~999)).out_string("\"\n");
				
				-- Additional round-trip conversion tests
				out_string("\nRound-trip conversion tests:\n");
				(let i1 : Int <- converter.a2i("123") in
				(let s1 : String <- converter.i2a(i1) in
					{
						out_string("\"123\" -> "); out_int(i1); 
						out_string(" -> \""); out_string(s1); out_string("\"\n");
					}
				));
				
				(let i2 : Int <- converter.a2i("-456") in
				(let s2 : String <- converter.i2a(i2) in
					{
						out_string("\"-456\" -> "); out_int(i2); 
						out_string(" -> \""); out_string(s2); out_string("\"\n");
					}
				));
				
				out_string("A2I conversion tests completed.\n");
			}
		)
	};
};
