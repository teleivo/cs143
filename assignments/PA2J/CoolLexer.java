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
		84,
		88,
		91
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
		/* 30 */ YY_NOT_ACCEPT,
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
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
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
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
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
		/* 178 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,49:8,4,5,49,4,1,49:18,4,49,48,49:5,6,8,7,53,55,2,54,51,46:10,56,57,60,61," +
"62,49,63,11,47,9,23,30,14,47,17,13,47:2,31,47,16,22,34,47,18,29,19,27,20,25" +
",47,33,47,49,50,49:2,32,49,35,36,37,38,15,28,36,39,40,36:2,10,36,41,42,24,3" +
"6,43,12,26,44,21,45,36:3,58,49,59,52,49,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,179,
"0,1:2,2,1:2,3,1:2,4,5,6,1:9,7,8,1:3,9,10,9:2,11,1:4,12,9:2,1:2,9,12:3,9,12:" +
"2,9:2,12,9:3,1:4,13,12,14,15,16,12,17,12:2,18,9,12:3,9:3,12,9,12:4,19,20,21" +
",22,23,24,25,26,27,28,29,30,31,32,33,12,34,35,36,37,38,39,40,41,42,43,44,45" +
",46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70" +
",71,72,73,74,12,75,76,77,78,79,80,81,82,9,83,84,85,86,87,88,89,90,91,92,93," +
"94,95,96,97,98,99,100,101,102,103,104,105,106,9,107,108,109,110,111,112,113")[0];

	private int yy_nxt[][] = unpackFromString(114,64,
"1,2,3,4,2,5,6,7,8,9,10,171,137,59,81,143,136,171:2,173,171,58,86,171,145,17" +
"4,147,171,60,175,176,142,4,171,177,137:2,149,137:2,82,151,87,137:2,153,57,1" +
"71,11,4:2,12,13,14,15,16,17,18,19,20,21,22,4,23,-1:66,24,-1:68,25,-1:65,171" +
",178,144,171:19,178,146,171:2,144,171:10,146,171,-1:25,137:6,90,137:6,93,13" +
"7:7,90,137,95,137:9,93,137:3,95,137,-1:17,11:2,30,11,61,11:42,31,11,83,11:1" +
"3,-1:2,32,-1:58,33,-1:64,34,-1:10,171:23,146,171:13,146,171,-1:25,171:8,161" +
",171:14,146,171:6,161,171:6,146,171,-1:17,30:4,-1,30:42,38,30,66,30:13,-1:9" +
",137:23,95,137:13,95,137,-1:62,57,-1:26,171:3,148,171,26,171,27,171:11,26,1" +
"48,171:2,146,171:8,27,171:4,146,171,-1:25,137:2,105,137,64,137:18,95,137:2," +
"105,137:4,64,137:5,95,137,-1:17,61:2,-1,61:44,39,61:15,-1:9,137:8,125,137:1" +
"4,95,137:6,125,137:6,95,137,-1:17,30:47,38,30,66,30:13,-1:7,55,-1:65,171:4," +
"28,171:18,146,171:7,28,171:5,146,171,-1:25,137:3,107,137,62,137,63,137:11,6" +
"2,107,137:2,95,137:8,63,137:4,95,137,-1:17,11:2,30,11:44,31,11,83,11:13,1,-" +
"1,53:3,5,53:58,-1:8,56,-1:64,171:5,29,171:13,29,171:3,146,171:13,146,171,-1" +
":25,137:5,65,137:13,65,137:3,95,137:13,95,137,-1:16,1,-1,54:4,80,85,54:56,-" +
"1:9,171:16,36,171:6,146,171:12,36,146,171,-1:25,137:10,35,137:6,35,137:5,95" +
",137:13,95,137,-1:16,1,-1,4:3,-1,4:58,-1:9,171:10,37,171:6,37,171:5,146,171" +
":13,146,171,-1:25,137:13,115,137:9,95,137:9,115,137:3,95,137,-1:25,171:10,6" +
"7,171:6,67,171:5,146,171:13,146,171,-1:25,171:6,40,171:14,40,171,146,171:13" +
",146,171,-1:25,137:3,117,137:16,117,137:2,95,137:13,95,137,-1:25,171:7,44,1" +
"71:15,146,171:8,44,171:4,146,171,-1:25,137:2,118,137:20,95,137:2,118,137:10" +
",95,137,-1:25,171:6,72,171:14,72,171,146,171:13,146,171,-1:25,137:6,120,137" +
":14,120,137,95,137:13,95,137,-1:25,73,171:22,146,171:4,73,171:8,146,171,-1:" +
"25,137:18,121,137:4,95,137:11,121,137,95,137,-1:25,171:15,71,171:7,146,171," +
"71,171:11,146,171,-1:25,137,122,137:20,122,95,137:13,95,137,-1:25,171,75,17" +
"1:20,75,146,171:13,146,171,-1:25,137:11,124:2,137:10,95,137:13,95,137,-1:25" +
",171:3,47,171:16,47,171:2,146,171:13,146,171,-1:25,137:16,68,137:6,95,137:1" +
"2,68,95,137,-1:25,171:6,48,171:14,48,171,146,171:13,146,171,-1:25,137:10,69" +
",137:6,69,137:5,95,137:13,95,137,-1:25,171:14,50,171:8,146,171:5,50,171:7,1" +
"46,171,-1:25,137:4,126,137:18,95,137:7,126,137:5,95,137,-1:25,171:3,51,171:" +
"16,51,171:2,146,171:13,146,171,-1:25,137:15,41,137:7,95,137,41,137:11,95,13" +
"7,-1:25,171:21,52,171,146,171:13,146,171,-1:25,137:6,42,137:14,42,137,95,13" +
"7:13,95,137,-1:25,43,137:22,95,137:4,43,137:8,95,137,-1:25,137,45,137:20,45" +
",95,137:13,95,137,-1:25,137:7,74,137:15,95,137:8,74,137:4,95,137,-1:25,137:" +
"6,46,137:14,46,137,95,137:13,95,137,-1:25,137:3,127,137:16,127,137:2,95,137" +
":13,95,137,-1:25,137:6,70,137:14,70,137,95,137:13,95,137,-1:25,137:13,129,1" +
"37:9,95,137:9,129,137:3,95,137,-1:25,137:6,130,137:14,130,137,95,137:13,95," +
"137,-1:25,137,131,137:20,131,95,137:13,95,137,-1:25,137:6,49,137:14,49,137," +
"95,137:13,95,137,-1:25,137:3,76,137:16,76,137:2,95,137:13,95,137,-1:25,137:" +
"4,132,137:18,95,137:7,132,137:5,95,137,-1:25,137:9,133,137:13,95,137:10,133" +
",137:2,95,137,-1:25,137:6,77,137:14,77,137,95,137:13,95,137,-1:25,137:14,78" +
",137:8,95,137:5,78,137:7,95,137,-1:25,137:4,134,137:18,95,137:7,134,137:5,9" +
"5,137,-1:25,137:10,135,137:6,135,137:5,95,137:13,95,137,-1:25,137:3,79,137:" +
"16,79,137:2,95,137:13,95,137,-1:25,171:6,89,171:6,92,171:7,89,171,146,171:9" +
",92,171:3,146,171,-1:25,137:13,119,137:9,95,137:9,119,137:3,95,137,-1:25,13" +
"7:3,123,137:16,123,137:2,95,137:13,95,137,-1:25,137:2,141,137:20,95,137:2,1" +
"41,137:10,95,137,-1:25,137:3,128,137:16,128,137:2,95,137:13,95,137,-1:25,17" +
"1:6,94,171:6,157,171:7,94,171,146,171:9,157,171:3,146,171,-1:25,137,97,137," +
"99,137:16,99,137,97,95,137:13,95,137,-1:25,171:3,96,171:16,96,171:2,146,171" +
":13,146,171,-1:25,137:13,138,137:9,95,137:9,138,137:3,95,137,-1:25,137:8,10" +
"1,103,137:13,95,137:6,101,137:3,103,137:2,95,137,-1:25,171:11,160:2,171:10," +
"146,171:13,146,171,-1:25,137,140,139,137:19,140,95,137:2,139,137:10,95,137," +
"-1:25,171:6,98,171:14,98,171,146,171:13,146,171,-1:25,137:6,109,137:6,111,1" +
"37:7,109,137,95,137:9,111,137:3,95,137,-1:25,171:4,162,171:18,146,171:7,162" +
",171:5,146,171,-1:25,137:8,113,137:14,95,137:6,113,137:6,95,137,-1:25,171:2" +
"2,163,146,171:13,146,171,-1:25,171:3,100,171:16,100,171:2,146,171:13,146,17" +
"1,-1:25,171:2,102,171:20,146,171:2,102,171:10,146,171,-1:25,171:13,104,171:" +
"9,146,171:9,104,171:3,146,171,-1:25,171:13,106,171:9,146,171:9,106,171:3,14" +
"6,171,-1:25,171:3,108,171:16,108,171:2,146,171:13,146,171,-1:25,171:13,164," +
"171:9,146,171:9,164,171:3,146,171,-1:25,171:6,165,171:14,165,171,146,171:13" +
",146,171,-1:25,171,110,171:20,110,146,171:13,146,171,-1:25,171:5,172,171:17" +
",146,171:13,146,171,-1:25,171:4,112,171:18,146,171:7,112,171:5,146,171,-1:2" +
"5,171:9,166,171:13,146,171:10,166,171:2,146,171,-1:25,171:4,168,171:18,146," +
"171:7,168,171:5,146,171,-1:25,171:10,169,171:12,146,171:13,146,171,-1:25,17" +
"1:10,114,171:6,114,171:5,146,171:13,146,171,-1:25,171:23,146,170,171:12,146" +
",171,-1:25,171:23,146,171,116,171:11,146,171,-1:25,171:23,167,171:13,146,17" +
"1,-1:25,171:8,150,171:14,146,171:6,150,171:6,146,171,-1:25,171:8,152,171:14" +
",146,171:6,152,171:6,146,171,-1:25,171:21,154,171,146,171:13,146,171,-1:25," +
"171,155,171,156,171:16,156,171,155,146,171:13,146,171,-1:25,171:13,158,171:" +
"9,146,171:9,158,171:3,146,171,-1:25,171:2,159,171:20,146,171:2,159,171:10,1" +
"46,171,-1:16");

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
  System.err.println("LEXER BUG - UNMATCHED: " + yytext());
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
    return new Symbol(TokenConstants.IF);
}
					case -28:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.IN);
}
					case -29:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.FI);
}
					case -30:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.OF);
}
					case -31:
						break;
					case 31:
						{
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.LE);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.DARROW);
}
					case -35:
						break;
					case 35:
						{
// TODO do I also need to lex a let statement or is that the parsers job?
    return new Symbol(TokenConstants.LET);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -51:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -52:
						break;
					case 52:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -53:
						break;
					case 53:
						{
    // comments are discarded
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
    openBlockComments++;
}
					case -56:
						break;
					case 56:
						{
    // comments are discarded
    openBlockComments--;
    if (openBlockComments == 0) {
        // all block comments have been properly closed
        yybegin(YYINITIAL);
    }
}
					case -57:
						break;
					case 57:
						{
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -58:
						break;
					case 58:
						{
}
					case -59:
						break;
					case 59:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -60:
						break;
					case 60:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -61:
						break;
					case 61:
						{
    yybegin(STRING);
}
					case -62:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.IF);
}
					case -63:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.IN);
}
					case -64:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.FI);
}
					case -65:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.OF);
}
					case -66:
						break;
					case 67:
						{
// TODO do I also need to lex a let statement or is that the parsers job?
    return new Symbol(TokenConstants.LET);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -78:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -79:
						break;
					case 80:
						{
    // comments are discarded
}
					case -80:
						break;
					case 81:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -81:
						break;
					case 82:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 83:
						{
    yybegin(STRING);
}
					case -83:
						break;
					case 85:
						{
    // comments are discarded
}
					case -84:
						break;
					case 86:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -85:
						break;
					case 87:
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
					case 92:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -89:
						break;
					case 93:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 94:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 95:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 96:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 97:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -94:
						break;
					case 98:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -95:
						break;
					case 99:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -96:
						break;
					case 100:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -97:
						break;
					case 101:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -98:
						break;
					case 102:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -99:
						break;
					case 103:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -100:
						break;
					case 104:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -101:
						break;
					case 105:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -102:
						break;
					case 106:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -103:
						break;
					case 107:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -104:
						break;
					case 108:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -105:
						break;
					case 109:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -106:
						break;
					case 110:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -107:
						break;
					case 111:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -108:
						break;
					case 112:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -109:
						break;
					case 113:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -110:
						break;
					case 114:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -111:
						break;
					case 115:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -112:
						break;
					case 116:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -113:
						break;
					case 117:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -133:
						break;
					case 137:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -139:
						break;
					case 143:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -140:
						break;
					case 144:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -141:
						break;
					case 145:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -142:
						break;
					case 146:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -143:
						break;
					case 147:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -144:
						break;
					case 148:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -145:
						break;
					case 149:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -146:
						break;
					case 150:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -147:
						break;
					case 151:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -148:
						break;
					case 152:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -149:
						break;
					case 153:
						{
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -150:
						break;
					case 154:
						{
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
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
