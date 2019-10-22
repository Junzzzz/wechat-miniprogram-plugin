package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes;
%%

%unicode

%class _WXSSLexer
%public
%implements com.intellij.lexer.FlexLexer
%function advance
%type IElementType

// state
%state IDENTIFIER
%state PERIOD
%state IMPORT_STRING
%state IMPORT_STRING_START_DQ
%state IMPORT_STRING_START_SQ
%state IMPORT_STRING_END
%state IMPORT_VALUE_START

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
DIGIT=[0-9]
STRING_CONTENT = ({ALPHA}|{DIGIT}|"_"|":"|"."|"-"|"\\"|"/")*
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


[^] { return TokenType.BAD_CHARACTER; }
