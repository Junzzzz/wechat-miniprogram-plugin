package com.zxy.ijplugin.wechat_miniprogram.lang.wxss;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSElementTypes;
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

CRLF=\R
WHITE_SPACE=[\ \n\t\f]

%%

<YYINITIAL> "@import" {
    yybegin(IMPORT_VALUE_START);
    return WXSSElementTypes.WXSS_IMPORT;
}


<IMPORT_VALUE_START> {
"s" {
          return TokenType.WHITE_SPACE;
      }
"\"" {
  yybegin(STRING_START_DQ);
  return WXSSElementTypes.WXSS_STRING_START;
}
}

<IMPORT_VALUE_START> "'" {
  yybegin(STRING_START_SQ);
  return WXSSElementTypes.WXSS_STRING_START;
}

<STRING_START_DQ>  {
    "\"" {
        yybegin(STRING_END);
        return WXSSElementTypes.WXSS_STRING_END;
    }
    [^] {
        return TokenType.BAD_CHARACTER;
    }
}

[^] { return TokenType.BAD_CHARACTER; }
