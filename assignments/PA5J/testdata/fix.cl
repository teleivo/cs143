class Main inherits IO {
    main() : Object {
		let a : Int <- 1 in let b : Int <- 2 in let c : Int <- 3 in let d : Int <- 4 in let e : Int <- 5 in let f : Int <- 6 in let g : Int <- 7 in let h : Int <- 8 in let i : Int <- 9 in let j : Int <- 10 in { -- TODO bug
			out_int(a);
			out_string("\n");
			out_int(b);
			out_string("\n");
			out_int(c);
			out_string("\n");
			out_int(d);
			out_string("\n");
			out_int(e);
			out_string("\n");
			out_int(f);
			out_string("\n");
			out_int(g);
			out_string("\n");
			out_int(h);
			out_string("\n");
			out_int(i);
			out_string("\n");
			out_int(j);
			out_string("\n");
		}
		-- let a : Int <- 1 in {
		-- 	out_int(a);
		-- 	case a of
		-- 		b : Int => {
		-- 			out_int(a);
		-- 			out_int(b);
		-- 			b <- b-1;
		-- 			out_int(a);
		-- 			out_int(b);
		-- 		};
		-- 	esac;
		-- 	out_int(a);
		-- }
    };
};
