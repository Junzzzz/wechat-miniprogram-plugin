package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes;

%%

%{
    private int beforeCommentState = YYINITIAL;

    private void saveBeforeCommentState(){
        this.beforeCommentState = yystate();
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
%state IMPORT_STRING_START_DQ
%state IMPORT_STRING_START_SQ
%state IMPORT_STRING_END
%state IMPORT_VALUE_START
%state SELECTOR_GROUP
%state ID_SELECTOR
%state CLASS_SELECTOR
%state STYLE_SELCTION
%state ATTRIBUTE_START
%state ATTRIBUTE_VALUE_STRAT
%state ATTRIBUTE_VALUE_NUMBER
%state ATTRIBUTE_VALUE_STRING_START_DQ
%state ATTRIBUTE_VALUE_STRING_START_SQ
%state ATTRIBUTE_VALUE_FUNCTION
%state ATTRIBUTE_VALUE_FUNCTION_ARGS
%state ATTRIBUTE_VALUE_FUNCTION_ARG_NUMBER
%state COMMENT

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
DIGIT=[0-9]
STRING_CONTENT = ({ALPHA}|{DIGIT}|"_"|":"|"."|"-"|"\\"|"/")*

IDENTIFIER_START = {ALPHA}|"-"|"_"
IDENTIFIER = {IDENTIFIER_START}({IDENTIFIER_START}|{DIGIT})*

ATTRIBUTE_NAME = {ALPHA}({ALPHA}|-)*
WHITE_SPACE_AND_CRLF =     ({CRLF}|{WHITE_SPACE})+
HASH = #([0-9a-fA-F]{3}|[0-9a-fA-F]{6})
NUMBER = {DIGIT}*.{DIGIT}+ | {DIGIT}+ (.{DIGIT}+)?
NUMBER_UNIT = {ALPHA}+
NUMBER_WITH_UNIT = {NUMBER}{NUMBER_UNIT}
FUNCTION_NAME = {IDENTIFIER}
ELEMENT_NAME = ({ALPHA}|-)+
COMMENT_START = "/*"
COMMENT_END = "*/"
%%

<YYINITIAL> "@import" {
    yybegin(IMPORT_VALUE_START);
    return WXSSTypes.IMPORT_KEYWORD;
}

<IMPORT_VALUE_START> {
    {WHITE_SPACE} {
        return TokenType.WHITE_SPACE;
    }

    "\"" {
                yybegin(IMPORT_STRING_START_DQ);
                return WXSSTypes.STRING_START_DQ;
            }
    "'" {
                yybegin(IMPORT_STRING_START_SQ);
                return WXSSTypes.STRING_START_SQ;
            }
}

<IMPORT_STRING_START_SQ,IMPORT_STRING_START_DQ>{
    {STRING_CONTENT} {
        return WXSSTypes.STRING_CONTENT;
    }
}

<IMPORT_STRING_START_DQ>  {
    "\"" {
                yybegin(IMPORT_STRING_END);
                return WXSSTypes.STRING_END_DQ;
        }
}

<IMPORT_STRING_START_SQ>  {
    "'" {
                yybegin(IMPORT_STRING_END);
                return WXSSTypes.STRING_END_SQ;
        }
}

<IMPORT_STRING_END> ";" {
          yybegin(YYINITIAL);
    return WXSSTypes.SEMICOLON;
}

// 选择器

<YYINITIAL> "#"|"."|{ELEMENT_NAME} {
    yypushback(yylength());
    yybegin(SELECTOR_GROUP);
}

<SELECTOR_GROUP> {
    {ELEMENT_NAME} {
          return WXSSTypes.ELEMENT_NAME;
      }
    "#" {
          yybegin(ID_SELECTOR);
        return WXSSTypes.NUMBER_SIGN;
    }
    "." {
          yybegin(CLASS_SELECTOR);
          return WXSSTypes.DOT;
      }
    (":"|"::")("before"|"after") {
          return WXSSTypes.PSEUDO_SELECTOR;
      }
      {WHITE_SPACE_AND_CRLF} {
                        return TokenType.WHITE_SPACE;
                    }
}

<CLASS_SELECTOR> {IDENTIFIER} {
           yybegin(SELECTOR_GROUP);
                  return WXSSTypes.CLASS;
      }

<ID_SELECTOR> {
    {IDENTIFIER} {
          yybegin(SELECTOR_GROUP);
        return WXSSTypes.ID;
      }
}

<ID_SELECTOR,CLASS_SELECTOR,SELECTOR_GROUP>{
    "," {
          yybegin(SELECTOR_GROUP);
          return WXSSTypes.COMMA;

      }
      {WHITE_SPACE} {
        yybegin(SELECTOR_GROUP);
          return TokenType.WHITE_SPACE;
      }
            "{" {
                yybegin(STYLE_SELCTION);
                yypushback(yylength());
            }
}

<STYLE_SELCTION>{
    "{" {
        return WXSSTypes.LEFT_BRACKET;
    }
    {WHITE_SPACE_AND_CRLF} {
          return TokenType.WHITE_SPACE;
      }
    {ATTRIBUTE_NAME} {
          yybegin(ATTRIBUTE_START);
        return WXSSTypes.ATTRIBUTE_NAME;
      }
    "}" {
          yybegin(YYINITIAL);
        return WXSSTypes.RIGHT_BRACKET;
      }
}

<ATTRIBUTE_START>{
    ":" {
          yybegin(ATTRIBUTE_VALUE_STRAT);
          return WXSSTypes.COLON;
      }

      {WHITE_SPACE_AND_CRLF} {
            return TokenType.WHITE_SPACE;
        }
}

<ATTRIBUTE_VALUE_STRAT>{
      ";" {
          yybegin(STYLE_SELCTION);
          return WXSSTypes.SEMICOLON;
      }
      "," {
          return WXSSTypes.COMMA;
      }
      {HASH} {
          return WXSSTypes.HASH;
      }
      {WHITE_SPACE_AND_CRLF} {
          return TokenType.WHITE_SPACE;
      }
      {NUMBER}|{NUMBER_WITH_UNIT} {
          yypushback(yylength());
          yybegin(ATTRIBUTE_VALUE_NUMBER);
      }
      "'" {
          yybegin(ATTRIBUTE_VALUE_STRING_START_DQ);
          return WXSSTypes.STRING_START_DQ;
      }
      "\"" {
          yybegin(ATTRIBUTE_VALUE_STRING_START_SQ);
          return WXSSTypes.STRING_START_SQ;
      }
      {FUNCTION_NAME}{WHITE_SPACE_AND_CRLF}?"(" {
                yypushback(yylength());
                yybegin(ATTRIBUTE_VALUE_FUNCTION);
            }
       {IDENTIFIER} {
          return WXSSTypes.ATTRIBUTE_VALUE_LITERAL;
      }
}

// 属性值中的数字

<ATTRIBUTE_VALUE_NUMBER> {
    {NUMBER} {
        return WXSSTypes.NUMBER;
    }
    {WHITE_SPACE_AND_CRLF} {
            yybegin(ATTRIBUTE_VALUE_STRAT);
            return TokenType.WHITE_SPACE;
    }
    "," {
          yybegin(ATTRIBUTE_VALUE_STRAT);
          return WXSSTypes.COMMA;
      }
    {NUMBER_UNIT} {
          return WXSSTypes.NUMBER_UNIT;
    }
    ";" {
        yybegin(STYLE_SELCTION);
        return WXSSTypes.SEMICOLON;
    }
}

// 属性值中的字符串

<ATTRIBUTE_VALUE_STRING_START_DQ> {
    "'" {
          yybegin(ATTRIBUTE_VALUE_STRAT);
          return WXSSTypes.STRING_END_DQ;
    }
}

<ATTRIBUTE_VALUE_STRING_START_SQ> "'" {
    yybegin(ATTRIBUTE_VALUE_STRAT);
    return WXSSTypes.STRING_END_SQ;
}

<ATTRIBUTE_VALUE_STRING_START_DQ,ATTRIBUTE_VALUE_STRING_START_SQ> {STRING_CONTENT} {
      return WXSSTypes.STRING_CONTENT;
}

// 属性值中的方法调用
<ATTRIBUTE_VALUE_FUNCTION> {
    {FUNCTION_NAME} {
          return WXSSTypes.FUNCTION_NAME;
      }
     "(" {
          yybegin(ATTRIBUTE_VALUE_FUNCTION_ARGS);
          return WXSSTypes.LEFT_PARENTHESES;
      }
}

<ATTRIBUTE_VALUE_FUNCTION_ARGS>{
    "," {
          return WXSSTypes.COMMA;
      }
      {WHITE_SPACE_AND_CRLF} {
          return TokenType.WHITE_SPACE;
      }
    {IDENTIFIER} {
        return WXSSTypes.ATTRIBUTE_VALUE_LITERAL;
    }
    {NUMBER}|{NUMBER_WITH_UNIT} {
        yypushback(yylength());
        yybegin(ATTRIBUTE_VALUE_FUNCTION_ARG_NUMBER);
    }
      {HASH} {
          return WXSSTypes.HASH;
      }
     ")" {
          yybegin(ATTRIBUTE_VALUE_STRAT);
          return WXSSTypes.RIGHT_PARENTHESES;
      }
}

// 方法中的数字
<ATTRIBUTE_VALUE_FUNCTION_ARG_NUMBER>{
        {NUMBER} {
            return WXSSTypes.NUMBER;
        }
        {WHITE_SPACE_AND_CRLF} {
                yybegin(ATTRIBUTE_VALUE_FUNCTION_ARGS);
                return TokenType.WHITE_SPACE;
        }
        {NUMBER_UNIT} {
              return WXSSTypes.NUMBER_UNIT;
        }
        "," {
          yybegin(ATTRIBUTE_VALUE_FUNCTION_ARGS);
            return WXSSTypes.COMMA;
        }
        ")" {
            yybegin(ATTRIBUTE_VALUE_STRAT);
            return WXSSTypes.RIGHT_PARENTHESES;
        }
}

{WHITE_SPACE_AND_CRLF}                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

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
          return WXSSTy
      }
    [^] {
        return WXSSTypes.COMMENT;
    }
    <<EOF>> {
        return WXSSTypes.COMMENT;
    }
}

[^] { return TokenType.BAD_CHARACTER; }