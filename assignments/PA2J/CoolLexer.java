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
    private int commentCount;
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
	private final int BLOCK_COMMENT = 2;
	private final int LINE_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		70,
		75
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
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NOT_ACCEPT,
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
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
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
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
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
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,49:8,4,5,49,4,1,49:18,4,49,48,49:5,6,8,7,49:2,2,49:2,46:10,49,51,49,54,55" +
",49:2,11,47,9,23,30,14,47,17,13,47:2,31,47,16,22,34,47,18,29,19,27,20,25,47" +
",33,47,49,50,49:2,32,49,35,36,37,38,15,28,36,39,40,36:2,10,36,41,42,24,36,4" +
"3,12,26,44,21,45,36:3,52,49,53,49:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,165,
"0,1:2,2,1,3,4,5,1:5,6,7,6:2,8,1:2,9,6:2,1,6,9:3,6,9:2,6:2,9,6:3,1:4,10,9,1," +
"11,12,13,9,14,9:2,15,6,9:3,6:3,9,6,9:4,16,17,18,19,20,21,22,23,24,25,26,27," +
"28,29,30,31,9,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,5" +
"2,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,9,73,74,75,76" +
",77,78,79,80,6,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100" +
",101,102,103,104,6,105,106,107,108,109,110,111")[0];

	private int yy_nxt[][] = unpackFromString(112,56,
"1,2,3,43,2,4,66,43:2,5,6,157,123,44,67,129,122,157:2,159,157,42,73,157,131," +
"160,133,157,45,161,162,128,43,157,163,123:2,135,123:2,68,137,74,123:2,139,4" +
"1,157,7,43:2,8,9,10,72,43,-1:58,11,-1:62,157,164,130,157:19,164,132,157:2,1" +
"30,157:10,132,157,-1:17,123:6,77,123:6,79,123:7,77,123,81,123:9,79,123:3,81" +
",123,-1:9,7:2,17,7,46,7:42,18,7,69,7:5,-1:9,157:23,132,157:13,132,157,-1:17" +
",157:8,147,157:14,132,157:6,147,157:6,132,157,-1:9,17:4,-1,17:42,23,17,51,1" +
"7:5,-1:9,123:23,81,123:13,81,123,-1:54,41,-1:18,157:3,134,157,13,157,14,157" +
":11,13,134,157:2,132,157:8,14,157:4,132,157,-1:17,123:2,91,123,49,123:18,81" +
",123:2,91,123:4,49,123:5,81,123,-1:9,46:2,-1,46:44,-1,46:7,-1:9,123:8,111,1" +
"23:14,81,123:6,111,123:6,81,123,-1:9,17:47,23,17,51,17:5,-1:7,39,-1:55,12,-" +
"1:57,157:4,15,157:18,132,157:7,15,157:5,132,157,-1:17,123:3,93,123,47,123,4" +
"8,123:11,47,93,123:2,81,123:8,48,123:4,81,123,-1:9,7:2,17,7:44,18,7,69,7:5," +
"1,-1,37:3,4,37:50,-1:8,40,-1:102,19,-1:9,157:5,16,157:13,16,157:3,132,157:1" +
"3,132,157,-1:17,123:5,50,123:13,50,123:3,81,123:13,81,123,-1:8,1,-1,38:4,65" +
",71,38:48,-1:9,157:16,21,157:6,132,157:12,21,132,157,-1:17,123:10,20,123:6," +
"20,123:5,81,123:13,81,123,-1:17,157:10,22,157:6,22,157:5,132,157:13,132,157" +
",-1:17,123:13,101,123:9,81,123:9,101,123:3,81,123,-1:17,157:10,52,157:6,52," +
"157:5,132,157:13,132,157,-1:17,157:6,24,157:14,24,157,132,157:13,132,157,-1" +
":17,123:3,103,123:16,103,123:2,81,123:13,81,123,-1:17,157:7,28,157:15,132,1" +
"57:8,28,157:4,132,157,-1:17,123:2,104,123:20,81,123:2,104,123:10,81,123,-1:" +
"17,157:6,57,157:14,57,157,132,157:13,132,157,-1:17,123:6,106,123:14,106,123" +
",81,123:13,81,123,-1:17,58,157:22,132,157:4,58,157:8,132,157,-1:17,123:18,1" +
"07,123:4,81,123:11,107,123,81,123,-1:17,157:15,56,157:7,132,157,56,157:11,1" +
"32,157,-1:17,123,108,123:20,108,81,123:13,81,123,-1:17,157,60,157:20,60,132" +
",157:13,132,157,-1:17,123:11,110:2,123:10,81,123:13,81,123,-1:17,157:3,31,1" +
"57:16,31,157:2,132,157:13,132,157,-1:17,123:16,53,123:6,81,123:12,53,81,123" +
",-1:17,157:6,32,157:14,32,157,132,157:13,132,157,-1:17,123:10,54,123:6,54,1" +
"23:5,81,123:13,81,123,-1:17,157:14,34,157:8,132,157:5,34,157:7,132,157,-1:1" +
"7,123:4,112,123:18,81,123:7,112,123:5,81,123,-1:17,157:3,35,157:16,35,157:2" +
",132,157:13,132,157,-1:17,123:15,25,123:7,81,123,25,123:11,81,123,-1:17,157" +
":21,36,157,132,157:13,132,157,-1:17,123:6,26,123:14,26,123,81,123:13,81,123" +
",-1:17,27,123:22,81,123:4,27,123:8,81,123,-1:17,123,29,123:20,29,81,123:13," +
"81,123,-1:17,123:7,59,123:15,81,123:8,59,123:4,81,123,-1:17,123:6,30,123:14" +
",30,123,81,123:13,81,123,-1:17,123:3,113,123:16,113,123:2,81,123:13,81,123," +
"-1:17,123:6,55,123:14,55,123,81,123:13,81,123,-1:17,123:13,115,123:9,81,123" +
":9,115,123:3,81,123,-1:17,123:6,116,123:14,116,123,81,123:13,81,123,-1:17,1" +
"23,117,123:20,117,81,123:13,81,123,-1:17,123:6,33,123:14,33,123,81,123:13,8" +
"1,123,-1:17,123:3,61,123:16,61,123:2,81,123:13,81,123,-1:17,123:4,118,123:1" +
"8,81,123:7,118,123:5,81,123,-1:17,123:9,119,123:13,81,123:10,119,123:2,81,1" +
"23,-1:17,123:6,62,123:14,62,123,81,123:13,81,123,-1:17,123:14,63,123:8,81,1" +
"23:5,63,123:7,81,123,-1:17,123:4,120,123:18,81,123:7,120,123:5,81,123,-1:17" +
",123:10,121,123:6,121,123:5,81,123:13,81,123,-1:17,123:3,64,123:16,64,123:2" +
",81,123:13,81,123,-1:17,157:6,76,157:6,78,157:7,76,157,132,157:9,78,157:3,1" +
"32,157,-1:17,123:13,105,123:9,81,123:9,105,123:3,81,123,-1:17,123:3,109,123" +
":16,109,123:2,81,123:13,81,123,-1:17,123:2,127,123:20,81,123:2,127,123:10,8" +
"1,123,-1:17,123:3,114,123:16,114,123:2,81,123:13,81,123,-1:17,157:6,80,157:" +
"6,143,157:7,80,157,132,157:9,143,157:3,132,157,-1:17,123,83,123,85,123:16,8" +
"5,123,83,81,123:13,81,123,-1:17,157:3,82,157:16,82,157:2,132,157:13,132,157" +
",-1:17,123:13,124,123:9,81,123:9,124,123:3,81,123,-1:17,123:8,87,89,123:13," +
"81,123:6,87,123:3,89,123:2,81,123,-1:17,157:11,146:2,157:10,132,157:13,132," +
"157,-1:17,123,126,125,123:19,126,81,123:2,125,123:10,81,123,-1:17,157:6,84," +
"157:14,84,157,132,157:13,132,157,-1:17,123:6,95,123:6,97,123:7,95,123,81,12" +
"3:9,97,123:3,81,123,-1:17,157:4,148,157:18,132,157:7,148,157:5,132,157,-1:1" +
"7,123:8,99,123:14,81,123:6,99,123:6,81,123,-1:17,157:22,149,132,157:13,132," +
"157,-1:17,157:3,86,157:16,86,157:2,132,157:13,132,157,-1:17,157:2,88,157:20" +
",132,157:2,88,157:10,132,157,-1:17,157:13,90,157:9,132,157:9,90,157:3,132,1" +
"57,-1:17,157:13,92,157:9,132,157:9,92,157:3,132,157,-1:17,157:3,94,157:16,9" +
"4,157:2,132,157:13,132,157,-1:17,157:13,150,157:9,132,157:9,150,157:3,132,1" +
"57,-1:17,157:6,151,157:14,151,157,132,157:13,132,157,-1:17,157,96,157:20,96" +
",132,157:13,132,157,-1:17,157:5,158,157:17,132,157:13,132,157,-1:17,157:4,9" +
"8,157:18,132,157:7,98,157:5,132,157,-1:17,157:9,152,157:13,132,157:10,152,1" +
"57:2,132,157,-1:17,157:4,154,157:18,132,157:7,154,157:5,132,157,-1:17,157:1" +
"0,155,157:12,132,157:13,132,157,-1:17,157:10,100,157:6,100,157:5,132,157:13" +
",132,157,-1:17,157:23,132,156,157:12,132,157,-1:17,157:23,132,157,102,157:1" +
"1,132,157,-1:17,157:23,153,157:13,132,157,-1:17,157:8,136,157:14,132,157:6," +
"136,157:6,132,157,-1:17,157:8,138,157:14,132,157:6,138,157:6,132,157,-1:17," +
"157:21,140,157,132,157:13,132,157,-1:17,157,141,157,142,157:16,142,157,141," +
"132,157:13,132,157,-1:17,157:13,144,157:9,132,157:9,144,157:3,132,157,-1:17" +
",157:2,145,157:20,132,157:2,145,157:10,132,157,-1:8");

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
    case LINE_COMMENT:
        yybegin(YYINITIAL);
        break;
    case BLOCK_COMMENT:
        yybegin(YYINITIAL);
// TODO how can I also return the EOF symbol? I could go into an EOF state but would that then allow
// me to add an action later? as there are no chars to consume anymore
        return new Symbol(TokenConstants.ERROR,"EOF in comment");
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
    yybegin(BLOCK_COMMENT);
    commentCount++;
}
					case -14:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.IF);
}
					case -15:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.IN);
}
					case -16:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.FI);
}
					case -17:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.OF);
}
					case -18:
						break;
					case 18:
						{
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
					case -19:
						break;
					case 19:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.LET);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -36:
						break;
					case 36:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -37:
						break;
					case 37:
						{
    // comments are discarded
}
					case -38:
						break;
					case 38:
						{
    // comments are discarded
}
					case -39:
						break;
					case 39:
						{
    commentCount++;
}
					case -40:
						break;
					case 40:
						{
    // comments are discarded
    commentCount--;
    if (commentCount == 0) {
        yybegin(YYINITIAL);
    }
}
					case -41:
						break;
					case 41:
						{
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -42:
						break;
					case 42:
						{
}
					case -43:
						break;
					case 43:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -44:
						break;
					case 44:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -45:
						break;
					case 45:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.IF);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.IN);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.FI);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.OF);
}
					case -51:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.LET);
}
					case -52:
						break;
					case 53:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -53:
						break;
					case 54:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -54:
						break;
					case 55:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -55:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -58:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -60:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -64:
						break;
					case 65:
						{
    // comments are discarded
}
					case -65:
						break;
					case 66:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -66:
						break;
					case 67:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -67:
						break;
					case 68:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -69:
						break;
					case 71:
						{
    // comments are discarded
}
					case -70:
						break;
					case 72:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -71:
						break;
					case 73:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -72:
						break;
					case 74:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -73:
						break;
					case 76:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -74:
						break;
					case 77:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -75:
						break;
					case 78:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -76:
						break;
					case 79:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -77:
						break;
					case 80:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -78:
						break;
					case 81:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -79:
						break;
					case 82:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -80:
						break;
					case 83:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -81:
						break;
					case 84:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 85:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -83:
						break;
					case 86:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -84:
						break;
					case 87:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -85:
						break;
					case 88:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -86:
						break;
					case 89:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -87:
						break;
					case 90:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -88:
						break;
					case 91:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -89:
						break;
					case 92:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 93:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 94:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 95:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 96:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -119:
						break;
					case 122:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -120:
						break;
					case 123:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -125:
						break;
					case 128:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -126:
						break;
					case 129:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -127:
						break;
					case 130:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -128:
						break;
					case 131:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -129:
						break;
					case 132:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -130:
						break;
					case 133:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
					case 158:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -156:
						break;
					case 159:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -157:
						break;
					case 160:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -158:
						break;
					case 161:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -159:
						break;
					case 162:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -160:
						break;
					case 163:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -161:
						break;
					case 164:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -162:
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
