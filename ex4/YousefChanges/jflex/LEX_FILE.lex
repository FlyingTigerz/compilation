/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup


/******************************************************************/
/* An exception is thrown when input cannot be matched 					  */
/******************************************************************/
%yylexthrow Exception

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }
	public int getCharPos() { return yycolumn; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace			= {LineTerminator} | [ \t\f]
LPAREN		= "("
RPAREN 		= ")"
LBRACK		= "["
RBRACK		= "]"
LBRACE		= "{"
RBRACE		= "}"
NIL 			= "nil"
PLUS			= "+"
MINUS			= "-"
TIMES			= "*"
DIVIDE 		= "/"
COMMA 		= ","
DOT 			= "."
SEMICOLON	= ";"
TYPE_INT  = "int"
ASSIGN 		= ":="
EQ 				= "="
LT 				= "<"
GT 				= ">"
ARRAY 		= "array"
CLASS 		= "class"
EXTENDS 	= "extends"
RETURN 		= "return"
WHILE 		= "while"
IF 				= "if"
NEW 			= "new"
TYPE_STRING = "string"
TYPE_VOID = "void"
/* Regex token macros */
INT				= 0 | [1-9][0-9]*
STRING		= \"[a-zA-Z]*\"
ID				= [a-zA-Z]+[a-zA-Z0-9]*
COMMENT_CHAR		= [a-zA-Z0-9 \t\f\(\)\[\]\{\}\?\!\+\-\*\/\.\;]*
MULTI_COMMENT		= \/\*({COMMENT_CHAR}|{LineTerminator})*\*\/
COMMENT					= \/\/{COMMENT_CHAR}*{LineTerminator}
INVALID_COMMENT= (\/\*)|(\/\/{LineTerminator})
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

{COMMENT}		    { /* skip, do nothing */ }
{MULTI_COMMENT}	{ /* skip, do nothing */ }
{INVALID_COMMENT}	{ throw new Exception("LEXICAL ERROR: invalid comment"); }
{LPAREN}		{ return symbol(TokenNames.LPAREN)		;}
{RPAREN}		{ return symbol(TokenNames.RPAREN)		;}
{LBRACK}		{ return symbol(TokenNames.LBRACK)		;}
{RBRACK}		{ return symbol(TokenNames.RBRACK)		;}
{LBRACE}		{ return symbol(TokenNames.LBRACE)	 	;}
{RBRACE}		{ return symbol(TokenNames.RBRACE)	 	;}
{NIL}				{ return symbol(TokenNames.NIL)			 	;}
{PLUS}			{ return symbol(TokenNames.PLUS)		 	;}
{MINUS}			{ return symbol(TokenNames.MINUS)		 	;}
{TIMES}			{ return symbol(TokenNames.TIMES)		 	;}
{DIVIDE}		{ return symbol(TokenNames.DIVIDE)	 	;}
{COMMA}			{ return symbol(TokenNames.COMMA)		 	;}
{DOT}				{ return symbol(TokenNames.DOT)			 	;}
{SEMICOLON}	{ return symbol(TokenNames.SEMICOLON)	;}
{TYPE_INT}	{ return symbol(TokenNames.TYPE_INT)	;}
{ASSIGN}		{ return symbol(TokenNames.ASSIGN)		;}
{EQ}				{ return symbol(TokenNames.EQ)				;}
{LT}				{ return symbol(TokenNames.LT)				;}
{GT}				{ return symbol(TokenNames.GT)				;}
{ARRAY}			{ return symbol(TokenNames.ARRAY)			;}
{CLASS}	  	{ return symbol(TokenNames.CLASS)			;}
{EXTENDS}		{ return symbol(TokenNames.EXTENDS)		;}
{RETURN}		{ return symbol(TokenNames.RETURN)		;}
{WHILE}			{ return symbol(TokenNames.WHILE)			;}
{IF}				{ return symbol(TokenNames.IF)				;}
{NEW}				{ return symbol(TokenNames.NEW)				;}
{TYPE_STRING}	{ return symbol(TokenNames.TYPE_STRING);}
{INT} {
	int x = new Integer(yytext());
	if(x < 0 || x > 65535) {throw new Exception("LEXICAL ERROR: integer out of bounds"); }
	return symbol(TokenNames.INT, x);
}
{STRING} {
    String s = new String(yytext());
    s = s.substring( 1, s.length() - 1 );
    return symbol(TokenNames.STRING, s);
}
{ID}			{ return symbol(TokenNames.ID,     new String( yytext()));}
{WhiteSpace}	{ /* skip, do nothing */ }
<<EOF>>				{ return symbol(TokenNames.EOF);}
.					{throw new Exception("LEXICAL ERROR: invalid token");}
}
