/*
 *  vim: set ft=java:
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;

%%

%{

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

    // Convert escaped chars according to PA1 4.3 Strings and the Cool Reference Manual 10.2 Strings
    private Symbol unescapeString(String stringText) {
        StringBuilder out = new StringBuilder(stringText.length());

        int i = 0;
        while (i < stringText.length()) {
            char c1 = stringText.charAt(i);
            if (c1 == '\n') {
                return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
            }
            if (c1 == '\0') {
                return new Symbol(TokenConstants.ERROR, "String contains null character");
            }

            if (c1 == '\\' && i + 1 < stringText.length()) {
                char c2 = stringText.charAt(i + 1);

                if (c2 == 'b') {
                  out.append('\b');
                } else if (c2 == 't') {
                  out.append('\t');
                } else if (c2 == 'n') {
                  out.append('\n');
                } else if (c2 == 'f') {
                  out.append('\f');
                } else {
                  out.append(c2);
                }
                i++; // consume one extra char for '\'
            } else {
                out.append(c1);
            }
            i++;
        }

        AbstractSymbol str = AbstractTable.stringtable.addString(out.toString());
        return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(), str.getString().length(), str.index));
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer class constructor, all the extra
 *  initialization you want to do should go here.  Don't remove or modify anything that was there
 *  initially. */

    // empty for now
%init}

%eofval{

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
        curr_lineno = yyline+1;
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        curr_lineno = yyline+1;
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }

    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%line
%char
%state LINE_COMMENT
%state BLOCK_COMMENT
%state STRING

CLASS=[Cc][Ll][Aa][Ss][Ss]
IF=[Ii][Ff]
FI=[Ff][Ii]
ELSE=[Ee][Ll][Ss][Ee]
IN=[Ii][Nn]
INHERITS=[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]
ISVOID=[Ii][Ss][Vv][Oo][Ii][Dd]
LET=[Ll][Ee][Tt]
LOOP=[Ll][Oo][Oo][Pp]
POOL=[Pp][Oo][Oo][Ll]
THEN=[Tt][Hh][Ee][Nn]
WHILE=[Ww][Hh][Ii][Ll][Ee]
CASE=[Cc][Aa][Ss][Ee]
ESAC=[Ee][Ss][Aa][Cc]
NEW=[Nn][Ee][Ww]
OF=[Oo][Ff]
NOT=[Nn][Oo][Tt]
TRUE=(t[rR][uU][eE])
FALSE=(f[aA][lL][sS][eE])
DIGIT=[0-9]
LOWERCASE=[a-z]
UPPERCASE=[A-Z]
ALPHA=[A-Za-z]
WHITESPACE_WITHOUT_NEWLINE=[\ \f\r\t\v]
STRING_TEXT=([^\"])*
STRING_TEXT_UNESCAPED_NEWLINE=([^\0\"]|(\\\n))*

%%

<YYINITIAL> {WHITESPACE_WITHOUT_NEWLINE} {
}
<YYINITIAL> "--" {
    yybegin(LINE_COMMENT);
}
<LINE_COMMENT> . {
    // comments are discarded
}
<YYINITIAL,LINE_COMMENT> \n {
    // comments are discarded
    yybegin(YYINITIAL);
}
<YYINITIAL> "(*" {
    yybegin(BLOCK_COMMENT);
    openBlockComments++;
}
<BLOCK_COMMENT>"(*"  {
    openBlockComments++;
}
<BLOCK_COMMENT> (.|\n) {
    // comments are discarded
}
<YYINITIAL> "*)" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
<BLOCK_COMMENT> "*)" {
    // comments are discarded
    openBlockComments--;
    if (openBlockComments == 0) {
        // all block comments have been properly closed
        yybegin(YYINITIAL);
    }
}
<YYINITIAL> {CLASS} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CLASS);
}
<YYINITIAL> {IF} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IF);
}
<YYINITIAL> {FI} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.FI);
}
<YYINITIAL> {ELSE} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ELSE);
}
<YYINITIAL> {IN} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.IN);
}
<YYINITIAL> {INHERITS} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.INHERITS);
}
<YYINITIAL> {ISVOID} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ISVOID);
}
<YYINITIAL> {LET} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LET);
}
<YYINITIAL> {LOOP} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LOOP);
}
<YYINITIAL> {POOL} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.POOL);
}
<YYINITIAL> {THEN} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.THEN);
}
<YYINITIAL> {WHILE} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.WHILE);
}
<YYINITIAL> {CASE} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.CASE);
}
<YYINITIAL> {ESAC} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ESAC);
}
<YYINITIAL> {NEW} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NEW);
}
<YYINITIAL> {OF} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.OF);
}
<YYINITIAL> {NOT} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NOT);
}
<YYINITIAL> {TRUE} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
<YYINITIAL> {FALSE} {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
<YYINITIAL> "SELF_TYPE" {
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {LOWERCASE}({DIGIT}|{ALPHA}|_)*  {
    curr_lineno = yyline+1;
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {UPPERCASE}({DIGIT}|{ALPHA}|_)* {
    curr_lineno = yyline+1;
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {DIGIT}* {
    curr_lineno = yyline+1;
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
<YYINITIAL> \"{STRING_TEXT}\" {
// TODO check line nr for \n and multi-line strings with \
// When reporting line numbers for a multi-line string, use the last line. In general, the line number
// should be where the token ends.
    curr_lineno = yyline+1;
    String text = yytext();
	if (text.length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}

    if (text.length() == 2) {
        AbstractSymbol stringText = AbstractTable.stringtable.addString("");
        return new Symbol(TokenConstants.STR_CONST, new StringSymbol(stringText.getString(), stringText.getString().length(), stringText.index));
    }

    return unescapeString(text.substring(1, text.length()-1));
}
<YYINITIAL> \"{STRING_TEXT_UNESCAPED_NEWLINE} {
    yybegin(STRING);
}
<YYINITIAL> "*" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.MULT);
}
<YYINITIAL> "/" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DIV);
}
<YYINITIAL> "~" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.NEG);
}
<YYINITIAL> "-" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.MINUS);
}
<YYINITIAL> "+" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.PLUS);
}
<YYINITIAL> "." {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DOT);
}
<YYINITIAL> "," {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.COMMA);
}
<YYINITIAL> ":" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.COLON);
}
<YYINITIAL> ";" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.SEMI);
}
<YYINITIAL> "{" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LBRACE);
}
<YYINITIAL> "}" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.RBRACE);
}
<YYINITIAL> "(" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LPAREN);
}
<YYINITIAL> ")" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.RPAREN);
}
<YYINITIAL> "<=" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LE);
}
<YYINITIAL> "<" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.LT);
}
<YYINITIAL> "=" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.EQ);
}
<YYINITIAL> "<-" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ASSIGN);
}
<YYINITIAL> "=>" {
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.DARROW);
}
<YYINITIAL> "@"	{
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.AT);
}
. {
/* This rule should be the very last in your lexical specification and will match match
everything not matched by other lexical rules. */
    curr_lineno = yyline+1;
    return new Symbol(TokenConstants.ERROR, yytext());
}
