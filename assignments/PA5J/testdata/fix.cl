class Main inherits IO {
	foo(a : Int) : Object {
		{
			out_int(a);
			case a of
				a : Int => {
					out_int(a);
					a <- a-1;
					out_int(a);
				};
			esac;
			out_int(a);
		}
	};
    main() : Object {
		let a : Int <- 1 in {
			out_int(a);
			case a of
				a : Int => {
					out_int(a);
					a <- a-1;
					out_int(a);
					case a of
						a : Int => {
							out_int(a);
							a <- a-1;
							foo(a);
							out_int(a);
						};
					esac;
					out_int(a);
				};
			esac;
			out_int(a);
		}
    };
};
