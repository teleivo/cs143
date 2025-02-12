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
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
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
STRING_TEXT=([^\n\0\"]|(\\\n))*
STRING_TEXT_NUL_BYTE=([^\n\"]|(\\\n))*
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
<BLOCK_COMMENT> "*)" {
    // comments are discarded
    openBlockComments--;
    if (openBlockComments == 0) {
        // all block comments have been properly closed
        yybegin(YYINITIAL);
    }
}
<YYINITIAL> {CLASS} {
    return new Symbol(TokenConstants.CLASS);
}
<YYINITIAL> {IF} {
    return new Symbol(TokenConstants.IF);
}
<YYINITIAL> {FI} {
    return new Symbol(TokenConstants.FI);
}
<YYINITIAL> {ELSE} {
    return new Symbol(TokenConstants.ELSE);
}
<YYINITIAL> {IN} {
    return new Symbol(TokenConstants.IN);
}
<YYINITIAL> {INHERITS} {
    return new Symbol(TokenConstants.INHERITS);
}
<YYINITIAL> {ISVOID} {
    return new Symbol(TokenConstants.ISVOID);
}
<YYINITIAL> {LET} {
// TODO do I also need to lex a let statement or is that the parsers job?
    return new Symbol(TokenConstants.LET);
}
<YYINITIAL> {LOOP} {
    return new Symbol(TokenConstants.LOOP);
}
<YYINITIAL> {POOL} {
    return new Symbol(TokenConstants.POOL);
}
<YYINITIAL> {THEN} {
    return new Symbol(TokenConstants.THEN);
}
<YYINITIAL> {WHILE} {
    return new Symbol(TokenConstants.WHILE);
}
<YYINITIAL> {CASE} {
    return new Symbol(TokenConstants.CASE);
}
<YYINITIAL> {ESAC} {
    return new Symbol(TokenConstants.ESAC);
}
<YYINITIAL> {NEW} {
    return new Symbol(TokenConstants.NEW);
}
<YYINITIAL> {OF} {
    return new Symbol(TokenConstants.OF);
}
<YYINITIAL> {NOT} {
    return new Symbol(TokenConstants.NOT);
}
<YYINITIAL> {TRUE} {
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE);
}
<YYINITIAL> {FALSE} {
    return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE);
}
<YYINITIAL> "SELF_TYPE" {
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {LOWERCASE}({DIGIT}|{ALPHA}|_)*  {
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {UPPERCASE}({DIGIT}|{ALPHA}|_)* {
// TODO does it also have a max length?
    AbstractSymbol id = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, new IdSymbol(id.getString(),id.getString().length(), id.index));
}
<YYINITIAL> {DIGIT}* {
    AbstractSymbol number = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, new IdSymbol(number.getString(),number.getString().length(), number.index));
}
<YYINITIAL> \"{STRING_TEXT}\" {
	if (yytext().length() > MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
    AbstractSymbol str = AbstractTable.stringtable.addString(yytext());
    return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str.getString(),str.getString().length(), str.index));
}
<YYINITIAL> \"{STRING_TEXT_NUL_BYTE}\" {
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
<YYINITIAL> \"{STRING_TEXT_UNESCAPED_NEWLINE}\" {
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
<YYINITIAL> \"{STRING_TEXT_UNESCAPED_NEWLINE} {
    yybegin(STRING);
}
<YYINITIAL> "*" {
    return new Symbol(TokenConstants.MULT);
}
<YYINITIAL> "/" {
    return new Symbol(TokenConstants.DIV);
}
<YYINITIAL> "-" {
// TODO does the lexer need to discern minus and negate?
    return new Symbol(TokenConstants.MINUS);
}
<YYINITIAL> "+" {
    return new Symbol(TokenConstants.PLUS);
}
<YYINITIAL> "." {
    return new Symbol(TokenConstants.DOT);
}
<YYINITIAL> "," {
    return new Symbol(TokenConstants.COMMA);
}
<YYINITIAL> ":" {
    return new Symbol(TokenConstants.COLON);
}
<YYINITIAL> ";" {
    return new Symbol(TokenConstants.SEMI);
}
<YYINITIAL> "{" {
    return new Symbol(TokenConstants.LBRACE);
}
<YYINITIAL> "}" {
    return new Symbol(TokenConstants.RBRACE);
}
<YYINITIAL> "(" {
    return new Symbol(TokenConstants.LPAREN);
}
<YYINITIAL> ")" {
    return new Symbol(TokenConstants.RPAREN);
}
<YYINITIAL> "<=" {
    return new Symbol(TokenConstants.LE);
}
<YYINITIAL> "<" {
    return new Symbol(TokenConstants.LT);
}
<YYINITIAL> "=" {
// TODO does the lexer need to discern eq and assign?
    return new Symbol(TokenConstants.EQ);
}
<YYINITIAL> "=>" {
    return new Symbol(TokenConstants.DARROW);
}
<YYINITIAL> "@"	{
    return new Symbol(TokenConstants.AT);
}
. {
/* This rule should be the very last in your lexical specification and will match match
everything not matched by other lexical rules. */
  System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
