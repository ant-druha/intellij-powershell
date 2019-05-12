package com.intellij.plugin.powershell.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;

%%

%{
  public _PowerShellLexer() {
    this((java.io.Reader)null);
  }

  private static final class State {
          final int lParenCount;
          final int state;

          public State(int state, int lParenCount) {
              this.state = state;
              this.lParenCount = lParenCount;
          }

          @Override
          public String toString() {
              return "yystate = " + state + (lParenCount == 0 ? "" : "lParenCount = " + lParenCount);
          }
      }

      private final Stack<State> states = new Stack<State>();
      private int lParenCount;

      private int commentStart;
      private int commentDepth;

      private int yycolumn = 0;

      private void pushState(int state) {
          states.push(new State(yystate(), lParenCount));
          lParenCount = 0;
          yybegin(state);
      }

      private void popState() {
          if (states.empty()) {
              yybegin(YYINITIAL);
              return;
          }
          State state = states.pop();
          lParenCount = state.lParenCount;
          yybegin(state.state);
      }

      public int getState() {
      return states.peek().state;
      }

%}

%public
%class _PowerShellLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%column

EOL=\R
WHITE_SPACE_CHAR=[\ \t\f\xA0\u2002]|{BACKTICK}{NL}
BACKTICK="`"
WHITE_SPACE={WHITE_SPACE_CHAR}+

//comparison operators
AS=[aA][sS]
NOT=[nN][oO][tT]
CNOT=[cC]{NOT}
INOT=[iI]{NOT}
IS=[iI][sS]
ISNOT={IS}{NOT}

IN=[iI][nN]
NOTIN={NOT}{IN}

LIKE=[lL][iI][kK][eE]
NOTLIKE={NOT}{LIKE}
ILIKE=[iI]{LIKE}
INOTLIKE={INOT}{LIKE}
CLIKE=[cC]{LIKE}
CNOTLIKE={CNOT}{LIKE}

MATCH=[mM][aA][tT][cC][hH]
NOTMATCH={NOT}{MATCH}
CMATCH=[cC]{MATCH}
CNOTMATCH={CNOT}{MATCH}
IMATCH=[iI]{MATCH}
INOTMATCH={INOT}{MATCH}

CONTAINS=[cC][oO][nN][tT][aA][iI][nN][sS]
NOTCONTAINS={NOT}{CONTAINS}
CCONTAINS=[cC]{CONTAINS}
CNOTCONTAINS={CNOT}{CONTAINS}
ICONTAINS=[iI]{CONTAINS}
INOTCONTAINS={INOT}{CONTAINS}

REPLACE=[rR][eE][pP][lL][aA][cC][eE]
CREPLACE=[cC]{REPLACE}
IREPLACE=[iI]{REPLACE}

SPLIT=[sS][pP][lL][iI][tT]
CSPLIT=[cC]{SPLIT}
ISPLIT=[iI]{SPLIT}

EQ=[eE][qQ]
NE=[nN][eE]
GE=[gG][eE]
GT=[gG][tT]
LE=[lL][eE]
LT=[lL][tT]

CEQ=[cC]{EQ}
CNE=[cC]{NE}
CGE=[cC]{GE}
CGT=[cC]{GT}
CLE=[cC]{LE}
CLT=[cC]{LT}

IEQ=[iI]{EQ}
INE=[iI]{NE}
IGE=[iI]{GE}
IGT=[iI]{GT}
ILE=[iI]{LE}
ILT=[iI]{LT}

JOIN=[jJ][oO][iI][nN]

SHL=[sS][hH][lL]
SHR=[sS][hH][rR]

AND=[aA][nN][dD]
OR=[oO][rR]

COMPARISON_TOKEN={AS}|{CCONTAINS}|{CEQ}|{CGE}|{CGT}|{CLE}|{CLIKE}|{CLT}|{CMATCH}|{CNE}|{CNOTCONTAINS}|{CNOTLIKE}|{CNOTMATCH}|{CONTAINS}
|{CREPLACE}|{CSPLIT}|{EQ}|{GE}|{GT}|{ICONTAINS}|{IEQ}|{IGE}|{IGT}|{ILE}|{ILIKE}|{ILT}|{IMATCH}|{IN}|{INE}|{INOTCONTAINS}|{INOTLIKE}
|{INOTMATCH}|{IREPLACE}|{IS}|{ISNOT}|{ISPLIT}|{JOIN}|{LE}|{LIKE}|{LT}|{MATCH}|{NE}|{NOTCONTAINS}|{NOTIN}|{NOTLIKE}|{NOTMATCH}
|{REPLACE}|{SHL}|{SHR}|{SPLIT}

OP_C={DASH}{COMPARISON_TOKEN}
OP_MR=("*>&1"|"2>&1"|"3>&1"|"4>&1"|"5>&1"|"6>&1"|"*>&2"|"1>&2"|"3>&2"|"4>&2"|"5>&2"|"6>&2")
OP_FR=(">"|">>"|"2>"|"2>>"|"3>"|"3>>"|"4>"|"4>>"|"5>"|"5>>"|"6>"|"6>>"|"*>"|"*>>"|"<")
OP_NOT={DASH}{NOT}
OP_AND={DASH}{AND}
OP_OR={DASH}{OR}
OP_XOR={DASH}[xX]{OR}
OP_BNOT={DASH}[bB]{NOT}
OP_BAND={DASH}[bB]{AND}
OP_BXOR={DASH}[bB][xX]{OR}
OP_BOR={DASH}[bB]{OR}
EXCL_MARK="!"

NL=(\r|\n|\r\n)
NLS={WHITE_SPACE}*{NL}({NL}|{WHITE_SPACE}*)*
CH_DQ=(\"|“|”|„)
CH_SQ=(\'|‘|’|‚|‛)
//BRACED_VAR="${"{VAR_SCOPE}?{WHITE_SPACE}?{BRACED_ID}"}"
//EXPAND_STRING_CHARS=([^\$\"\“\”\„\`\r\n]|{BRACED_VAR}|"$"[^\"\“\”\„\`\r\n]|"$"(\`.)|(\`.)|{CH_DQ}{CH_DQ})+
EXPANDABLE_STRING_PART=([^\$\"\“\”\„\`]/*|{BRACED_VAR}|"$"[^\"\“\”\„\`\r\n]|"$"(\`.)*/|(\`.)|{CH_DQ}{CH_DQ})+
//EXPANDABLE_HERE_STRING_PART=(([^\"\“\”\„\\]|\\.)+(\r|\n|\r\n))
EXPANDABLE_HERE_STRING_PART=({EXPANDABLE_STRING_PART}|(\r|\n|\r\n))+
//EXPANDABLE_STRING={CH_DQ}{EXPAND_STRING_CHARS}?"$"*{CH_DQ}

VERBATIM_STRING_CHARS=([^\'\‘\’\‚\‛]|{CH_SQ}{CH_SQ})+
VERBATIM_STRING={CH_SQ}{VERBATIM_STRING_CHARS}?{CH_SQ}

EXPANDABLE_HERE_STRING_START=@{CH_DQ}([ \t\x0B\f])*(\r|\n|\r\n)
EXPANDABLE_HERE_STRING_END=/*(\r|\n|\r\n)*/{CH_DQ}@
//EXPANDABLE_HERE_STRING={EXPANDABLE_HERE_STRING_START}{EXPANDABLE_HERE_STRING_PART}*{EXPANDABLE_HERE_STRING_END}
VERBATIM_HERE_STRING=@{CH_SQ}([ \t\x0B\f])*(\r|\n|\r\n)( [^\r\n] | (\r|\n|\r\n) ([^\'\‘\’\‚\‛] | {CH_SQ}[^@]) )*([\n\x0B\r])*{CH_SQ}@
DEC_DIGIT=[0-9]
HEX_DIGIT={DEC_DIGIT}|[abcdefABCDEF]
DEC_DIGITS={DEC_DIGIT}+
HEX_DIGITS={HEX_DIGIT}+{DEC_DIGIT}*

DEC_EXPONENT=[Ee][+-]?[0-9]+
DEC_SUF=[dDlL]
NUM_MULTIPLIER=([kK]|[mM]|[gG]|[tT]|[pP])[bB]

REAL_NUM={DEC_DIGITS}{DOT}{DEC_DIGITS}?{DEC_EXPONENT}?{DEC_SUF}?{NUM_MULTIPLIER}?|{DOT}{DEC_DIGITS}{DEC_EXPONENT}?{DEC_SUF}?{NUM_MULTIPLIER}?|{DEC_DIGITS}{DEC_EXPONENT}{DEC_SUF}?{NUM_MULTIPLIER}?
HEX_INTEGER=0x{HEX_DIGITS}[lL]?{NUM_MULTIPLIER}?
DEC_INTEGER={DEC_DIGITS}{DEC_SUF}?{NUM_MULTIPLIER}?

DOT_DOT=".."
DOT="."
SEMI=";"
COLON2="::"
EQ_DASH={DASH}"="
EQ_PLUS="+="
EQ_STAR={STAR}"="
EQ_DIV={DIV}"="
EQ_PERS={PERS}"="
PERS="%"
STAR="*"
DIV="/"
HASH="#"
SQBR_L="["
SQBR_R="]"
DS="$"
LCURLY="{"
RCURLY="}"

SIMPLE_ID_FIRST_CHAR=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_)
SIMPLE_ID_CHAR={SIMPLE_ID_FIRST_CHAR}|(\p{Nd}|\_|{HASH})
SIMPLE_ID={SIMPLE_ID_FIRST_CHAR}{SIMPLE_ID_CHAR}*

VAR_ID_CHAR={SIMPLE_ID_CHAR}|(\?)
VAR_ID={VAR_ID_CHAR}+

QMARK="?"
HAT="^"
GENERIC_ID_PART_FIRST_CHAR=([^@\*\/\\\.\=\[\]\%\-\–\—\―\}\{\(\)\,\;\"\“\”\„\'\|\&\$\s\n\r\#\:\`0-9!\+]|(`.))
GENERIC_ID_PART_CHAR={GENERIC_ID_PART_FIRST_CHAR}|([0-9!@])
GENERIC_ID_PART={GENERIC_ID_PART_FIRST_CHAR}(({GENERIC_ID_PART_CHAR}*[\-\–\—\―]{GENERIC_ID_PART_FIRST_CHAR}+)?|{GENERIC_ID_PART_CHAR}*)
GENERIC_ID_PART_TOKEN_START={SIMPLE_ID}|{GENERIC_ID_PART}|{STAR}
GENERIC_ID_PART_TOKEN={GENERIC_ID_PART_TOKEN_START}|{DOT}|\+|"\\"|{DIV}|{DASH}|\*|{PERS}
GENERIC_ID_PART_TOKEN_TAIL={GENERIC_ID_PART_TOKEN_START}|{DOT}|\+|"\\"|{DIV}|{DASH}|\*|{PERS}
GENERIC_ID_PART_TOKENS={GENERIC_ID_PART_TOKEN_START}{GENERIC_ID_PART_TOKEN_TAIL}*

BRACED_ID_CHAR=([^\}\`]|(`.))
BRACED_ID={BRACED_ID_CHAR}+
//TYPE_NAME=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nd}|\_)+
SINGLE_LINE_COMMENT_CHARS=[^\n\r]+
SINGLE_LINE_COMMENT={HASH}{SINGLE_LINE_COMMENT_CHARS}?
DELIMITED_COMENT_START=<{HASH}
DELIMITED_COMENT_END={HASH}+>
DELIMITED_COMMENT_CHARS=(>|{HASH}*[^#>])+
DELIMITED_COMMENT={DELIMITED_COMENT_START}{DELIMITED_COMMENT_CHARS}?{DELIMITED_COMENT_END}

//AMP_ARG=\&/*[^&]*/[\w]
//PARAM_ARGUMENT=([\w]([\w$0-9]|{DASH})*)
//ALNUM=([:letter:]|[:digit:])+
LETTERS=[a-zA-Z]+
DASH=[\-\–\—\―]
MM="--"
CMD_PARAMETER=({DASH}|{DIV})(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_|\?){PARAMETER_CHAR}*:?
PARAMETER_CHAR=[^\{\}\(\)\;\,\|\&\.\[\:\s\n\r\'\‘\’\‚\‛\"\“\”\„]
VERBATIM_ARG_START={MM}{PERS}
VERBATIM_ARG_INPUT=[^\|\r\n]+
BRACED_VAR_START={DS}{LCURLY}

%state VAR_SIMPLE VAR_BRACED VERBATIM_ARGUMENT FUNCTION_ID TYPE_ID STRING HERE_STRING STRING_SUB_EXPRESSION
%caseless


%%

<STRING, HERE_STRING> {
  {EXPANDABLE_STRING_PART}                                     { return EXPANDABLE_STRING_PART; }
  {DS}/{SIMPLE_ID}                                             { pushState(VAR_SIMPLE); return DS; }
  {DS}/{QMARK}|{HAT}|{DS}                                      { pushState(VAR_SIMPLE); return DS; }
  {BRACED_VAR_START}                                           { pushState(VAR_BRACED); return BRACED_VAR_START; }
  {DS}/"("                                                     { pushState(STRING_SUB_EXPRESSION); return DS; }
  {DS}                                                         { return EXPANDABLE_STRING_PART; }
}

<STRING> {
  {CH_DQ}                                                      { popState(); return DQ_CLOSE; }
}

<HERE_STRING> {
  {EXPANDABLE_HERE_STRING_END}                                 { if (yycolumn==0) {popState(); return EXPANDABLE_HERE_STRING_END;} else return EXPANDABLE_HERE_STRING_PART; }
  {CH_DQ}                                                      { return EXPANDABLE_HERE_STRING_PART; }
  {EXPANDABLE_HERE_STRING_PART}                                { return EXPANDABLE_HERE_STRING_PART; }
}

<VAR_SIMPLE> {
  "this"/{DOT}                                                 { State s = states.get(states.size() - 1); if (s.state == STRING || s.state == HERE_STRING) popState(); return THIS; }
  "this"/{COLON2}                                              { popState(); return THIS; }
  "this"/":"                                                   { return THIS; }
  "this"                                                       { popState(); return THIS; }
  {SIMPLE_ID}/{DOT}                                            { State s = states.get(states.size() - 1); if (s.state == STRING || s.state == HERE_STRING) popState(); return SIMPLE_ID; }
  {SIMPLE_ID}/{COLON2}                                         { popState(); return SIMPLE_ID; }
  {SIMPLE_ID}/":"                                              { return SIMPLE_ID; }
  {SIMPLE_ID}                                                  { popState(); return SIMPLE_ID; }
  {QMARK}                                                      { popState(); return QMARK; }
  {HAT}                                                        { popState(); return HAT; }
  {DS}                                                         { popState(); return DS; }
  {VAR_ID}/{DOT}                                               { State s = states.get(states.size() - 1); if (s.state == STRING || s.state == HERE_STRING) popState(); return VAR_ID; }
  {VAR_ID}                                                     { popState(); return VAR_ID; }
  ":"                                                          { return COLON; }
  {DOT}/{SIMPLE_ID}                                            { return DOT; }
  [^]                                                          { popState(); yypushback(yylength()); }
}
<VAR_BRACED> {
  {SIMPLE_ID}   / ":"{BRACED_ID}{RCURLY}                       { return SIMPLE_ID; }
  ":"           / {BRACED_ID}{RCURLY}                          { return COLON; }
  {BRACED_ID}                                                  { return BRACED_ID; }
  {BACKTICK}                                                   { popState(); return BACKTICK; }
  {RCURLY}                                                     { popState(); return RCURLY; }
}

<VERBATIM_ARGUMENT> {
  {VERBATIM_ARG_INPUT}                                         { return VERBATIM_ARG_INPUT; }
  "|"                                                          { popState(); return PIPE; }
  {NLS}                                                        { popState(); return NLS; }
}
<FUNCTION_ID> {
  {SIMPLE_ID}                                                  { popState(); return SIMPLE_ID; }
  {WHITE_SPACE}                                                { return WHITE_SPACE; }
  {GENERIC_ID_PART_TOKENS}                                     { popState(); return GENERIC_ID_PART; }
  [^]                                                          { popState(); yypushback(yylength()); }
}

<TYPE_ID> {
  {SIMPLE_ID}/{DOT}                                            { return SIMPLE_ID; }
  {SIMPLE_ID}                                                  { popState(); return SIMPLE_ID; }
  {DOT}                                                        { return DOT; }
  {WHITE_SPACE}                                                { return WHITE_SPACE; }
  [^]                                                          { popState(); yypushback(yylength()); }
}

<STRING_SUB_EXPRESSION> {
  "("                                                          { lParenCount++; return LP; }
  ")"                                                          { lParenCount--; if (lParenCount==0) popState(); return RP; }
}

<YYINITIAL> {
  "("                                                          { return LP; }
  ")"                                                          { return RP; }
}

<YYINITIAL, STRING_SUB_EXPRESSION> {
  {WHITE_SPACE}                                                { return WHITE_SPACE; }

  "begin"                                                      { return BEGIN; }
  "break"                                                      { return BREAK; }
  "catch"                                                      { return CATCH; }
  "class"                                                      { return CLASS; }
  "hidden"                                                     { return HIDDEN; }
  "static"                                                     { return STATIC; }
  "enum"                                                       { return ENUM; }
  "continue"                                                   { return CONTINUE; }
  "data"                                                       { return DATA; }
  "define"                                                     { return DEFINE; }
  "do"                                                         { return DO; }
  "dynamicparam"                                               { return DYNAMICPARAM; }
  "else"                                                       { return ELSE; }
  "elseif"                                                     { return ELSEIF; }
  "end"                                                        { return END; }
  "exit"                                                       { return EXIT; }
  "filter"/{WHITE_SPACE}                                       { pushState(FUNCTION_ID); return FILTER; }
  "finally"                                                    { return FINALLY; }
  "for"                                                        { return FOR; }
  "foreach"                                                    { return FOREACH; }
  "from"                                                       { return FROM; }
  "function"/{WHITE_SPACE}                                     { pushState(FUNCTION_ID); return FUNCTION; }
  "configuration"/{WHITE_SPACE}                                { pushState(FUNCTION_ID); return CONFIGURATION; }
  "if"                                                         { return IF; }
  "in"                                                         { return IN; }
  "inlinescript"                                               { return INLINESCRIPT; }
  "parallel"                                                   { return PARALLEL; }
  "param"                                                      { return PARAM; }
  "process"                                                    { return PROCESS; }
  "return"                                                     { return RETURN; }
  "switch"                                                     { return SWITCH; }
  "throw"                                                      { return THROW; }
  "trap"                                                       { return TRAP; }
  "try"                                                        { return TRY; }
  "until"                                                      { return UNTIL; }
  "using"                                                      { return USING; }
  "var"                                                        { return VAR; }
  "while"                                                      { return WHILE; }
  "workflow"/{WHITE_SPACE}                                     { pushState(FUNCTION_ID); return WORKFLOW; }
  "«"                                                          { return RAW_LBR; }
  "»"                                                          { return RAW_RBR; }
  ","                                                          { return COMMA; }
  ":"                                                          { return COLON; }
  "@"                                                          { return AT; }
  "|"                                                          { return PIPE; }
  "&"                                                          { return AMP; }
  "++"                                                         { return PP; }
  {EQ_DASH}                                                    { return EQ_DASH; }
  {EQ_PLUS}                                                    { return EQ_PLUS; }
  {EQ_STAR}                                                    { return EQ_STAR; }
  {EQ_DIV}                                                     { return EQ_DIV; }
  {EQ_PERS}                                                    { return EQ_PERS; }
  "+"                                                          { return PLUS; }
  {STAR}                                                       { return STAR; }
  {DIV}                                                        { return DIV; }
  {MM}                                                         { return MM; }
  {VERBATIM_ARG_START}                                         { pushState(VERBATIM_ARGUMENT); return VERBATIM_ARG_START; }
  "="                                                          { return EQ; }
  "\\"                                                         { return PATH_SEP; }

  {DASH}                           { return DASH; }
  {BRACED_VAR_START}               { pushState(VAR_BRACED); return BRACED_VAR_START; }
  {DS}                             { pushState(VAR_SIMPLE); return DS; }
  {LCURLY}                         { return LCURLY; }
  {RCURLY}                         { return RCURLY; }
  {DOT_DOT}                        { return DOT_DOT; }
  {DOT}                            { return DOT; }
  {SEMI}                           { return SEMI; }
  {COLON2}                         { return COLON2; }
  {PERS}                           { return PERS; }
  {SQBR_L}                         { return SQBR_L; }
  {SQBR_L}/{WHITE_SPACE}?{SIMPLE_ID}       { pushState(TYPE_ID);   return SQBR_L; }
  {SQBR_R}                         { return SQBR_R; }
  {OP_MR}                          { return OP_MR; }
  {OP_FR}/[^#]                     { return OP_FR; }
  {OP_C}/[^a-zA-Z]+{PARAMETER_CHAR}*       { return OP_C; }
//  {OP_C}/{PARAMETER_CHAR}*         { return OP_C; }
  {OP_BNOT}/[^a-zA-Z]+{PARAMETER_CHAR}*    { return OP_BNOT; }
  {OP_BAND}/[^a-zA-Z]+{PARAMETER_CHAR}*    { return OP_BAND; }
  {OP_BOR}/[^a-zA-Z]+{PARAMETER_CHAR}*     { return OP_BOR; }
  {OP_BXOR}/[^a-zA-Z]+{PARAMETER_CHAR}*    { return OP_BXOR; }
  {OP_NOT}/[^a-zA-Z]+{PARAMETER_CHAR}*     { return OP_NOT; }
  {OP_AND}/[^a-zA-Z]+{PARAMETER_CHAR}*     { return OP_AND; }
  {OP_OR}/[^a-zA-Z]+{PARAMETER_CHAR}*      { return OP_OR; }
  {OP_XOR}/[^a-zA-Z]+{PARAMETER_CHAR}*     { return OP_XOR; }
  {EXCL_MARK}                      { return EXCL_MARK; }
  {NLS}                            { return NLS; }
  {CH_DQ}                          { pushState(STRING); return DQ_OPEN; }
  {VERBATIM_STRING}                { return VERBATIM_STRING; }
  {EXPANDABLE_HERE_STRING_START}   { pushState(HERE_STRING); return EXPANDABLE_HERE_STRING_START; }
  {VERBATIM_HERE_STRING}           { return VERBATIM_HERE_STRING; }
  {REAL_NUM}                       { return REAL_NUM; }
  {HEX_INTEGER}                    { return HEX_INTEGER; }
  {DEC_INTEGER}/{DOT_DOT}?         { return DEC_INTEGER; }
  {DEC_EXPONENT}                   { return DEC_EXPONENT; }
  {SIMPLE_ID}                      { return SIMPLE_ID; }
//  {TYPE_NAME}                      { return TYPE_NAME; }
  {SINGLE_LINE_COMMENT}            { return COMMENT; }
  {DELIMITED_COMMENT}              { return COMMENT; }
  {GENERIC_ID_PART}                { return GENERIC_ID_PART; }
//  {AMP_ARG}                        { return AMP_ARG; }
//  {PARAM_ARGUMENT}                 { return PARAM_ARGUMENT; }
//  {ALNUM}                          { return ALNUM; }
  {LETTERS}                        { return LETTERS; }
  {CMD_PARAMETER}                    { return CMD_PARAMETER; }

}

[^] { return BAD_CHARACTER; }
