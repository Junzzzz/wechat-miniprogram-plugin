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

// regex

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
WHITE_SPACE_AND_CRLF =     ({CRLF}|{WHITE_SPACE})+

TAG_NAME = [a-z_][a-z_-]*
ATTRIBUTE_NAME = ({ALPHA}|-|_|:)+

%%

<YYINITIAL> {
    "</" {yybegin(END_TAG_START);return WXMLTypes.END_TAG_START;}
    "<"  {yybegin(START_TAG_START);return WXMLTypes.START_TAG_START;}
    [^\ \n\t\f\R<][^<]+ {return WXMLTypes.TEXT;}
}

<END_TAG_START> {TAG_NAME} {yybegin(END_TAG_TAG_NAME);return WXMLTypes.TAG_NAME;}

<END_TAG_TAG_NAME> {
    {WHITE_SPACE_AND_CRLF} {return TokenType.WHITE_SPACE;}
    ">" { yybegin(YYINITIAL); return WXMLTypes.END_TAG_END; }
}

<START_TAG_START> {TAG_NAME} { yybegin(START_TAG_TAG_NAME); return WXMLTypes.TAG_NAME; }

<START_TAG_TAG_NAME> {
    {ATTRIBUTE_NAME} { yybegin(ATTRIBUTE_START);return WXMLTypes.ATTRIBUTE_NAME; }
    {WHITE_SPACE_AND_CRLF} { return TokenType.WHITE_SPACE; }
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
    ([^\R'"}}"]|"\\'")+ { return WXMLTypes.EXPR;}
}

<EXPR_START_DQ> {
    "}}" { yybegin(ATTRIBUTE_VALUE_STRING_DQ_STRAT); return WXMLTypes.RIGHT_DOUBLE_BRACE;}
    ([^\R'"}}"]|"\\\"")+ { return WXMLTypes.EXPR;}
}

"<!--" {
    this.saveBeforeCommentState();
    yybegin(COMMENT);
    return WXMLTypes.COMMENT;
}

<COMMENT> {
    "-->" {
        yybegin(this.beforeCommentState);
        return WXMLTypes.COMMENT;
    }
    {WHITE_SPACE_AND_CRLF} {
          return TokenType.WHITE_SPACE;
    }
    [^] {
        return WXMLTypes.COMMENT;
    }
}

// TODO https://github.com/joewalnes/idea-community/blob/master/plugins/groovy/src/org/jetbrains/plugins/groovy/lang/groovydoc/parser/GroovyDocElementTypes.java

{WHITE_SPACE_AND_CRLF}                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^] { return TokenType.BAD_CHARACTER ; }