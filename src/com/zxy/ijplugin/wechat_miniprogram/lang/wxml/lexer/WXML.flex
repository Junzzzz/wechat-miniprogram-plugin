package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes;

%%

%{
    private int beforeCommentState = YYINITIAL;

    private void saveBeforeCommentState(){
        this.beforeCommentState = yystate();
    }

    private boolean badCharsetBacked = false;
%}

%unicode
%class _WXMLLexer
%public
%implements com.intellij.lexer.FlexLexer
%function advance
%type IElementType

// state
%state START_TAG_START
%state START_TAG_TAG_NAME
%state ELEMENT_CONTENT_START
%state ATTRIBUTE_START
%state ATTRIBUTE_VALUE_START
%state ATTRIBUTE_VALUE_STRING_DQ_STRAT
%state ATTRIBUTE_VALUE_STRING_SQ_STRAT
%state END_TAG_START
%state END_TAG_TAG_NAME
%state COMMENT
%state EXPR_START_SQ
%state EXPR_START_DQ
%state EXPR_START
%state STRING_DQ_START
%state STRING_SQ_START
%state DQ_STRING_DQ_START
%state SQ_STRING_DQ_START
%state DQ_STRING_SQ_START
%state SQ_STRING_SQ_START


// regex

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
WHITE_SPACE_AND_CRLF =     ({CRLF}|{WHITE_SPACE})+
DIGIT=[0-9]
TAG_NAME = [a-z_][a-z_-]*
ATTRIBUTE_NAME = ({ALPHA}|-|_|:)+

IDENTIFIER_START = {ALPHA}|"_"|"$"
IDENTIFIER = {IDENTIFIER_START} ({IDENTIFIER_START}|{DIGIT})*
NUMBER = {DIGIT}*\.{DIGIT}+ | {DIGIT}+ (\.{DIGIT}+)?

%%

<YYINITIAL> {
    "</" {yybegin(END_TAG_START);return WXMLTypes.END_TAG_START;}
    "<"  {yybegin(START_TAG_START);return WXMLTypes.START_TAG_START;}
    "{{" {yybegin(EXPR_START);return WXMLTypes.LEFT_DOUBLE_BRACE;}
    [^\ \n\t\f\R<"{{"][^<"{{"]* {return WXMLTypes.TEXT;}
}

<END_TAG_START> {TAG_NAME} {yybegin(END_TAG_TAG_NAME);return WXMLTypes.TAG_NAME;}

<END_TAG_TAG_NAME> {
    {WHITE_SPACE_AND_CRLF} {return TokenType.WHITE_SPACE;}
    ">" { yybegin(YYINITIAL); return WXMLTypes.END_TAG_END; }
}

<START_TAG_START> {TAG_NAME} { yybegin(START_TAG_TAG_NAME); return WXMLTypes.TAG_NAME; }

<START_TAG_TAG_NAME> {
    {WHITE_SPACE_AND_CRLF} { yybegin(ATTRIBUTE_START); return TokenType.WHITE_SPACE; }
}

<ATTRIBUTE_START> {
    {WHITE_SPACE_AND_CRLF} { return TokenType.WHITE_SPACE; }
    {ATTRIBUTE_NAME} { return WXMLTypes.ATTRIBUTE_NAME; }
    "=" { yybegin(ATTRIBUTE_VALUE_START); return WXMLTypes.EQ; }
}

<START_TAG_TAG_NAME,ATTRIBUTE_START> {
    ">" { yybegin(YYINITIAL); return WXMLTypes.START_TAG_END; }
    "/>" { yybegin(YYINITIAL); return WXMLTypes.EMPTY_ELEMENT_END; }
}

<ATTRIBUTE_VALUE_START> {
    {WHITE_SPACE_AND_CRLF} { return TokenType.WHITE_SPACE; }
    "\"" { yybegin(ATTRIBUTE_VALUE_STRING_DQ_STRAT); return WXMLTypes.STRING_START; }
    "'" { yybegin(ATTRIBUTE_VALUE_STRING_SQ_STRAT); return WXMLTypes.STRING_START; }
}

<ATTRIBUTE_VALUE_STRING_SQ_STRAT> {
    "'" { yybegin(START_TAG_TAG_NAME);return WXMLTypes.STRING_END; }
    "{{" { yybegin(EXPR_START_SQ); return WXMLTypes.LEFT_DOUBLE_BRACE; }
    ([^\R"{{"']|"\\'")+ { return WXMLTypes.STRING_CONTENT; }
}

<ATTRIBUTE_VALUE_STRING_DQ_STRAT> {
    "\"" { yybegin(START_TAG_TAG_NAME);return WXMLTypes.STRING_END; }
    "{{" { yybegin(EXPR_START_DQ); return WXMLTypes.LEFT_DOUBLE_BRACE;}
     ([^\R"{{"\"]|"\\\"")+ {
        return WXMLTypes.STRING_CONTENT;
     }
}

<EXPR_START_SQ> {
    "}}" { yybegin(ATTRIBUTE_VALUE_STRING_SQ_STRAT); return WXMLTypes.RIGHT_DOUBLE_BRACE;}
}

<EXPR_START_DQ> {
    "}}" { yybegin(ATTRIBUTE_VALUE_STRING_DQ_STRAT); return WXMLTypes.RIGHT_DOUBLE_BRACE;}
}

"<!--" {
    this.saveBeforeCommentState();
    yybegin(COMMENT);
    return WXMLTypes.COMMENT_START;
}

<COMMENT> {
    "-->" {
        yybegin(this.beforeCommentState);
        return WXMLTypes.COMMONT_END;
    }
    [^"-->"]+ {
        return WXMLTypes.COMMENT_CONTENT;
    }
}

<EXPR_START> {
    "}}" {
        yybegin(YYINITIAL);
        return WXMLTypes.RIGHT_DOUBLE_BRACE;
    }
}

// 双括号表达式的分词

<EXPR_START,EXPR_START_SQ,EXPR_START_DQ> {
    "+" { return WXMLTypes.PLUS; }
    "-" { return WXMLTypes.MINUS;}
    "*" { return WXMLTypes.MULTIPLY;}
    "/" { return WXMLTypes.DIVIDE;}
    "%" { return WXMLTypes.RESIDUAL;}
    {NUMBER} {return WXMLTypes.NUMBER;}
    "true" {return WXMLTypes.TRUE;}
    "false" {return WXMLTypes.FALSE;}
      "null" {return WXMLTypes.NULL;}
    "." {return WXMLTypes.DOT;}
    {IDENTIFIER} {return WXMLTypes.IDENTIFIER;}
    ":" {return WXMLTypes.COLON;}
    "," {return WXMLTypes.COMMA;}
    "(" {return WXMLTypes.LEFT_PARENTHESES;}
    ")" {return WXMLTypes.RIGHT_PARENTHESES;}
    "{" {return WXMLTypes.LEFT_BRACE;}
    "}" {return WXMLTypes.RIGHT_BRACE;}
    "[" {return WXMLTypes.LEFT_BRACKET;}
    "]" {return WXMLTypes.RIGHT_BRACKET;}
    "..." {return WXMLTypes.EXPAND_KEYWORD;}
    "?" {return WXMLTypes.QUESTION_MARK;}
    "!==" {return WXMLTypes.NOT_STRICT_EQ;}
    "!=" {return WXMLTypes.NOT_EQ;}
    "==" {return WXMLTypes.EQ;}
    "===" {return WXMLTypes.STRICT_EQ;}
    "!" {return WXMLTypes.EXCLAMATION_MARK;}
     {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
}

<EXPR_START> {
    "'" {yybegin(STRING_SQ_START);return WXMLTypes.STRING_START;}
    "\"" {yybegin(STRING_DQ_START);return WXMLTypes.STRING_START;}
}

<EXPR_START_SQ> {
        "'"|"\\'" {yybegin(SQ_STRING_SQ_START);return WXMLTypes.STRING_START;}
        "\""|"\\\"" {yybegin(SQ_STRING_DQ_START);return WXMLTypes.STRING_START;}
}

<EXPR_START_DQ> {
        "'"|"\\'" {yybegin(DQ_STRING_SQ_START);return WXMLTypes.STRING_START;}
        "\""|"\\\"" {yybegin(DQ_STRING_DQ_START);return WXMLTypes.STRING_START;}
}

<STRING_DQ_START> {
    [^\R\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }
    "\"" {
          yybegin(EXPR_START);
          return WXMLTypes.STRING_END;
      }
}

<STRING_SQ_START> {
    [^\R\']+ {
          return WXMLTypes.STRING_CONTENT;
    }
    "'" {
          yybegin(EXPR_START);
          return WXMLTypes.STRING_END;
    }
}

<SQ_STRING_DQ_START> {
    [^\R"\\\""\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }
    "\"" {
          yybegin(EXPR_START_SQ);
          return WXMLTypes.STRING_END;
    }
}

<SQ_STRING_SQ_START> {
    [^\R"\\'"\']+ {
          return WXMLTypes.STRING_CONTENT;
    }
    "\\'"|"'" {
          yybegin(EXPR_START_SQ);
          return WXMLTypes.STRING_END;
    }
}

<DQ_STRING_DQ_START> {
    [^\R"\\\""\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }
    "\"" {
          yybegin(EXPR_START_DQ);
          return WXMLTypes.STRING_END;
    }
}

<DQ_STRING_SQ_START> {
    [^\R"\\'"\']+ {
          return WXMLTypes.STRING_CONTENT;
    }
    "\\'"|"'" {
          yybegin(EXPR_START_DQ);
          return WXMLTypes.STRING_END;
    }
}

{WHITE_SPACE_AND_CRLF}                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^] {
          yybegin(YYINITIAL);
          if (this.badCharsetBacked){
            this.badCharsetBacked = false;
            return TokenType.BAD_CHARACTER ;
          }else {
            this.yypushback(yylength());
            this.badCharsetBacked = true;
          }

}