package com.intellij.plugin.powershell.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;

%%

%{
  public _PowerShellLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _PowerShellLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE_CHAR=[\ \t\f]|{BACKTICK}{NL}
BACKTICK="`"
WHITE_SPACE={WHITE_SPACE_CHAR}+//|{BACKTICK}{NL}{WHITE_SPACE_CHAR}*//|{CC}

OP_C=("-as"|"-ccontains"|"-ceq"|"-cge"|"-cgt"|"-cle"|"-clike"|"-clt"|"-cmatch"|"-cne"|"-cnotcontains"|"-cnotlike"|"-cnotmatch"|"-contains"
|"-creplace"|"-csplit"|"-eq"|"-ge"|"-gt"|"-icontains"|"-ieq"|"-ige"|"-igt"|"-ile"|"-ilike"|"-ilt"|"-imatch"|"-in"|"-ine"|"-inotcontains"|"-inotlike"
|"-inotmatch"|"-ireplace"|"-is"|"-isnot"|"-isplit"|"-join"|"-le"|"-like"|"-lt"|"-match"|"-ne"|"-notcontains"|"-notin"|"-notlike"|"-notmatch"
|"-replace"|"-shl"|"-shr"|"-split")
OP_MR=("*>&1"|"2>&1"|"3>&1"|"4>&1"|"5>&1"|"6>&1"|"*>&2"|"1>&2"|"3>&2"|"4>&2"|"5>&2"|"6>&2")
OP_FR=(">"|">>"|"2>"|"2>>3>"|"3>>4>"|"4>>"|"5>"|"5>>6>"|"6>>*>"|"*>>"|"<")
OP_NOT={DASH}"not"
OP_BNOT={DASH}"bnot"
OP_SPLIT={DASH}"split"
OP_JOIN={DASH}"join"
EXCL_MARK="!"

NL=(\r|\n|\r\n)
NLS={WHITE_SPACE}*{NL}({NL}|{WHITE_SPACE}*)*
CH_DQ=\"|\“|\”|\„
CH_SQ=\'|\‘|\’|\‚|\‛
VAR_SCOPE="global:" | "local:" | "private:" | "script:" | "using:" | "workflow:" | {SIMPLE_ID}":"
BRACED_VAR="${"{VAR_SCOPE}?{WHITE_SPACE}?{BRACED_ID}"}"
EXPAND_STRING_CHARS=([^\$\"\“\”\„\`\r\n]|{BRACED_VAR}|"$"[^\"\“\”\„\`\r\n]|"$"(\`.)|(\`.)|{CH_DQ}{CH_DQ})+//?subexpression'\(\)' in: '|"$"[^\(\)\"\“\”\„\`\r\n]|'
EXPANDABLE_STRING={CH_DQ}{EXPAND_STRING_CHARS}?"$"*{CH_DQ}

VERBATIM_STRING_CHARS=([^\'\‘\’\‚\‛\r\n]|{CH_SQ}{CH_SQ})+
VERBATIM_STRING={CH_SQ}{VERBATIM_STRING_CHARS}?{CH_SQ}

EXPANDABLE_HERE_STRING=@{CH_DQ}([ \t\n\x0B\f\r])*(\r|\n|\r\n)(([^\"\“\”\„\\]|\\.)+(\r|\n|\r\n))?([ \t\n\x0B\f\r])*{CH_DQ}@
VERBATIM_HERE_STRING=@{CH_SQ}([ \t\n\x0B\f\r])*(\r|\n|\r\n)(([^\'\‘\’\‚\‛\\]|\\.)+(\r|\n|\r\n))?([ \t\n\x0B\f\r])*{CH_SQ}@
DEC_DIGIT=[0-9]
HEX_DIGIT={DEC_DIGIT}|[abcdef]
DEC_DIGITS={DEC_DIGIT}+
HEX_DIGITS={HEX_DIGIT}{DEC_DIGIT}*

DEC_EXPONENT=[Ee][+-]?[0-9]+
DEC_SF=[dDlL]
NUM_MULTIPLIER=([kK]|[mM]|[gG]|[tT]|[pP])[bB]

REAL_NUM={DEC_DIGITS}"."{DEC_DIGITS}?{DEC_EXPONENT}?{DEC_SF}?{NUM_MULTIPLIER}?|"."{DEC_DIGITS}{DEC_EXPONENT}?{DEC_SF}?{NUM_MULTIPLIER}?|{DEC_DIGITS}{DEC_EXPONENT}{DEC_SF}?{NUM_MULTIPLIER}?
HEX_INTEGER=0x{HEX_DIGITS}[lL]?{NUM_MULTIPLIER}?
DEC_INTEGER={DEC_DIGITS}{DEC_SF}?{NUM_MULTIPLIER}?

DOT="."
SEMI=";"
COLON2="::"
PERS="%"
HASH="#"
SQBR_L="["
SQBR_R="]"

SIMPLE_ID_FIRST_CHAR=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_)
SIMPLE_ID_CHAR={SIMPLE_ID_FIRST_CHAR}|(\p{Nd}|\_|{HASH})
SIMPLE_ID={SIMPLE_ID_FIRST_CHAR}{SIMPLE_ID_CHAR}*

VAR_ID_CHAR={SIMPLE_ID_CHAR}|(\?)
VAR_ID={VAR_ID_CHAR}+


GENERIC_ID_PART_FIRST_CHAR=([^\\\.\=\[\]\%\-\–\—\―\}\{\(\)\,\;\"\'\|\&\$\s\n\r\#\:\`0-9!\+]|(`.))
GENERIC_ID_PART_CHAR={GENERIC_ID_PART_FIRST_CHAR}|([\+\-\–\—\―\%0-9!])
GENERIC_ID_PART={GENERIC_ID_PART_FIRST_CHAR}{GENERIC_ID_PART_CHAR}*

BRACED_ID_CHAR=([^\}\`]|(`.))
BRACED_ID={BRACED_ID_CHAR}+
TYPE_NAME=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nd}|\_)+
SINGLE_LINE_COMMENT_CHARS=[^\n\r]+
SINGLE_LINE_COMMENT={HASH}({SINGLE_LINE_COMMENT_CHARS}?|{EOL})//??todo: #\n
DELIMITED_COMENT_START=<{HASH}
DELIMITED_COMENT_END={HASH}+>
DELIMITED_COMMENT_CHARS=({HASH}*[^#>]+)+
DELIMITED_COMMENT={DELIMITED_COMENT_START}{DELIMITED_COMMENT_CHARS}?{DELIMITED_COMENT_END}

//AMP_ARG=\&/*[^&]*/[\w]
PARAM_ARGUMENT=([\w]([\w$0-9]|{DASH})*)
ALNUM=([:letter:]|[:digit:])+
LETTERS=[a-zA-Z]+
DASH=[\-\–\—\―]
MM="--"
PARAM_TOKEN={DASH}(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_|\?)[^\{\}\(\)\;\,\|\&\.\[\:\s\n\r]*:?
VERBATIM_ARG_START={MM}{PERS}
VERBATIM_ARG_INPUT=[^\|\r\n]+

%state VAR_START VAR_BRACED VERBATIM_ARGUMENT


%%
<YYINITIAL> {
"$"                                                            { yybegin(VAR_START); return DS; }
}
<VAR_START> {
  "{"                                                          { yybegin(VAR_BRACED); return LCURLY; }
  {SIMPLE_ID}                                                  { /*yybegin(YYINITIAL);*/ return SIMPLE_ID; }
  {VAR_ID}                                                     { /*yybegin(YYINITIAL);*/ return VAR_ID; }
  ":"                                                          { /*yybegin(YYINITIAL);*/ return COLON; }
  {COLON2}                                                     { yybegin(YYINITIAL); return COLON2; }
//  "}"                                                          { return RCURLY; }
  "("                                                          { yybegin(YYINITIAL); return LP; }
  ")"                                                          { yybegin(YYINITIAL); return RP; }
  {WHITE_SPACE}                                                { yybegin(YYINITIAL); return WHITE_SPACE; }//todo WS can be after the colon $<scope name>: simple_id
  {NLS}                                                        { yybegin(YYINITIAL); return NLS; }
  {SEMI}                                                       { yybegin(YYINITIAL); return SEMI; }
  {DOT}                                                        { yybegin(YYINITIAL); return DOT; }
  ","                                                          { yybegin(YYINITIAL); return COMMA; }
  {SQBR_L}                                                     { yybegin(YYINITIAL); return SQBR_L; }
  "++"                                                         { yybegin(YYINITIAL); return PP; }
  {MM}                                                         { yybegin(YYINITIAL); return MM; }
  "="                                                          { yybegin(YYINITIAL); return EQ; }
  "+"                                                          { yybegin(YYINITIAL); return PLUS; }
//  "-"                                                          { yybegin(YYINITIAL); return MINUS; }
  {DASH}                                                       { yybegin(YYINITIAL); return DASH; }
  {OP_NOT}                                                     { yybegin(YYINITIAL); return OP_NOT; }
  {OP_BNOT}                                                    { yybegin(YYINITIAL); return OP_BNOT; }
  {OP_SPLIT}                                                   { yybegin(YYINITIAL); return OP_SPLIT; }
  {OP_JOIN}                                                    { yybegin(YYINITIAL); return OP_JOIN; }
  {EXCL_MARK}                                                  { yybegin(YYINITIAL); return EXCL_MARK; }
}
<VAR_BRACED> {
  {BRACED_ID}                                                  { return BRACED_ID; }
//  "{"                                                          { yybegin(IN_BRACED_VAR) return LCURLY; }
  "}"                                                          { yybegin(YYINITIAL); return RCURLY; }
}

<VERBATIM_ARGUMENT> {
  {VERBATIM_ARG_INPUT}                                          { return VERBATIM_ARG_INPUT; }
  "|"                                                           { yybegin(YYINITIAL); return PIPE; }
  {NLS}                                                         { yybegin(YYINITIAL); return NLS; }
}

<YYINITIAL> {
  {WHITE_SPACE}                                                { return WHITE_SPACE; }

  "("                                                          { return LP; }
  ")"                                                          { return RP; }
  "begin"                                                      { return BEGIN; }
  "break"                                                      { return BREAK; }
  "catch"                                                      { return CATCH; }
  "class"                                                      { return CLASS; }
  "continue"                                                   { return CONTINUE; }
  "data"                                                       { return DATA; }
  "define"                                                     { return DEFINE; }
  "do"                                                         { return DO; }
  "dynamicparam"                                               { return DYNAMICPARAM; }
  "else"                                                       { return ELSE; }
  "elseif"                                                     { return ELSEIF; }
  "end"                                                        { return END; }
  "exit"                                                       { return EXIT; }
  "filter"                                                     { return FILTER; }
  "finally"                                                    { return FINALLY; }
  "for"                                                        { return FOR; }
  "foreach"                                                    { return FOREACH; }
  "from"                                                       { return FROM; }
  "function"                                                   { return FUNCTION; }
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
  "workflow"                                                   { return WORKFLOW; }
  "«"                                                          { return RAW_LBR; }
  "»"                                                          { return RAW_RBR; }
  "(*"                                                         { return MULTI_LINE_COMMENT_START; }
  "*)"                                                         { return MULTI_LINE_COMMENT_END; }
  ","                                                          { return COMMA; }
  "{"                                                          { return LCURLY; }
  "}"                                                          { return RCURLY; }
  ":"                                                          { return COLON; }
  "@"                                                          { return AT; }
  "="                                                          { return EQ; }
  "|"                                                          { return PIPE; }
  "&"                                                          { return AMP; }
  "++"                                                         { return PP; }
  {MM}                                                         { return MM; }
  {VERBATIM_ARG_START}                                         { yybegin(VERBATIM_ARGUMENT); return VERBATIM_ARG_START; }
  "+"                                                          { return PLUS; }
  "\\"                                                         { return PATH_SEP; }

  {DASH}                           { return DASH; }
  {DOT}                            { return DOT; }
  {SEMI}                           { return SEMI; }
  {COLON2}                         { return COLON2; }
  {PERS}                           { return PERS; }
//  {HASH}                           { return HASH; }
  {SQBR_L}                         { return SQBR_L; }
  {SQBR_R}                         { return SQBR_R; }
  {OP_MR}                          { return OP_MR; }
  {OP_FR}                          { return OP_FR; }
  {OP_NOT}                         { return OP_NOT; }
  {OP_BNOT}                        { return OP_BNOT; }
  {OP_SPLIT}                       { return OP_SPLIT; }
  {OP_JOIN}                        { return OP_JOIN; }
  {OP_C}                           { return OP_C; }
  {EXCL_MARK}                      { return EXCL_MARK; }
  {NLS}                            { return NLS; }
  {EXPANDABLE_STRING}              { return EXPANDABLE_STRING; }
  {VERBATIM_STRING}                { return VERBATIM_STRING; }
  {EXPANDABLE_HERE_STRING}         { return EXPANDABLE_HERE_STRING; }
  {VERBATIM_HERE_STRING}           { return VERBATIM_HERE_STRING; }
  {REAL_NUM}                       { return REAL_NUM; }
  {HEX_INTEGER}                    { return HEX_INTEGER; }
  {DEC_INTEGER}                    { return DEC_INTEGER; }
  {DEC_EXPONENT}                   { return DEC_EXPONENT; }
  {SIMPLE_ID}                      { return SIMPLE_ID; }
  {GENERIC_ID_PART}                { return GENERIC_ID_PART; }
  {TYPE_NAME}                      { return TYPE_NAME; }
  {SINGLE_LINE_COMMENT}            { return COMMENT; }
  {DELIMITED_COMMENT}              { return COMMENT; }
//  {AMP_ARG}                        { return AMP_ARG; }
  {PARAM_ARGUMENT}                 { return PARAM_ARGUMENT; }
  {ALNUM}                          { return ALNUM; }
  {LETTERS}                        { return LETTERS; }
  {PARAM_TOKEN}                    { return PARAM_TOKEN; }

}

[^] { return BAD_CHARACTER; }
