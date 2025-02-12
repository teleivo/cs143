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
    /*
    // The Cool Reference Manual 7.1 Constants
     * String constants are sequences of characters enclosed in double quotes, such as "This is a
     * string." String constants may be at most 1024 characters long. There are other restrictions
     * on strings; see Section 10
     *
     * My interpretation is that the double-quotes are part of the string and thus count towards the
     * max length.
     */
    static int MAX_STR_CONST = 1024;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int openBlockComments;
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
	private final int STRING = 3;
	private final int BLOCK_COMMENT = 2;
	private final int LINE_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		85,
		89,
		92
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
		/* 31 */ YY_NOT_ACCEPT,
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
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
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
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NOT_ACCEPT,
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
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,49:8,4,5,49,4,1,49:18,4,49,48,49:5,6,8,7,53,55,2,54,51,46:10,56,57,60,61," +
"62,49,63,11,47,9,23,30,14,47,17,13,47:2,31,47,16,22,34,47,18,29,19,27,20,25" +
",47,33,47,49,50,49:2,32,49,35,36,37,38,15,28,36,39,40,36:2,10,36,41,42,24,3" +
"6,43,12,26,44,21,45,36:3,58,49,59,52,49,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,180,
"0,1:2,2,1:2,3,4,1,5,6,7,1:9,8,9,1:4,10,11,10:2,12,1:4,13,10:2,1:2,10,13:3,1" +
"0,13:2,10:2,13,10:3,1:4,14,13,15,16,17,13,18,13:2,19,10,13:3,10:3,13,10,13:" +
"4,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,13,35,36,37,38,39,40,41,42,4" +
"3,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,6" +
"8,69,70,71,72,73,74,75,13,76,77,78,79,80,81,82,83,10,84,85,86,87,88,89,90,9" +
"1,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,10,108,109,110,11" +
"1,112,113,114")[0];

	private int yy_nxt[][] = unpackFromString(115,64,
"1,2,3,4,2,5,6,7,8,9,10,172,138,60,82,144,137,172:2,174,172,59,87,172,146,17" +
"5,148,172,61,176,177,143,4,172,178,138:2,150,138:2,83,152,88,138:2,154,58,1" +
"72,11,4:2,12,13,14,15,16,17,18,19,20,21,22,4,23,-1:66,24,-1:68,25,-1:64,26," +
"-1:64,172,179,145,172:19,179,147,172:2,145,172:10,147,172,-1:25,138:6,91,13" +
"8:6,94,138:7,91,138,96,138:9,94,138:3,96,138,-1:17,11:2,31,11,62,11:42,32,1" +
"1,84,11:13,-1:2,33,-1:58,34,-1:64,35,-1:10,172:23,147,172:13,147,172,-1:25," +
"172:8,162,172:14,147,172:6,162,172:6,147,172,-1:17,31:4,-1,31:42,39,31,67,3" +
"1:13,-1:9,138:23,96,138:13,96,138,-1:62,58,-1:26,172:3,149,172,27,172,28,17" +
"2:11,27,149,172:2,147,172:8,28,172:4,147,172,-1:25,138:2,106,138,65,138:18," +
"96,138:2,106,138:4,65,138:5,96,138,-1:17,62:2,-1,62:44,40,62:15,-1:9,138:8," +
"126,138:14,96,138:6,126,138:6,96,138,-1:17,31:47,39,31,67,31:13,-1:7,56,-1:" +
"65,172:4,29,172:18,147,172:7,29,172:5,147,172,-1:25,138:3,108,138,63,138,64" +
",138:11,63,108,138:2,96,138:8,64,138:4,96,138,-1:17,11:2,31,11:44,32,11,84," +
"11:13,1,-1,54:3,5,54:58,-1:8,57,-1:64,172:5,30,172:13,30,172:3,147,172:13,1" +
"47,172,-1:25,138:5,66,138:13,66,138:3,96,138:13,96,138,-1:16,1,-1,55:4,81,8" +
"6,55:56,-1:9,172:16,37,172:6,147,172:12,37,147,172,-1:25,138:10,36,138:6,36" +
",138:5,96,138:13,96,138,-1:16,1,-1,4:3,-1,4:58,-1:9,172:10,38,172:6,38,172:" +
"5,147,172:13,147,172,-1:25,138:13,116,138:9,96,138:9,116,138:3,96,138,-1:25" +
",172:10,68,172:6,68,172:5,147,172:13,147,172,-1:25,172:6,41,172:14,41,172,1" +
"47,172:13,147,172,-1:25,138:3,118,138:16,118,138:2,96,138:13,96,138,-1:25,1" +
"72:7,45,172:15,147,172:8,45,172:4,147,172,-1:25,138:2,119,138:20,96,138:2,1" +
"19,138:10,96,138,-1:25,172:6,73,172:14,73,172,147,172:13,147,172,-1:25,138:" +
"6,121,138:14,121,138,96,138:13,96,138,-1:25,74,172:22,147,172:4,74,172:8,14" +
"7,172,-1:25,138:18,122,138:4,96,138:11,122,138,96,138,-1:25,172:15,72,172:7" +
",147,172,72,172:11,147,172,-1:25,138,123,138:20,123,96,138:13,96,138,-1:25," +
"172,76,172:20,76,147,172:13,147,172,-1:25,138:11,125:2,138:10,96,138:13,96," +
"138,-1:25,172:3,48,172:16,48,172:2,147,172:13,147,172,-1:25,138:16,69,138:6" +
",96,138:12,69,96,138,-1:25,172:6,49,172:14,49,172,147,172:13,147,172,-1:25," +
"138:10,70,138:6,70,138:5,96,138:13,96,138,-1:25,172:14,51,172:8,147,172:5,5" +
"1,172:7,147,172,-1:25,138:4,127,138:18,96,138:7,127,138:5,96,138,-1:25,172:" +
"3,52,172:16,52,172:2,147,172:13,147,172,-1:25,138:15,42,138:7,96,138,42,138" +
":11,96,138,-1:25,172:21,53,172,147,172:13,147,172,-1:25,138:6,43,138:14,43," +
"138,96,138:13,96,138,-1:25,44,138:22,96,138:4,44,138:8,96,138,-1:25,138,46," +
"138:20,46,96,138:13,96,138,-1:25,138:7,75,138:15,96,138:8,75,138:4,96,138,-" +
"1:25,138:6,47,138:14,47,138,96,138:13,96,138,-1:25,138:3,128,138:16,128,138" +
":2,96,138:13,96,138,-1:25,138:6,71,138:14,71,138,96,138:13,96,138,-1:25,138" +
":13,130,138:9,96,138:9,130,138:3,96,138,-1:25,138:6,131,138:14,131,138,96,1" +
"38:13,96,138,-1:25,138,132,138:20,132,96,138:13,96,138,-1:25,138:6,50,138:1" +
"4,50,138,96,138:13,96,138,-1:25,138:3,77,138:16,77,138:2,96,138:13,96,138,-" +
"1:25,138:4,133,138:18,96,138:7,133,138:5,96,138,-1:25,138:9,134,138:13,96,1" +
"38:10,134,138:2,96,138,-1:25,138:6,78,138:14,78,138,96,138:13,96,138,-1:25," +
"138:14,79,138:8,96,138:5,79,138:7,96,138,-1:25,138:4,135,138:18,96,138:7,13" +
"5,138:5,96,138,-1:25,138:10,136,138:6,136,138:5,96,138:13,96,138,-1:25,138:" +
"3,80,138:16,80,138:2,96,138:13,96,138,-1:25,172:6,90,172:6,93,172:7,90,172," +
"147,172:9,93,172:3,147,172,-1:25,138:13,120,138:9,96,138:9,120,138:3,96,138" +
",-1:25,138:3,124,138:16,124,138:2,96,138:13,96,138,-1:25,138:2,142,138:20,9" +
"6,138:2,142,138:10,96,138,-1:25,138:3,129,138:16,129,138:2,96,138:13,96,138" +
",-1:25,172:6,95,172:6,158,172:7,95,172,147,172:9,158,172:3,147,172,-1:25,13" +
"8,98,138,100,138:16,100,138,98,96,138:13,96,138,-1:25,172:3,97,172:16,97,17" +
"2:2,147,172:13,147,172,-1:25,138:13,139,138:9,96,138:9,139,138:3,96,138,-1:" +
"25,138:8,102,104,138:13,96,138:6,102,138:3,104,138:2,96,138,-1:25,172:11,16" +
"1:2,172:10,147,172:13,147,172,-1:25,138,141,140,138:19,141,96,138:2,140,138" +
":10,96,138,-1:25,172:6,99,172:14,99,172,147,172:13,147,172,-1:25,138:6,110," +
"138:6,112,138:7,110,138,96,138:9,112,138:3,96,138,-1:25,172:4,163,172:18,14" +
"7,172:7,163,172:5,147,172,-1:25,138:8,114,138:14,96,138:6,114,138:6,96,138," +
"-1:25,172:22,164,147,172:13,147,172,-1:25,172:3,101,172:16,101,172:2,147,17" +
"2:13,147,172,-1:25,172:2,103,172:20,147,172:2,103,172:10,147,172,-1:25,172:" +
"13,105,172:9,147,172:9,105,172:3,147,172,-1:25,172:13,107,172:9,147,172:9,1" +
"07,172:3,147,172,-1:25,172:3,109,172:16,109,172:2,147,172:13,147,172,-1:25," +
"172:13,165,172:9,147,172:9,165,172:3,147,172,-1:25,172:6,166,172:14,166,172" +
",147,172:13,147,172,-1:25,172,111,172:20,111,147,172:13,147,172,-1:25,172:5" +
",173,172:17,147,172:13,147,172,-1:25,172:4,113,172:18,147,172:7,113,172:5,1" +
"47,172,-1:25,172:9,167,172:13,147,172:10,167,172:2,147,172,-1:25,172:4,169," +
"172:18,147,172:7,169,172:5,147,172,-1:25,172:10,170,172:12,147,172:13,147,1" +
"72,-1:25,172:10,115,172:6,115,172:5,147,172:13,147,172,-1:25,172:23,147,171" +
",172:12,147,172,-1:25,172:23,147,172,117,172:11,147,172,-1:25,172:23,168,17" +
"2:13,147,172,-1:25,172:8,151,172:14,147,172:6,151,172:6,147,172,-1:25,172:8" +
",153,172:14,147,172:6,153,172:6,147,172,-1:25,172:21,155,172,147,172:13,147" +
",172,-1:25,172,156,172,157,172:16,157,172,156,147,172:13,147,172,-1:25,172:" +
"13,159,172:9,147,172:9,159,172:3,147,172,-1:25,172:2,160,172:20,147,172:2,1" +
"60,172:10,147,172,-1:16");

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
// TODO how can I also return the EOF symbol? I could go into an EOF state but would that then allow
// me to add an action later? as there are no chars to consume anymore
    switch(yy_lexical_state) {
    case YYINITIAL:
        /* nothing special to do in the initial state */
        break;
    case LINE_COMMENT:
    // The Cool Reference Manual 10.3 Comments
    // Any characters between two dashes “--” and the next newline (or EOF, if there is no next
    // newline) are treated as comments
        yybegin(YYINITIAL);
        break;
    case BLOCK_COMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
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
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -5:
						break;
					case 4:
						{
/* This rule should be the very last in your lexical specification and will match match
everything not matched by other lexical rules. */
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -6:
						break;
					case 5:
						{
    // comments are discarded
    yybegin(YYINITIAL);
}
					case -7:
						break;
					case 6:
						{
    return new Symbol(TokenConstants.LPAREN);
}
					case -8:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -9:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.RPAREN);
}
					case -10:
						break;
					case 9:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -11:
						break;
					case 10:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -12:
						break;
					case 11:
						{
    yybegin(STRING);
}
					case -13:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -14:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -15:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -16:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -17:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -18:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -19:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -20:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.LBRACE);
}
					case -21:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.RBRACE);
}
					case -22:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.LT);
}
					case -23:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -24:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.AT);
}
					case -25:
						break;
					case 24:
						{
    yybegin(LINE_COMMENT);
}
					case -26:
						break;
					case 25:
						{
    yybegin(BLOCK_COMMENT);
    openBlockComments++;
}
					case -27:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -28:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.IF);
}
					case -29:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.IN);
}
					case -30:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.FI);
}
					case -31:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.OF);
}
					case -32:
						break;
					case 32:
						{
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.LE);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.DARROW);
}
					case -36:
						break;
					case 36:
						{
// TODO do I also need to lex a let statement or is that the parsers job?
    return new Symbol(TokenConstants.LET);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -51:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -52:
						break;
					case 52:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -53:
						break;
					case 53:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -54:
						break;
					case 54:
						{
    // comments are discarded
}
					case -55:
						break;
					case 55:
						{
    // comments are discarded
}
					case -56:
						break;
					case 56:
						{
    openBlockComments++;
}
					case -57:
						break;
					case 57:
						{
    // comments are discarded
    openBlockComments--;
    if (openBlockComments == 0) {
        // all block comments have been properly closed
        yybegin(YYINITIAL);
    }
}
					case -58:
						break;
					case 58:
						{
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -59:
						break;
					case 59:
						{
}
					case -60:
						break;
					case 60:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -61:
						break;
					case 61:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -62:
						break;
					case 62:
						{
    yybegin(STRING);
}
					case -63:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.IF);
}
					case -64:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.IN);
}
					case -65:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.FI);
}
					case -66:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.OF);
}
					case -67:
						break;
					case 68:
						{
// TODO do I also need to lex a let statement or is that the parsers job?
    return new Symbol(TokenConstants.LET);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -78:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -79:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -80:
						break;
					case 81:
						{
    // comments are discarded
}
					case -81:
						break;
					case 82:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 83:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -83:
						break;
					case 84:
						{
    yybegin(STRING);
}
					case -84:
						break;
					case 86:
						{
    // comments are discarded
}
					case -85:
						break;
					case 87:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -86:
						break;
					case 88:
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
					case 93:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 94:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 95:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 96:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 97:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -94:
						break;
					case 98:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -95:
						break;
					case 99:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -96:
						break;
					case 100:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -97:
						break;
					case 101:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -98:
						break;
					case 102:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -99:
						break;
					case 103:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -100:
						break;
					case 104:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -101:
						break;
					case 105:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -102:
						break;
					case 106:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -103:
						break;
					case 107:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -104:
						break;
					case 108:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -105:
						break;
					case 109:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -106:
						break;
					case 110:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -107:
						break;
					case 111:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -108:
						break;
					case 112:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -109:
						break;
					case 113:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -110:
						break;
					case 114:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -111:
						break;
					case 115:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -112:
						break;
					case 116:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -113:
						break;
					case 117:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -114:
						break;
					case 118:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -115:
						break;
					case 119:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -116:
						break;
					case 120:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -117:
						break;
					case 121:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -118:
						break;
					case 122:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -119:
						break;
					case 123:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -120:
						break;
					case 124:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -121:
						break;
					case 125:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -122:
						break;
					case 126:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -123:
						break;
					case 127:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -124:
						break;
					case 128:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -125:
						break;
					case 129:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -126:
						break;
					case 130:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -127:
						break;
					case 131:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -128:
						break;
					case 132:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -129:
						break;
					case 133:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -130:
						break;
					case 134:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -131:
						break;
					case 135:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -132:
						break;
					case 136:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -133:
						break;
					case 137:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -134:
						break;
					case 138:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -135:
						break;
					case 139:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -136:
						break;
					case 140:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -137:
						break;
					case 141:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -138:
						break;
					case 142:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -139:
						break;
					case 143:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -140:
						break;
					case 144:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -141:
						break;
					case 145:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -142:
						break;
					case 146:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -143:
						break;
					case 147:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -144:
						break;
					case 148:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -145:
						break;
					case 149:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -146:
						break;
					case 150:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -147:
						break;
					case 151:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -148:
						break;
					case 152:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -149:
						break;
					case 153:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -150:
						break;
					case 154:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -151:
						break;
					case 155:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -152:
						break;
					case 156:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -153:
						break;
					case 157:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -154:
						break;
					case 158:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -155:
						break;
					case 159:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -156:
						break;
					case 160:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -157:
						break;
					case 161:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -158:
						break;
					case 162:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -159:
						break;
					case 163:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -160:
						break;
					case 164:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -161:
						break;
					case 165:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -162:
						break;
					case 166:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -163:
						break;
					case 167:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -164:
						break;
					case 168:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -165:
						break;
					case 169:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -166:
						break;
					case 170:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -167:
						break;
					case 171:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -168:
						break;
					case 172:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -169:
						break;
					case 173:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -170:
						break;
					case 174:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -171:
						break;
					case 175:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -172:
						break;
					case 176:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -173:
						break;
					case 177:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -174:
						break;
					case 178:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -175:
						break;
					case 179:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -176:
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
