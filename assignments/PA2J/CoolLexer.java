/*
 *  vim: set ft=java:
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class definition, all the extra
 *  variables/functions you want to use in the lexer actions should go here.  Don't remove or modify
 *  anything that was there initially.
 */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
        return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	    filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	    return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer class constructor, all the extra
 *  initialization you want to do should go here.  Don't remove or modify anything that was there
 *  initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int LINE_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		112,
		113
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NOT_ACCEPT,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NOT_ACCEPT,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NOT_ACCEPT,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,43:8,4,5,43,4,1,43:18,4,43,42,43:10,2,43:2,41:10,43,45,43,48,49,43:2,26,2" +
"7,28,29,30,11,27,31,32,27:2,33,27,34,35,36,27,37,38,16,39,17,40,27:3,43,44," +
"43:2,41,43,8,41,6,20,12,25,41,14,10,41:2,7,41,13,19,21,41,15,9,23,24,18,22," +
"41:3,46,43,47,43:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,144,
"0,1:2,2,1,3,4,1:5,5,6,1:11,6,1:8,7,1,8,9,6,10,1,6:8,1,6:5,11,12,13,14,15,16" +
",17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41" +
",42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66" +
",67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91" +
",92,93,94,6,95,96,97,98")[0];

	private int yy_nxt[][] = unpackFromString(99,50,
"1,2,3,35,2,4,56,60,35:2,63,5,66,69,35:2,36,139,2,72,35,75,78,81,35,84,139:2" +
",140,139,141,139,57,115,120,61,142,139:3,143,35,6,35:2,7,8,9,87,35,-1:52,10" +
",-1:53,139:4,13,139:21,13,139:9,-1:9,6:2,86,6,37,6:36,15,6,58,6:5,-1:14,118" +
",-1:16,118,-1:24,139:36,-1:16,121,-1:17,121,-1:29,139:8,122,139:16,122,139:" +
"10,-1:9,37:2,-1,37:38,-1,37:7,-1:6,139:8,133,139:16,133,139:10,-1:17,89,-1:" +
"28,89,-1:18,34,55,-1:17,55,-1:6,34,-1:22,139:3,127,139,38,139,39,139:11,38," +
"139:8,39,139:3,127,139:3,-1:9,6:2,86,6:38,15,6,58,6:5,-1:16,17,-1:6,17,-1:3" +
"8,59,-1:6,62,-1:10,59,-1:4,62,-1:20,139:5,41,139:13,41,139:16,-1:27,91,-1:1" +
"5,91,-1:23,65,-1,11,-1,12,-1:11,11,-1:8,12,-1:3,65,-1:17,139:10,42,139:6,42" +
",139:18,-1:25,93:2,-1:38,116,-1,114,-1:23,116,-1:4,114,-1:17,139:16,43,139:" +
"17,43,139,-1:30,18,-1:17,18,-1:21,68,-1:6,71,-1:10,68,-1:4,71,-1:20,139:10," +
"44,139:6,44,139:18,-1:24,19,-1:6,19,-1:37,14,-1:13,14,-1:30,139:7,25,139:20" +
",25,139:7,-1:18,119,-1:21,119,-1:36,117,-1:15,117,-1:20,139:6,45,139:17,45," +
"139:11,-1:20,99,-1:17,99,-1:33,74,-1:16,74,-1:24,139:6,47,139:17,47,139:11," +
"-1:32,100,-1:14,100,-1:24,77,80,-1:15,77,-1:5,80,-1:18,48,139:21,48,139:13," +
"-1:15,101,-1:25,101,-1:24,83,-1,40,-1:15,83,-1:5,40,-1:23,139:15,46,139:14," +
"46,139:5,-1:9,86:4,-1,86:36,20,86,102,86:5,-1:49,16,-1:6,139,50,139:25,50,1" +
"39:8,-1:20,21,-1:17,21,-1:25,139:3,51,139:28,51,139:3,-1:29,22,-1:14,22,-1:" +
"19,139:6,52,139:17,52,139:11,-1:27,104,-1:15,104,-1:20,139:14,53,139:8,53,1" +
"39:12,-1:20,23,-1:17,23,-1:25,139:3,54,139:28,54,139:3,-1:14,24,-1:21,24,-1" +
":28,26,-1:25,26,-1:29,49,-1:20,49,-1:27,27,-1:17,27,-1:28,107,-1:28,107,-1:" +
"12,86:41,20,86,102,86:5,-1:9,28,-1:28,28,-1:21,108,-1:21,108,-1:32,109,-1:2" +
"1,109,-1:24,29,-1:17,29,-1:31,30,-1:17,30,-1:39,31,-1:8,31,-1:30,110,-1:21," +
"110,-1:33,111,-1:6,111,-1:35,32,-1:28,32,-1:11,1,-1,33:3,4,33:44,1,-1,35:3," +
"-1,35:44,-1:8,97,-1:17,97,-1:29,139:6,64,139:6,128,139:10,64,139:4,128,139:" +
"6,-1:17,95,-1:28,95,-1:30,98,-1:15,98,-1:26,105,-1:17,105,-1:26,106,-1:25,1" +
"06,-1:22,139:6,67,139:6,70,139:10,67,139:4,70,139:6,-1:17,103,-1:28,103,-1:" +
"17,139:6,73,139:17,73,139:11,-1:14,139:2,131,139:17,131,139:15,-1:14,139:3," +
"76,139:28,76,139:3,-1:14,139:3,79,139:28,79,139:3,-1:14,139:2,82,139:17,82," +
"139:15,-1:14,139:11,132:2,139:23,-1:14,139:13,85,139:15,85,139:6,-1:14,139:" +
"13,88,139:15,88,139:6,-1:14,139:4,134,139:21,134,139:9,-1:14,139:3,90,139:2" +
"8,90,139:3,-1:14,139:13,135,139:15,135,139:6,-1:14,139:6,136,139:17,136,139" +
":11,-1:14,139,92,139:25,92,139:8,-1:14,139:4,94,139:21,94,139:9,-1:14,139:9" +
",137,139:21,137,139:4,-1:14,139:4,138,139:21,138,139:9,-1:14,139:10,96,139:" +
"6,96,139:18,-1:14,139,123,124,139:17,124,139:6,123,139:8,-1:14,139,125,139," +
"126,139:23,125,139:4,126,139:3,-1:14,139:13,129,139:15,129,139:6,-1:14,139:" +
"8,130,139:16,130,139:10,-1:8");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is executed when end-of-file is
 *  reached.  If you use multiple lexical states and want to do something special if an EOF is
 *  encountered in one of those states, place your code in the switch statement. Ultimately, you
 *  should return the EOF symbol, or your lexer won't work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
    /* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
}
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -4:
						break;
					case 4:
						{
    // comments are discarded
    yybegin(YYINITIAL);
}
					case -5:
						break;
					case 5:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -6:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.error, "Unterminated string constant");
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -10:
						break;
					case 10:
						{
    yybegin(LINE_COMMENT);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.IF);
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.IN);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.FI);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.OF);
}
					case -15:
						break;
					case 15:
						{
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.error, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
					case -16:
						break;
					case 16:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.LET);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.error, "String contains null character");
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -33:
						break;
					case 33:
						{
    // comments are discarded
}
					case -34:
						break;
					case 35:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -35:
						break;
					case 36:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -36:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.error, "Unterminated string constant");
}
					case -37:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.IF);
}
					case -38:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.IN);
}
					case -39:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.FI);
}
					case -40:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.OF);
}
					case -41:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.LET);
}
					case -42:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -43:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -44:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -45:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -46:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -47:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -48:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -49:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -50:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -51:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -52:
						break;
					case 53:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -53:
						break;
					case 54:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -54:
						break;
					case 56:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -55:
						break;
					case 57:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -56:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.error, "Unterminated string constant");
}
					case -57:
						break;
					case 60:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -58:
						break;
					case 61:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -59:
						break;
					case 63:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -60:
						break;
					case 64:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -61:
						break;
					case 66:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -62:
						break;
					case 67:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -63:
						break;
					case 69:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -64:
						break;
					case 70:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -65:
						break;
					case 72:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -66:
						break;
					case 73:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -67:
						break;
					case 75:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -68:
						break;
					case 76:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -69:
						break;
					case 78:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -70:
						break;
					case 79:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -71:
						break;
					case 81:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -72:
						break;
					case 82:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -73:
						break;
					case 84:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -74:
						break;
					case 85:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -75:
						break;
					case 87:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -76:
						break;
					case 88:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -77:
						break;
					case 90:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -78:
						break;
					case 92:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -79:
						break;
					case 94:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -80:
						break;
					case 96:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -81:
						break;
					case 115:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 120:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -83:
						break;
					case 122:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -84:
						break;
					case 123:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -85:
						break;
					case 124:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -86:
						break;
					case 125:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -87:
						break;
					case 126:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -88:
						break;
					case 127:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -89:
						break;
					case 128:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 129:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 130:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 131:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 132:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -94:
						break;
					case 133:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -95:
						break;
					case 134:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -96:
						break;
					case 135:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -97:
						break;
					case 136:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -98:
						break;
					case 137:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -99:
						break;
					case 138:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -100:
						break;
					case 139:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -101:
						break;
					case 140:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -102:
						break;
					case 141:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -103:
						break;
					case 142:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -104:
						break;
					case 143:
						{
// TODO does it also have a max length?
  AbstractSymbol id = AbstractTable.idtable.addString(yytext());
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -105:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
