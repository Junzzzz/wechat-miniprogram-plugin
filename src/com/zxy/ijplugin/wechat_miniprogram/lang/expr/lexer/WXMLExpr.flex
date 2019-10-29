package com.zxy.ijplugin.wechat_miniprogram.lang.expr.lexer;
import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.psi.WXMLExprTypes;
%%

%unicode

%class _WXMLExprLexer
%public
%implements com.intellij.lexer.FlexLexer
%function advance
%type IElementType
//%ctorarg ExprWrapType exprWrapType

//%{
//    private ExprWrapType wrapType;
//%}
//
//%init{
//    this.wrapType = exprWrapType;
//%init}
// state
%state STRING_DQ_START
%state STRING_SQ_START

// regex
ALPHA=[:letter:]
WHITE_SPACE=[\ \n\t\f]
DIGIT=[0-9]
NUMBER = {DIGIT}*.{DIGIT}+ | {DIGIT}+ (.{DIGIT}+)?
IDENTIFIER_START = {ALPHA}|"_"|"$"
IDENTIFIER = {IDENTIFIER_START} ({IDENTIFIER_START}|{DIGIT})*
%%

<YYINITIAL> {
    "+" { return WXMLExprTypes.PLUS; }
    "-" { return WXMLExprTypes.MINUS;}
    "*" { return WXMLExprTypes.MULTIPLY;}
    "/" { return WXMLExprTypes.DIVIDE;}
    "%" { return WXMLExprTypes.RESIDUAL;}
    {NUMBER} {return WXMLExprTypes.NUMBER;}
    "true" {return WXMLExprTypes.TRUE;}
    "false" {return WXMLExprTypes.FALSE;}
    "." {return WXMLExprTypes.DOT;}
    {IDENTIFIER} {return WXMLExprTypes.IDENTIFIER;}
    ":" {return WXMLExprTypes.COLON;}
    "," {return WXMLExprTypes.COMMA;}
    "(" {return WXMLExprTypes.LEFT_PARENTHESES;}
    ")" {return WXMLExprTypes.RIGHT_PARENTHESES;}
    "{" {return WXMLExprTypes.LEFT_BRACE;}
    "}" {return WXMLExprTypes.RIGHT_BRACE;}
    "[" {return WXMLExprTypes.LEFT_BRACKET;}
    "]" {return WXMLExprTypes.RIGHT_BRACKET;}
    "..." {return WXMLExprTypes.EXPAND_KEYWORD;}
    "\\'""'" {yybegin(STRING_DQ_START);return WXMLExprTypes.STRING_START;}
    "\\\""|"\"" {yybegin(STRING_SQ_START);return WXMLExprTypes.STRING_START;}
}

<STRING_DQ_START> {
    [^\R"\\\""\"] {
        return WXMLExprTypes.STRING_CONTENT;
    }
    "\\\""|"\"" {
          yybegin(YYINITIAL);
          return WXMLExprTypes.STRING_END;
      }
}

<STRING_SQ_START> {
    [^\R"\\'"\'] {
          return WXMLExprTypes.STRING_CONTENT;
    }
    "\\'"|"'" {
          yybegin(YYINITIAL);
          return WXMLExprTypes.STRING_END;
    }
}