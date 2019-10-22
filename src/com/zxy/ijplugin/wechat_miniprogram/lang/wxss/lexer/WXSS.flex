package com.zxy.ijplugin.wechat_miniprogram.lang.wxss;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTokenTypes;
import com.intellij.lexer.FlexLexer;
%%

%unicode

%class _WXSSLexer
%public
%implements FlexLexer
%function advance
%type IElementType

// state
%state IDENTIFIER
%state PERIOD
%state STRING
%state STRING_START_DQ
%state STRING_START_SQ
%state STRING_END
%state IMPORT_VALUE_START

ALPHA=[:letter:]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
DIGIT=[0-9]
STRING_CONTENT = ({ALPHA}|"_"|":")({ALPHA}|{DIGIT}|"_"|":"|"."|"-")*
%%

<YYINITIAL> "@import" {
    yybegin(IMPORT_VALUE_START);
    return WXSSTokenTypes.WXSS_IMPORT;
}


<IMPORT_VALUE_START> {
"s" {
          return TokenType.WHITE_SPACE;
      }
"\"" {
  yybegin(STRING_START_DQ);
  return WXSSTokenTypes.WXSS_STRING_START;
}
}

<IMPORT_VALUE_START> "'" {
  yybegin(STRING_START_SQ);
  return WXSSTokenTypes.WXSS_STRING_START;
}

<STRING_START_DQ>  {
    "\"" {
            yybegin(STRING_END);
            return WXSSTokenTypes.WXSS_STRING_END;
        }
      {STRING_CONTENT} {
          return WXSSTokenTypes.WXSS_STRING_CONTENT;
      }
    [^] {
        return TokenType.BAD_CHARACTER;
    }
}

[^] { return TokenType.BAD_CHARACTER; }
