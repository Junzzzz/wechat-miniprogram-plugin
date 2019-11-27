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

%%
<YYINITIAL> {
    "</" {yybegin(END_TAG_START);return WXMLTypes.END_TAG_START;}
    "<"  {yybegin(START_TAG_START);return WXMLTypes.START_TAG_START;}
    [^"<"\n\t\f\ ][^<]*[^"<"\n\t\f\ ]? {
        return WXMLTypes.CONTENT;
    }
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
    ([^\r\n']|"\\'")+ { return WXMLTypes.STRING_CONTENT; }
}

<ATTRIBUTE_VALUE_STRING_DQ_STRAT> {
    "\"" { yybegin(START_TAG_TAG_NAME);return WXMLTypes.STRING_END; }
     ([^\r\n\"]|"\\\"")+ {
        return WXMLTypes.STRING_CONTENT;
     }
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



<STRING_DQ_START> {
    [^\r\n\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }

}

<STRING_SQ_START> {
    [^\r\n\']+ {
          return WXMLTypes.STRING_CONTENT;
    }
}

<SQ_STRING_DQ_START> {
    [^\r\n"\\\""\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }
}

<SQ_STRING_SQ_START> {
    [^\r\n"\\'"\']+ {
          return WXMLTypes.STRING_CONTENT;
    }
}

<DQ_STRING_DQ_START> {
    [^\r\n"\\\""\"]+ {
        return WXMLTypes.STRING_CONTENT;
    }
}

<DQ_STRING_SQ_START> {
    [^\r\n"\\'"\']+ {
          return WXMLTypes.STRING_CONTENT;
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