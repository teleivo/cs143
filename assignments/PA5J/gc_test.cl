-- tests integration with Garbage collection
-- Garbage collection is not enabled in test.sh but can be tested with '-g' when compiling the file
-- individually
class List {
	isNil() : Bool { true };
	head() : String { { abort(); ""; } };
	tail() : List { { abort(); new List; } };
	cons(h : String) : List {
		new Cons.init(h, self)
	};
};

class Cons inherits List {
    car : String;
    cdr : List;

    isNil() : Bool { false };
    head() : String { car };
    tail() : List { cdr };

    init(h : String, t : List) : Cons {
        {
            car <- h;
            cdr <- t;
            self;
        }
    };
};

class Main inherits IO {
	-- creates a list with n items
    createList(n : Int) : List {
        if n = 0 then
            new List
        else
            createList(n-1).cons("item".copy())
        fi
    };

    createLists(count : Int) : Object {
        if count = 0 then
            0
        else
            {
                let n : Int <- 1000 in let list : List <- createList(n) in {
                    out_string("Created list with ");
                    out_int(n);
                    out_string(" items\n");
				};
                createLists(count - 1);
            }
        fi
    };

    main() : Object {
        {
            out_string("GC test start...\n");
            createLists(50);
            out_string("GC test end\n");
        }
    };
};
