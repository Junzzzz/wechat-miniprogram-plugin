package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSAttachElementType;import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes;

%%

%{
    private int beforeCommentState = YYINITIAL;

    private void saveBeforeCommentState(){
        this.beforeCommentState = yystate();
    }

    private int beforeStringState = YYINITIAL;

    private void saveBeforeStringState(){
        this.beforeStringState = yystate();
    }
%}

%unicode

%class _WXSSLexer
%public
%implements com.intellij.lexer.FlexLexer
%function advance
%type IElementType

// state
%state IDENTIFIER
%state PERIOD
%state SELECTOR_GROUP
%state ID_SELECTOR
%state CLASS_SELECTOR
%state STYLE_SELCTION
%state ATTRIBUTE_START
%state ATTRIBUTE_VALUE_STRAT
%state ATTRIBUTE_VALUE
%state ATTRIBUTE_VALUE_END
%state ATTRIBUTE_VALUE_NUMBER
%state ATTRIBUTE_VALUE_STRING_START_DQ
%state ATTRIBUTE_VALUE_STRING_START_SQ
%state ATTRIBUTE_VALUE_FUNCTION
%state ATTRIBUTE_VALUE_FUNCTION_ARGS
%state ATTRIBUTE_VALUE_FUNCTION_ARG_NUMBER
%state COMMENT
%state STRING_START_DQ
%state STRING_START_SQ
%state KEYFRAMES_START

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
WHITE_SPACE_AND_CRLF =     ({CRLF}|{WHITE_SPACE})+
DIGIT=[0-9]

IDENTIFIER_START = [a-zA-Z]
IDENTIFIER = {IDENTIFIER_START}({IDENTIFIER_START}|{DIGIT}|"-"|"_")*

WHITE_SPACE_AND_CRLF =     ({CRLF}|{WHITE_SPACE})+
HASH = #([0-9a-fA-F]{3}|[0-9a-fA-F]{6})
NUMBER = "-"?({DIGIT}*\.{DIGIT}+ | {DIGIT}+(\.{DIGIT}+)?)
NUMBER_UNIT = {ALPHA}+ | %
COMMENT_START = "/*"
COMMENT_END = "*/"
UNICODE_RANGE = "U+"([0-9a-fA-F]{1,4}(-[0-9a-fA-F]{1,4})?|[0-9a-fA-F?]{1,4})
%%

"!important" { return WXSSTypes.IMPORTANT_KEYWORD;}
"@font-face" { return WXSSTypes.FONT_FACE_KEYWORD; }
"@keyframes" {  return WXSSTypes.KEYFRAMES_KEYWORD; }
"@import" {return WXSSTypes.IMPORT_KEYWORD;}
"@"{IDENTIFIER} {return WXSSAttachElementType.AT_KEYWORD;}
"}" { return WXSSTypes.RIGHT_BRACKET; }
"{" { return WXSSTypes.LEFT_BRACKET; }
"(" { return WXSSTypes.LEFT_PARENTHESES; }
")" { return WXSSTypes.RIGHT_PARENTHESES; }
"," {return WXSSTypes.COMMA;}
";" {return WXSSTypes.SEMICOLON;}
":" {return WXSSTypes.COLON;}
{NUMBER} { return WXSSTypes.NUMBER; }
{NUMBER_UNIT} { return WXSSTypes.IDENTIFIER;}
{HASH} { return WXSSTypes.HASH; }
"#" { return WXSSTypes.NUMBER_SIGN; }
"." { return WXSSTypes.DOT; }
(":"|"::")("before"|"after") { return WXSSTypes.PSEUDO_SELECTOR; }
{UNICODE_RANGE} { yybegin(ATTRIBUTE_VALUE_END);return WXSSTypes.UNICODE_RANGE; }

// string
<STRING_START_SQ> {
  "'" { yybegin(this.beforeStringState);return WXSSTypes.STRING_END_SQ; }
   ([^\n\']|"\\'")+ { return WXSSTypes.STRING_CONTENT; }
}
<STRING_START_DQ> {
  "\"" { yybegin(this.beforeStringState);return WXSSTypes.STRING_END_DQ; }
  ([^\n"\""]|"\\\"")+ { return WXSSTypes.STRING_CONTENT; }
}

"\"" { this.saveBeforeStringState();yybegin(STRING_START_DQ);return WXSSTypes.STRING_START_DQ; }
"'" { this.saveBeforeStringState();yybegin(STRING_START_SQ);return WXSSTypes.STRING_START_SQ; }

// 注释，记录进入注释之前的状态
// 再注释结束之后释放
{COMMENT_START} {
    this.saveBeforeCommentState();
    yybegin(COMMENT);
    return WXSSTypes.COMMENT;
}

<COMMENT> {
    {COMMENT_END} {
        yybegin(this.beforeCommentState);
        return WXSSTypes.COMMENT;
    }
    {WHITE_SPACE_AND_CRLF} {
          return TokenType.WHITE_SPACE;
      }
    [^] {
        return WXSSTypes.COMMENT;
    }
}

{IDENTIFIER} {return WXSSTypes.IDENTIFIER;}

{WHITE_SPACE_AND_CRLF} { return TokenType.WHITE_SPACE; }

[^] { return TokenType.BAD_CHARACTER; }