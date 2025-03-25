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
    /*
     * The Cool Reference Manual 7.1 Constants
     * String constants are sequences of characters enclosed in double quotes, such as "This is a
     * string." String constants may be at most 1024 characters long. There are other restrictions
     * on strings; see Section 10
     *
     * Added 2 to 1024 to compare the string including quotes which are then stripped before
     * inserting it into the string table.
     */
    static int MAX_STR_CONST = 1026;
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
    // Lex string according to PA1 4.3 Strings and the Cool Reference Manual 10.2 Strings
    private Symbol lexString(String stringText) {
        curr_lineno = yyline+1;
        if (stringText.length() > MAX_STR_CONST) {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        }
        StringBuilder out = new StringBuilder(stringText.length());
        boolean isClosed = false;
        int i = 1; // skip opening quote
        while (i < stringText.length()) {
            char c1 = stringText.charAt(i);
            if (c1 == '\n') {
                curr_lineno = yyline+2;
                return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
            }
            if (c1 == '\0') {
                curr_lineno = yyline+1;
                return new Symbol(TokenConstants.ERROR, "String contains null character.");
            }
            // Cool Reference Manual 10.2 Strings
            // > Within a string, a sequence ‘\c’ denotes the character ‘c’, with the exception of the following:
            if (c1 == '\\' && i + 1 < stringText.length()) {
                char c2 = stringText.charAt(i + 1);
                if (c2 == 'b') {
                    out.append('\b');
                } else if (c2 == 't') {
                    out.append('\t');
                } else if (c2 == 'n') {
                    out.append('\n');
                } else if (c2 == '\n') {
                    curr_lineno++;
                    out.append('\n');
                } else if (c2 == 'f') {
                    out.append('\f');
                } else {
                    out.append(c2);
                }
                i++; // consume one extra char for '\'
            } else if (c1 == '"' && i == stringText.length() - 1) {
                isClosed = true;
                // skip closing quote
            } else {
                out.append(c1);
            }
            i++;
        }
        if (!isClosed) {
            // According to PA1 4.3: error should be reported at
            // 1. the beginning of the next line if an unescaped newline occurs at any point in the string;
            curr_lineno = curr_lineno+1;
            return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
        }
        AbstractSymbol str = AbstractTable.stringtable.addString(out.toString());
        return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(), str.getString().length(), str.index));
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
	}

	private boolean yy_eof_done = false;
	private final int BLOCK_COMMENT = 2;
	private final int LINE_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		94,
		97
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
		/* 30 */ YY_END,
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
		/* 64 */ YY_END,
		/* 65 */ YY_NOT_ACCEPT,
		/* 66 */ YY_NO_ANCHOR,
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
		/* 83 */ YY_END,
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
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
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
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3:9,4,5,3,4,1,3:18,4,3,48,3:5,6,8,7,53,55,2,54,51,46:10,56,57,60,61,62,3,63" +
",11,47,9,22,29,14,47,17,13,47:2,30,47,16,21,33,47,18,28,19,26,20,24,47,32,4" +
"7,3,49,3:2,31,3,34,35,36,37,15,27,35,38,39,35:2,10,35,40,41,23,35,42,12,25," +
"43,44,45,35:3,58,3,59,52,3,0,50")[0];

	private int yy_rmap[] = unpackFromString(1,181,
"0,1:2,2,1:2,3,4,1,5,6,1:9,7,8,1:4,9,10,9:2,11:2,1:4,12,9:3,12:3,9,12:2,9:2," +
"12,9:3,1:4,13,11,14,15,12,16,12:2,17,18,19,9,12:3,9:3,12,9,12:4,20,21,22,1," +
"17,23,24,25,19,26,27,28,29,30,31,32,12,33,34,35,36,37,38,39,40,41,42,43,44," +
"45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69," +
"70,71,72,73,74,12,75,76,77,78,79,80,81,82,9,83,84,85,86,87,88,89,90,91,92,9" +
"3,94,95,96,97,98,99,100,101,102,103,104,105,106,9,107,108,109,110,111,112,1" +
"13")[0];

	private int yy_nxt[][] = unpackFromString(114,64,
"1,2,3,4,2,5,6,7,8,9,10,173,139,58,81,145,138,173:2,175,173,86,173,147,176,1" +
"49,173,59,177,178,144,4,173,179,139:2,151,139:2,82,153,87,139:3,155,56,173," +
"57,4,1,11,12,13,14,15,16,17,18,19,20,21,4,22,-1:66,23,-1:68,24,-1:64,25,-1:" +
"64,173,180,146,173:18,180,148,173:2,146,173:11,148,173,-1:25,139:6,90,139:5" +
",93,139:7,90,139,96,139:9,93,139:4,96,139,-1:18,33,-1:58,34,-1:64,35,-1:10," +
"173:22,148,173:14,148,173,-1:25,173:8,163,173:13,148,173:6,163,173:7,148,17" +
"3,-1:17,30,31:3,64,31:42,32,65,83,31:13,-1:9,139:22,96,139:14,96,139,-1:62," +
"56,-1:26,173:3,150,173,26,173,27,173:10,26,150,173:2,148,173:8,27,173:5,148" +
",173,-1:25,139:2,107,139,62,139:17,96,139:2,107,139:4,62,139:6,96,139,-1:25" +
",139:8,127,139:13,96,139:6,127,139:7,96,139,-1:17,64,84:3,64,84:42,-1,84,83" +
",84:13,-1,30,31:3,30,31:42,66,65,83,31:13,-1,88:4,-1,88:42,32,91,-1,88:13,-" +
"1:7,54,-1:65,173:4,28,173:17,148,173:7,28,173:6,148,173,-1:25,139:3,109,139" +
",60,139,61,139:10,60,109,139:2,96,139:8,61,139:5,96,139,-1:24,55,-1:64,173:" +
"5,29,173:12,29,173:3,148,173:14,148,173,-1:25,139:5,63,139:12,63,139:3,96,1" +
"39:14,96,139,-1:25,173:15,37,173:6,148,173:13,37,148,173,-1:25,139:10,36,13" +
"9:5,36,139:5,96,139:14,96,139,-1:17,88:47,66,91,-1,88:13,-1:9,173:10,38,173" +
":5,38,173:5,148,173:14,148,173,-1:25,139:12,117,139:9,96,139:9,117,139:4,96" +
",139,-1:16,1,-1,52:3,5,52:44,1,52:13,-1:9,173:10,67,173:5,67,173:5,148,173:" +
"14,148,173,-1:16,1,-1,53:4,80,85,53:42,1,53:13,-1:9,173:6,39,173:13,39,173," +
"148,173:14,148,173,-1:25,139:3,119,139:15,119,139:2,96,139:14,96,139,-1:25," +
"173:7,43,173:14,148,173:8,43,173:5,148,173,-1:25,139:2,120,139:19,96,139:2," +
"120,139:11,96,139,-1:25,173:6,72,173:13,72,173,148,173:14,148,173,-1:25,139" +
":6,122,139:13,122,139,96,139:14,96,139,-1:25,73,173:21,148,173:4,73,173:9,1" +
"48,173,-1:25,139:17,123,139:4,96,139:11,123,139:2,96,139,-1:25,173:14,71,17" +
"3:7,148,173,71,173:12,148,173,-1:25,139,124,139:19,124,96,139:14,96,139,-1:" +
"25,173,75,173:19,75,148,173:14,148,173,-1:25,139:11,126,139:10,96,139:12,12" +
"6,139,96,139,-1:25,173:3,46,173:15,46,173:2,148,173:14,148,173,-1:25,139:15" +
",68,139:6,96,139:13,68,96,139,-1:25,173:6,47,173:13,47,173,148,173:14,148,1" +
"73,-1:25,139:10,69,139:5,69,139:5,96,139:14,96,139,-1:25,173:13,49,173:8,14" +
"8,173:5,49,173:8,148,173,-1:25,139:4,128,139:17,96,139:7,128,139:6,96,139,-" +
"1:25,173:3,50,173:15,50,173:2,148,173:14,148,173,-1:25,139:14,40,139:7,96,1" +
"39,40,139:12,96,139,-1:25,173:20,51,173,148,173:14,148,173,-1:25,139:6,41,1" +
"39:13,41,139,96,139:14,96,139,-1:25,42,139:21,96,139:4,42,139:9,96,139,-1:2" +
"5,139,44,139:19,44,96,139:14,96,139,-1:25,139:7,74,139:14,96,139:8,74,139:5" +
",96,139,-1:25,139:6,45,139:13,45,139,96,139:14,96,139,-1:25,139:3,129,139:1" +
"5,129,139:2,96,139:14,96,139,-1:25,139:6,70,139:13,70,139,96,139:14,96,139," +
"-1:25,139:12,131,139:9,96,139:9,131,139:4,96,139,-1:25,139:6,132,139:13,132" +
",139,96,139:14,96,139,-1:25,139,133,139:19,133,96,139:14,96,139,-1:25,139:6" +
",48,139:13,48,139,96,139:14,96,139,-1:25,139:3,76,139:15,76,139:2,96,139:14" +
",96,139,-1:25,139:4,134,139:17,96,139:7,134,139:6,96,139,-1:25,139:9,135,13" +
"9:12,96,139:10,135,139:3,96,139,-1:25,139:6,77,139:13,77,139,96,139:14,96,1" +
"39,-1:25,139:13,78,139:8,96,139:5,78,139:8,96,139,-1:25,139:4,136,139:17,96" +
",139:7,136,139:6,96,139,-1:25,139:10,137,139:5,137,139:5,96,139:14,96,139,-" +
"1:25,139:3,79,139:15,79,139:2,96,139:14,96,139,-1:25,173:6,89,173:5,92,173:" +
"7,89,173,148,173:9,92,173:4,148,173,-1:25,139:12,121,139:9,96,139:9,121,139" +
":4,96,139,-1:25,139:3,125,139:15,125,139:2,96,139:14,96,139,-1:25,139:2,143" +
",139:19,96,139:2,143,139:11,96,139,-1:25,139:3,130,139:15,130,139:2,96,139:" +
"14,96,139,-1:25,173:6,95,173:5,159,173:7,95,173,148,173:9,159,173:4,148,173" +
",-1:25,139,99,139,101,139:15,101,139,99,96,139:14,96,139,-1:25,173:3,98,173" +
":15,98,173:2,148,173:14,148,173,-1:25,139:12,140,139:9,96,139:9,140,139:4,9" +
"6,139,-1:25,139:8,103,105,139:12,96,139:6,103,139:3,105,139:3,96,139,-1:25," +
"173:11,162,173:10,148,173:12,162,173,148,173,-1:25,139,142,141,139:18,142,9" +
"6,139:2,141,139:11,96,139,-1:25,173:6,100,173:13,100,173,148,173:14,148,173" +
",-1:25,139:6,111,139:5,113,139:7,111,139,96,139:9,113,139:4,96,139,-1:25,17" +
"3:4,164,173:17,148,173:7,164,173:6,148,173,-1:25,139:8,115,139:13,96,139:6," +
"115,139:7,96,139,-1:25,173:21,165,148,173:14,148,173,-1:25,173:3,102,173:15" +
",102,173:2,148,173:14,148,173,-1:25,173:2,104,173:19,148,173:2,104,173:11,1" +
"48,173,-1:25,173:12,106,173:9,148,173:9,106,173:4,148,173,-1:25,173:12,108," +
"173:9,148,173:9,108,173:4,148,173,-1:25,173:3,110,173:15,110,173:2,148,173:" +
"14,148,173,-1:25,173:12,166,173:9,148,173:9,166,173:4,148,173,-1:25,173:6,1" +
"67,173:13,167,173,148,173:14,148,173,-1:25,173,112,173:19,112,148,173:14,14" +
"8,173,-1:25,173:5,174,173:16,148,173:14,148,173,-1:25,173:4,114,173:17,148," +
"173:7,114,173:6,148,173,-1:25,173:9,168,173:12,148,173:10,168,173:3,148,173" +
",-1:25,173:4,170,173:17,148,173:7,170,173:6,148,173,-1:25,173:10,171,173:11" +
",148,173:14,148,173,-1:25,173:10,116,173:5,116,173:5,148,173:14,148,173,-1:" +
"25,173:22,148,172,173:13,148,173,-1:25,173:22,148,173,118,173:12,148,173,-1" +
":25,173:22,169,173:14,148,173,-1:25,173:8,152,173:13,148,173:6,152,173:7,14" +
"8,173,-1:25,173:8,154,173:13,148,173:6,154,173:7,148,173,-1:25,173:20,156,1" +
"73,148,173:14,148,173,-1:25,173,157,173,158,173:15,158,173,157,148,173:14,1" +
"48,173,-1:25,173:12,160,173:9,148,173:9,160,173:4,148,173,-1:25,173:2,161,1" +
"73:19,148,173:2,161,173:11,148,173,-1:16");

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
        // The Cool Reference Manual 10.3 Comments
        // Any characters between two dashes “--” and the next newline (or EOF, if there is no next
        // newline) are treated as comments
        yybegin(YYINITIAL);
        break;
    case BLOCK_COMMENT:
        curr_lineno = yyline+1;
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
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
    curr_lineno = yyline+1;
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
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.MINUS);
}
					case -5:
						break;
					case 4:
						{
/* This rule should be the very last in your lexical specification and will match match
everything not matched by other lexical rules. */
    curr_lineno = yyline+1;
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
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LPAREN);
}
					case -8:
						break;
					case 7:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.MULT);
}
					case -9:
						break;
					case 8:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.RPAREN);
}
					case -10:
						break;
					case 9:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -11:
						break;
					case 10:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -12:
						break;
					case 11:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DIV);
}
					case -13:
						break;
					case 12:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NEG);
}
					case -14:
						break;
					case 13:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.PLUS);
}
					case -15:
						break;
					case 14:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DOT);
}
					case -16:
						break;
					case 15:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.COMMA);
}
					case -17:
						break;
					case 16:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.COLON);
}
					case -18:
						break;
					case 17:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.SEMI);
}
					case -19:
						break;
					case 18:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LBRACE);
}
					case -20:
						break;
					case 19:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.RBRACE);
}
					case -21:
						break;
					case 20:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LT);
}
					case -22:
						break;
					case 21:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.EQ);
}
					case -23:
						break;
					case 22:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.AT);
}
					case -24:
						break;
					case 23:
						{
    yybegin(LINE_COMMENT);
}
					case -25:
						break;
					case 24:
						{
    yybegin(BLOCK_COMMENT);
    openBlockComments++;
}
					case -26:
						break;
					case 25:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -27:
						break;
					case 26:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IF);
}
					case -28:
						break;
					case 27:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IN);
}
					case -29:
						break;
					case 28:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.FI);
}
					case -30:
						break;
					case 29:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.OF);
}
					case -31:
						break;
					case 30:
						{
/*  PA1 4.1 Error Handling
    Report only the first string constant error to occur, and then
    resume lexing after the end of the string. The end of the string is defined as either
    1. the beginning of the next line if an unescaped newline occurs at any point in the string;
*/
    return lexString(yytext());
}
					case -32:
						break;
					case 32:
						{
    return lexString(yytext());
}
					case -33:
						break;
					case 33:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ASSIGN);
}
					case -34:
						break;
					case 34:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LE);
}
					case -35:
						break;
					case 35:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DARROW);
}
					case -36:
						break;
					case 36:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LET);
}
					case -37:
						break;
					case 37:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NEW);
}
					case -38:
						break;
					case 38:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NOT);
}
					case -39:
						break;
					case 39:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CASE);
}
					case -40:
						break;
					case 40:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LOOP);
}
					case -41:
						break;
					case 41:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ELSE);
}
					case -42:
						break;
					case 42:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ESAC);
}
					case -43:
						break;
					case 43:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.THEN);
}
					case -44:
						break;
					case 44:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.POOL);
}
					case -45:
						break;
					case 45:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
					case -46:
						break;
					case 46:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CLASS);
}
					case -47:
						break;
					case 47:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.WHILE);
}
					case -48:
						break;
					case 48:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
					case -49:
						break;
					case 49:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ISVOID);
}
					case -50:
						break;
					case 50:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.INHERITS);
}
					case -51:
						break;
					case 51:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -52:
						break;
					case 52:
						{
    // comments are discarded
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
    openBlockComments++;
}
					case -55:
						break;
					case 55:
						{
    // comments are discarded
    openBlockComments--;
    if (openBlockComments == 0) {
        // all block comments have been properly closed
        yybegin(YYINITIAL);
    }
}
					case -56:
						break;
					case 56:
						{
    curr_lineno = yyline+1;
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
					case -57:
						break;
					case 57:
						{
/* This rule should be the very last in your lexical specification and will match match
everything not matched by other lexical rules. */
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -58:
						break;
					case 58:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -59:
						break;
					case 59:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -60:
						break;
					case 60:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IF);
}
					case -61:
						break;
					case 61:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IN);
}
					case -62:
						break;
					case 62:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.FI);
}
					case -63:
						break;
					case 63:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.OF);
}
					case -64:
						break;
					case 64:
						{
/*  PA1 4.1 Error Handling
    Report only the first string constant error to occur, and then
    resume lexing after the end of the string. The end of the string is defined as either
    1. the beginning of the next line if an unescaped newline occurs at any point in the string;
*/
    return lexString(yytext());
}
					case -65:
						break;
					case 66:
						{
    return lexString(yytext());
}
					case -66:
						break;
					case 67:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LET);
}
					case -67:
						break;
					case 68:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NEW);
}
					case -68:
						break;
					case 69:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NOT);
}
					case -69:
						break;
					case 70:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CASE);
}
					case -70:
						break;
					case 71:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LOOP);
}
					case -71:
						break;
					case 72:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ELSE);
}
					case -72:
						break;
					case 73:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ESAC);
}
					case -73:
						break;
					case 74:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.THEN);
}
					case -74:
						break;
					case 75:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.POOL);
}
					case -75:
						break;
					case 76:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CLASS);
}
					case -76:
						break;
					case 77:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.WHILE);
}
					case -77:
						break;
					case 78:
						{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ISVOID);
}
					case -78:
						break;
					case 79:
						{
    curr_lineno = yyline+1;
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
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -81:
						break;
					case 82:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -82:
						break;
					case 83:
						{
/*  PA1 4.1 Error Handling
    Report only the first string constant error to occur, and then
    resume lexing after the end of the string. The end of the string is defined as either
    1. the beginning of the next line if an unescaped newline occurs at any point in the string;
*/
    return lexString(yytext());
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
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -85:
						break;
					case 87:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -86:
						break;
					case 89:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -87:
						break;
					case 90:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -88:
						break;
					case 92:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -89:
						break;
					case 93:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -90:
						break;
					case 95:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -91:
						break;
					case 96:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -92:
						break;
					case 98:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -93:
						break;
					case 99:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -94:
						break;
					case 100:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -95:
						break;
					case 101:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -96:
						break;
					case 102:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -97:
						break;
					case 103:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -98:
						break;
					case 104:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -99:
						break;
					case 105:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -100:
						break;
					case 106:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -101:
						break;
					case 107:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -102:
						break;
					case 108:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -103:
						break;
					case 109:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -104:
						break;
					case 110:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -105:
						break;
					case 111:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -106:
						break;
					case 112:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -107:
						break;
					case 113:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -108:
						break;
					case 114:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -109:
						break;
					case 115:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -110:
						break;
					case 116:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -111:
						break;
					case 117:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -112:
						break;
					case 118:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -113:
						break;
					case 119:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -114:
						break;
					case 120:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -115:
						break;
					case 121:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -116:
						break;
					case 122:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -117:
						break;
					case 123:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -118:
						break;
					case 124:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -119:
						break;
					case 125:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -120:
						break;
					case 126:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -121:
						break;
					case 127:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -122:
						break;
					case 128:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -123:
						break;
					case 129:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -124:
						break;
					case 130:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -125:
						break;
					case 131:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -126:
						break;
					case 132:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -127:
						break;
					case 133:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -128:
						break;
					case 134:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -129:
						break;
					case 135:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -130:
						break;
					case 136:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -131:
						break;
					case 137:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -132:
						break;
					case 138:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -133:
						break;
					case 139:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -134:
						break;
					case 140:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -135:
						break;
					case 141:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -136:
						break;
					case 142:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -137:
						break;
					case 143:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -138:
						break;
					case 144:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -139:
						break;
					case 145:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -140:
						break;
					case 146:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -141:
						break;
					case 147:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -142:
						break;
					case 148:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -143:
						break;
					case 149:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -144:
						break;
					case 150:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -145:
						break;
					case 151:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -146:
						break;
					case 152:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -147:
						break;
					case 153:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -148:
						break;
					case 154:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -149:
						break;
					case 155:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -150:
						break;
					case 156:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -151:
						break;
					case 157:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -152:
						break;
					case 158:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -153:
						break;
					case 159:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -154:
						break;
					case 160:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -155:
						break;
					case 161:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -156:
						break;
					case 162:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -157:
						break;
					case 163:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -158:
						break;
					case 164:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -159:
						break;
					case 165:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -160:
						break;
					case 166:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -161:
						break;
					case 167:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -162:
						break;
					case 168:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -163:
						break;
					case 169:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -164:
						break;
					case 170:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -165:
						break;
					case 171:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -166:
						break;
					case 172:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -167:
						break;
					case 173:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -168:
						break;
					case 174:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -169:
						break;
					case 175:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -170:
						break;
					case 176:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -171:
						break;
					case 177:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -172:
						break;
					case 178:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -173:
						break;
					case 179:
						{
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
					case -174:
						break;
					case 180:
						{
    curr_lineno = yyline+1;
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
