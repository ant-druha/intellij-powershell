package com.intellij.plugin.powershell;

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
WHITE_SPACE_CHAR=[\ \t\f]
WHITE_SPACE={WHITE_SPACE_CHAR}+

OP_C=("-as"|"-ccontains"|"-ceq"|"-cge"|"-cgt"|"-cle"|"-clike"|"-clt"|"-cmatch"|"-cne"|"-cnotcontains"|"-cnotlike"|"-cnotmatch"|"-contains"
|"-creplace"|"-csplit"|"-eq"|"-ge"|"-gt"|"-icontains"|"-ieq"|"-ige"|"-igt"|"-ile"|"-ilike"|"-ilt"|"-imatch"|"-in"|"-ine"|"-inotcontains"|"-inotlike"
|"-inotmatch"|"-ireplace"|"-is"|"-isnot"|"-isplit"|"-join"|"-le"|"-like"|"-lt"|"-match"|"-ne"|"-notcontains"|"-notin"|"-notlike"|"-notmatch"
|"-replace"|"-shl"|"-shr"|"-split")
OP_MR=("*>&1"|"2>&1"|"3>&1"|"4>&1"|"5>&1"|"6>&1"|"*>&2"|"1>&2"|"3>&2"|"4>&2"|"5>&2"|"6>&2")
OP_FR=(">"|">>"|"2>"|"2>>3>"|"3>>4>"|"4>>"|"5>"|"5>>6>"|"6>>*>"|"*>>"|"<")

NL=(\r|\n|\r\n)
NLS={WHITE_SPACE}*{NL}({NL}|{WHITE_SPACE}*)*
STRING_DQ=\"([^\"\\]|\\.)*\"
STRING_SQ='[^']*'
EXPANDABLE_HERE_STRING=@\"([ \t\n\x0B\f\r])*(\r|\n|\r\n)(([^\"\\]|\\.)+(\r|\n|\r\n))?([ \t\n\x0B\f\r])*\"@
VERBATIM_HERE_STRING=@'([ \t\n\x0B\f\r])*(\r|\n|\r\n)(([^'\\]|\\.)+(\r|\n|\r\n))?([ \t\n\x0B\f\r])*'@
DIGITS=[0-9]+
DEC_EXPONENT=[Ee][+-]?[0-9]+
DOT="."
SEMI=";"
PERS="%"
HASH="#"
SQBR_L="["
SQBR_R="]"
//simple-name-first-char:
//A Unicode character of classes Lu, Ll, Lt, Lm, or Lo
//_   (The underscore character U+005F)
//simple-name-char:
//A Unicode character of classes Lu, Ll, Lt, Lm, Lo, or Nd
//_   (The underscore character U+005F)

SIMPLE_ID_FIRST_CHAR=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_)
SIMPLE_ID_CHAR={SIMPLE_ID_FIRST_CHAR}|(\p{Nd}|\_)
SIMPLE_ID={SIMPLE_ID_FIRST_CHAR}{SIMPLE_ID_CHAR}*

//variable-character:
//  A Unicode character of classes Lu, Ll, Lt, Lm, Lo, or Nd
//  _   (The underscore character U+005F)
//  ?
VAR_ID_CHAR={SIMPLE_ID_CHAR}|(\?)
VAR_ID={VAR_ID_CHAR}+


//GENERIC_ID_CHAR=([^\}\{\(\)\,\;\"\'\`\|\&\$\s\n\r]|(`.))
//GENERIC_ID_CHAR={SIMPLE_ID_CHAR}|(\-|\?)
GENERIC_ID_FIRST_CHAR=([^\=\[\]\%\-\}\{\(\)\,\;\"\'\|\&\$\s\n\r\#]|(`.))
GENERIC_ID_CHAR={GENERIC_ID_FIRST_CHAR}|(\-|\%)
GENERIC_ID={GENERIC_ID_FIRST_CHAR}{GENERIC_ID_CHAR}*//[int] -> int] - generic_id

BRACED_ID_CHAR=([^\}\`]|(`.))
BRACED_ID={BRACED_ID_CHAR}+
//GENERIC_TOKEN_CHARS=([^\}\{\(\)\,\;\"\`\|\&\$\s]|(`.))+
//GENERIC_ID=([^\}\{\(\)\,\;\"\`\|\&\$\s]|(`.))+
TYPE_NAME=(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nd}|\_)+
SINGLE_LINE_COMMENT_CHARS=[^\n\r]+
SINGLE_LINE_COMMENT={HASH}({SINGLE_LINE_COMMENT_CHARS}?|{EOL})
DELIMITED_COMENT_START=<{HASH}
DELIMITED_COMENT_END={HASH}+>
DELIMITED_COMMENT_CHARS=({HASH}*[^#>]+)+
DELIMITED_COMMENT={DELIMITED_COMENT_START}{DELIMITED_COMMENT_CHARS}?{DELIMITED_COMENT_END}

AMP_ARG=\&[^&]
PARAM_ARGUMENT=([\w][\w$0-9\-]*)
ALNUM=([:letter:]|[:digit:])+
LETTERS=[a-zA-Z]+
PARAM_TOKEN=\-(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\_|\?)[^\{\}\(\)\;\,\|\&\.\[\:\s\n\r]+:?

%state VAR_START VAR_BRACED


%%
<YYINITIAL> {
"$"                                                            { yybegin(VAR_START); return DS; }
}
<VAR_START> {
  "{"                                                          { yybegin(VAR_BRACED); return LCURLY; }
  {SIMPLE_ID}                                                  { /*yybegin(YYINITIAL);*/ return SIMPLE_ID; }
  {VAR_ID}                                                     { /*yybegin(YYINITIAL);*/ return VAR_ID; }
  ":"                                                          { /*yybegin(YYINITIAL);*/ return COLON; }
//  "}"                                                          { return RCURLY; }
  "("                                                          { yybegin(YYINITIAL); return LP; }
  ")"                                                          { yybegin(YYINITIAL); return RP; }
  {WHITE_SPACE}                                                { yybegin(YYINITIAL); return WHITE_SPACE; }//todo WS can be after the colon $<scope name>: simple_id
  {NLS}                                                        { yybegin(YYINITIAL); return NLS; }
  {SEMI}                                                       { yybegin(YYINITIAL); return SEMI; }
  {DOT}                                                        { /*yybegin(YYINITIAL);*/ return DOT; }
  ","                                                          { yybegin(YYINITIAL); return COMMA; }
}
<VAR_BRACED> {
  {BRACED_ID}                                                  { return BRACED_ID; }
//  "{"                                                          { yybegin(IN_BRACED_VAR) return LCURLY; }
  "}"                                                          { yybegin(YYINITIAL); return RCURLY; }
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
  "--"                                                         { return MM; }

  {DOT}                            { return DOT; }
  {SEMI}                           { return SEMI; }
  {PERS}                           { return PERS; }
//  {HASH}                           { return HASH; }
  {SQBR_L}                         { return SQBR_L; }
  {SQBR_R}                         { return SQBR_R; }
  {OP_C}                           { return OP_C; }
  {OP_MR}                          { return OP_MR; }
  {OP_FR}                          { return OP_FR; }
  {NLS}                            { return NLS; }
  {STRING_DQ}                      { return STRING_DQ; }
  {STRING_SQ}                      { return STRING_SQ; }
  {EXPANDABLE_HERE_STRING}         { return EXPANDABLE_HERE_STRING; }
  {VERBATIM_HERE_STRING}           { return VERBATIM_HERE_STRING; }
  {DIGITS}                         { return DIGITS; }
  {DEC_EXPONENT}                   { return DEC_EXPONENT; }
  {SIMPLE_ID}                      { return SIMPLE_ID; }
  {GENERIC_ID}                     { return GENERIC_ID; }
  {TYPE_NAME}                      { return TYPE_NAME; }
//  {SINGLE_LINE_COMMENT}             { return SINGLE_LINE_COMMENT; }
  {SINGLE_LINE_COMMENT}            { return COMMENT; }
  {DELIMITED_COMMENT}              { return COMMENT; }
  {AMP_ARG}                        { return AMP_ARG; }
  {PARAM_ARGUMENT}                 { return PARAM_ARGUMENT; }
  {ALNUM}                          { return ALNUM; }
  {LETTERS}                        { return LETTERS; }
  {PARAM_TOKEN}                    { return PARAM_TOKEN; }

}

[^] { return BAD_CHARACTER; }
