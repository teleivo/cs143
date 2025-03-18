class A {
	iter(position : Int, num : Int) : Int {
                while position < num loop
                    {
			if 10 < num then
				position <- position + 2
			else
				position <- position + 1
			fi;
                    }
                pool
	};
};
