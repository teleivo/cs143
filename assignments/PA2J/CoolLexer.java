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
		65,
		68
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
		/* 0 */ YY_NO_ANCHOR,
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
		/* 16 */ YY_NOT_ACCEPT,
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
		/* 34 */ YY_NO_ANCHOR,
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
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
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
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,46:8,4,5,46,4,1,46:18,4,46,45,46:10,2,46:2,43:10,46,48,46,51,52,46:2,8,44" +
",6,20,27,11,44,14,10,44:2,28,44,13,19,31,44,15,26,16,24,17,22,44,30,44,46,4" +
"7,46:2,29,46,32,33,34,35,12,25,33,36,37,33:2,7,33,38,39,21,33,40,9,23,41,18" +
",42,33:3,49,46,50,46:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,158,
"0,1:2,2,1,3,4,5,1:4,6,7,6:2,8,1:2,9,6:2,1,6,9:3,6,9:2,6:2,9,6:3,1,10,9,1,11" +
",12,13,9,14,9:2,15,6,9:3,6:3,9,6,9:4,16,17,18,19,20,21,22,23,24,25,26,27,28" +
",9,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52," +
"53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,9,70,71,72,73,74,75,76,7" +
"7,6,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,1" +
"01,6,102,103,104,105,106,107,108")[0];

	private int yy_nxt[][] = unpackFromString(109,53,
"1,2,3,39,2,4,5,6,150,116,40,62,122,115,150:2,152,150,38,66,150,124,153,126," +
"150,41,154,155,121,39,150,156,116:2,128,116:2,63,130,67,116:2,132,37,150,7," +
"39:2,8,9,10,61,39,-1:55,11,-1:56,150,157,123,150:19,157,125,150:2,123,150:1" +
"0,125,150,-1:14,116:6,70,116:6,72,116:7,70,116,74,116:9,72,116:3,74,116,-1:" +
"9,7:2,16,7,42,7:39,17,7,64,7:5,-1:6,150:23,125,150:13,125,150,-1:14,150:8,1" +
"40,150:14,125,150:6,140,150:6,125,150,-1:9,16:4,-1,16:39,22,16,47,16:5,-1:6" +
",116:23,74,116:13,74,116,-1:51,37,-1:15,150:3,127,150,12,150,13,150:11,12,1" +
"27,150:2,125,150:8,13,150:4,125,150,-1:14,116:2,84,116,45,116:18,74,116:2,8" +
"4,116:4,45,116:5,74,116,-1:9,42:2,-1,42:41,-1,42:7,-1:6,116:8,104,116:14,74" +
",116:6,104,116:6,74,116,-1:9,16:44,22,16,47,16:5,-1:52,18,-1:6,150:4,14,150" +
":18,125,150:7,14,150:5,125,150,-1:14,116:3,86,116,43,116,44,116:11,43,86,11" +
"6:2,74,116:8,44,116:4,74,116,-1:9,7:2,16,7:41,17,7,64,7:5,1,-1,36:3,4,36:47" +
",-1:6,150:5,15,150:13,15,150:3,125,150:13,125,150,-1:14,116:5,46,116:13,46," +
"116:3,74,116:13,74,116,-1:8,1,-1,39:3,-1,39:47,-1:6,150:16,20,150:6,125,150" +
":12,20,125,150,-1:14,116:10,19,116:6,19,116:5,74,116:13,74,116,-1:14,150:10" +
",21,150:6,21,150:5,125,150:13,125,150,-1:14,116:13,94,116:9,74,116:9,94,116" +
":3,74,116,-1:14,150:10,48,150:6,48,150:5,125,150:13,125,150,-1:14,150:6,23," +
"150:14,23,150,125,150:13,125,150,-1:14,116:3,96,116:16,96,116:2,74,116:13,7" +
"4,116,-1:14,150:7,27,150:15,125,150:8,27,150:4,125,150,-1:14,116:2,97,116:2" +
"0,74,116:2,97,116:10,74,116,-1:14,150:6,53,150:14,53,150,125,150:13,125,150" +
",-1:14,116:6,99,116:14,99,116,74,116:13,74,116,-1:14,54,150:22,125,150:4,54" +
",150:8,125,150,-1:14,116:18,100,116:4,74,116:11,100,116,74,116,-1:14,150:15" +
",52,150:7,125,150,52,150:11,125,150,-1:14,116,101,116:20,101,74,116:13,74,1" +
"16,-1:14,150,56,150:20,56,125,150:13,125,150,-1:14,116:11,103:2,116:10,74,1" +
"16:13,74,116,-1:14,150:3,30,150:16,30,150:2,125,150:13,125,150,-1:14,116:16" +
",49,116:6,74,116:12,49,74,116,-1:14,150:6,31,150:14,31,150,125,150:13,125,1" +
"50,-1:14,116:10,50,116:6,50,116:5,74,116:13,74,116,-1:14,150:14,33,150:8,12" +
"5,150:5,33,150:7,125,150,-1:14,116:4,105,116:18,74,116:7,105,116:5,74,116,-" +
"1:14,150:3,34,150:16,34,150:2,125,150:13,125,150,-1:14,116:15,24,116:7,74,1" +
"16,24,116:11,74,116,-1:14,150:21,35,150,125,150:13,125,150,-1:14,116:6,25,1" +
"16:14,25,116,74,116:13,74,116,-1:14,26,116:22,74,116:4,26,116:8,74,116,-1:1" +
"4,116,28,116:20,28,74,116:13,74,116,-1:14,116:7,55,116:15,74,116:8,55,116:4" +
",74,116,-1:14,116:6,29,116:14,29,116,74,116:13,74,116,-1:14,116:3,106,116:1" +
"6,106,116:2,74,116:13,74,116,-1:14,116:6,51,116:14,51,116,74,116:13,74,116," +
"-1:14,116:13,108,116:9,74,116:9,108,116:3,74,116,-1:14,116:6,109,116:14,109" +
",116,74,116:13,74,116,-1:14,116,110,116:20,110,74,116:13,74,116,-1:14,116:6" +
",32,116:14,32,116,74,116:13,74,116,-1:14,116:3,57,116:16,57,116:2,74,116:13" +
",74,116,-1:14,116:4,111,116:18,74,116:7,111,116:5,74,116,-1:14,116:9,112,11" +
"6:13,74,116:10,112,116:2,74,116,-1:14,116:6,58,116:14,58,116,74,116:13,74,1" +
"16,-1:14,116:14,59,116:8,74,116:5,59,116:7,74,116,-1:14,116:4,113,116:18,74" +
",116:7,113,116:5,74,116,-1:14,116:10,114,116:6,114,116:5,74,116:13,74,116,-" +
"1:14,116:3,60,116:16,60,116:2,74,116:13,74,116,-1:14,150:6,69,150:6,71,150:" +
"7,69,150,125,150:9,71,150:3,125,150,-1:14,116:13,98,116:9,74,116:9,98,116:3" +
",74,116,-1:14,116:3,102,116:16,102,116:2,74,116:13,74,116,-1:14,116:2,120,1" +
"16:20,74,116:2,120,116:10,74,116,-1:14,116:3,107,116:16,107,116:2,74,116:13" +
",74,116,-1:14,150:6,73,150:6,136,150:7,73,150,125,150:9,136,150:3,125,150,-" +
"1:14,116,76,116,78,116:16,78,116,76,74,116:13,74,116,-1:14,150:3,75,150:16," +
"75,150:2,125,150:13,125,150,-1:14,116:13,117,116:9,74,116:9,117,116:3,74,11" +
"6,-1:14,116:8,80,82,116:13,74,116:6,80,116:3,82,116:2,74,116,-1:14,150:11,1" +
"39:2,150:10,125,150:13,125,150,-1:14,116,119,118,116:19,119,74,116:2,118,11" +
"6:10,74,116,-1:14,150:6,77,150:14,77,150,125,150:13,125,150,-1:14,116:6,88," +
"116:6,90,116:7,88,116,74,116:9,90,116:3,74,116,-1:14,150:4,141,150:18,125,1" +
"50:7,141,150:5,125,150,-1:14,116:8,92,116:14,74,116:6,92,116:6,74,116,-1:14" +
",150:22,142,125,150:13,125,150,-1:14,150:3,79,150:16,79,150:2,125,150:13,12" +
"5,150,-1:14,150:2,81,150:20,125,150:2,81,150:10,125,150,-1:14,150:13,83,150" +
":9,125,150:9,83,150:3,125,150,-1:14,150:13,85,150:9,125,150:9,85,150:3,125," +
"150,-1:14,150:3,87,150:16,87,150:2,125,150:13,125,150,-1:14,150:13,143,150:" +
"9,125,150:9,143,150:3,125,150,-1:14,150:6,144,150:14,144,150,125,150:13,125" +
",150,-1:14,150,89,150:20,89,125,150:13,125,150,-1:14,150:5,151,150:17,125,1" +
"50:13,125,150,-1:14,150:4,91,150:18,125,150:7,91,150:5,125,150,-1:14,150:9," +
"145,150:13,125,150:10,145,150:2,125,150,-1:14,150:4,147,150:18,125,150:7,14" +
"7,150:5,125,150,-1:14,150:10,148,150:12,125,150:13,125,150,-1:14,150:10,93," +
"150:6,93,150:5,125,150:13,125,150,-1:14,150:23,125,149,150:12,125,150,-1:14" +
",150:23,125,150,95,150:11,125,150,-1:14,150:23,146,150:13,125,150,-1:14,150" +
":8,129,150:14,125,150:6,129,150:6,125,150,-1:14,150:8,131,150:14,125,150:6," +
"131,150:6,125,150,-1:14,150:21,133,150,125,150:13,125,150,-1:14,150,134,150" +
",135,150:16,135,150,134,125,150:13,125,150,-1:14,150:13,137,150:9,125,150:9" +
",137,150:3,125,150,-1:14,150:2,138,150:20,125,150:2,138,150:10,125,150,-1:8");

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
					case 0:
						{
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{
}
					case -4:
						break;
					case 3:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -5:
						break;
					case 4:
						{
    // comments are discarded
    yybegin(YYINITIAL);
}
					case -6:
						break;
					case 5:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -7:
						break;
					case 6:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -8:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -9:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -10:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -11:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -12:
						break;
					case 11:
						{
    yybegin(LINE_COMMENT);
}
					case -13:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.IF);
}
					case -14:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.IN);
}
					case -15:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.FI);
}
					case -16:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.OF);
}
					case -17:
						break;
					case 17:
						{
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
					case -18:
						break;
					case 18:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.LET);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -35:
						break;
					case 35:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -36:
						break;
					case 36:
						{
    // comments are discarded
}
					case -37:
						break;
					case 37:
						{
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -38:
						break;
					case 38:
						{
}
					case -39:
						break;
					case 39:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -40:
						break;
					case 40:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -41:
						break;
					case 41:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.IF);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.IN);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.FI);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.OF);
}
					case -47:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.LET);
}
					case -48:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -49:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -50:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -51:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -52:
						break;
					case 53:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -53:
						break;
					case 54:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -54:
						break;
					case 55:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -55:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -58:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -60:
						break;
					case 61:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -61:
						break;
					case 62:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -62:
						break;
					case 63:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -64:
						break;
					case 66:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -65:
						break;
					case 67:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -66:
						break;
					case 69:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -67:
						break;
					case 70:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -68:
						break;
					case 71:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -69:
						break;
					case 72:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -70:
						break;
					case 73:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -71:
						break;
					case 74:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -72:
						break;
					case 75:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -73:
						break;
					case 76:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -74:
						break;
					case 77:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -75:
						break;
					case 78:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -76:
						break;
					case 79:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -77:
						break;
					case 80:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -78:
						break;
					case 81:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -79:
						break;
					case 82:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -80:
						break;
					case 83:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -81:
						break;
					case 84:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 85:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -83:
						break;
					case 86:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -84:
						break;
					case 87:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -85:
						break;
					case 88:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -86:
						break;
					case 89:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -87:
						break;
					case 90:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -88:
						break;
					case 91:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -89:
						break;
					case 92:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 93:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 94:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 95:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 96:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -94:
						break;
					case 97:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -95:
						break;
					case 98:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -96:
						break;
					case 99:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -97:
						break;
					case 100:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -98:
						break;
					case 101:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -99:
						break;
					case 102:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -100:
						break;
					case 103:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -101:
						break;
					case 104:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -102:
						break;
					case 105:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -103:
						break;
					case 106:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -104:
						break;
					case 107:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -105:
						break;
					case 108:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -106:
						break;
					case 109:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -107:
						break;
					case 110:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -108:
						break;
					case 111:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -109:
						break;
					case 112:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -110:
						break;
					case 113:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -111:
						break;
					case 114:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -112:
						break;
					case 115:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -113:
						break;
					case 116:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -114:
						break;
					case 117:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -115:
						break;
					case 118:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -116:
						break;
					case 119:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -117:
						break;
					case 120:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -118:
						break;
					case 121:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -119:
						break;
					case 122:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -120:
						break;
					case 123:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -121:
						break;
					case 124:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -122:
						break;
					case 125:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -123:
						break;
					case 126:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -124:
						break;
					case 127:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -125:
						break;
					case 128:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -126:
						break;
					case 129:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -127:
						break;
					case 130:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -128:
						break;
					case 131:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -129:
						break;
					case 132:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -130:
						break;
					case 133:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -131:
						break;
					case 134:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -132:
						break;
					case 135:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -133:
						break;
					case 136:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -134:
						break;
					case 137:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -135:
						break;
					case 138:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -136:
						break;
					case 139:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -137:
						break;
					case 140:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -138:
						break;
					case 141:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -139:
						break;
					case 142:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -140:
						break;
					case 143:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -141:
						break;
					case 144:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -142:
						break;
					case 145:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -143:
						break;
					case 146:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -144:
						break;
					case 147:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -145:
						break;
					case 148:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -146:
						break;
					case 149:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -147:
						break;
					case 150:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -148:
						break;
					case 151:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -149:
						break;
					case 152:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -150:
						break;
					case 153:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -151:
						break;
					case 154:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -152:
						break;
					case 155:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -153:
						break;
					case 156:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -154:
						break;
					case 157:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -155:
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
