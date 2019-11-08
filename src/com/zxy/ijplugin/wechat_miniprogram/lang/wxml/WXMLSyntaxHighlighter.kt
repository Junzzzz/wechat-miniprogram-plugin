package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.javascript.highlighting.JSHighlighter
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.css.impl.util.CssHighlighter
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.lexer._WXMLLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val WXML_TAG = createTextAttributesKey("WXML_TAG", XmlHighlighterColors.HTML_TAG)
        val WXML_TAG_NAME = createTextAttributesKey("WXML_TAG_NAME", XmlHighlighterColors.HTML_TAG_NAME)
        val WXML_ATTRIBUTE_VALUE = createTextAttributesKey(
                "WXML_ATTRIBUTE_VALUE", XmlHighlighterColors.HTML_ATTRIBUTE_VALUE
        )
        val WXML_ATTRIBUTE_NAME = createTextAttributesKey(
                "WXML_ATTRIBUTE_NAME", XmlHighlighterColors.HTML_ATTRIBUTE_NAME
        )
        val WXML_COMMENT = createTextAttributesKey("WXML_COMMENT", XmlHighlighterColors.HTML_COMMENT)
        val WXML_IDENTIFIER = createTextAttributesKey("WXML_IDENTIFIER", JSHighlighter.JS_LOCAL_VARIABLE)
        val WXML_BRACKET = createTextAttributesKey("WXML_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
        val WXML_COMMA = createTextAttributesKey("WXML_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val WXML_NUMBER = createTextAttributesKey("WXML_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val WXML_NATIVE_VALUE = createTextAttributesKey("WXML_NATIVE_VALUE", JSHighlighter.JS_KEYWORD)
        val WXML_OPERATOR = createTextAttributesKey("WXML_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val WXML_COLON = createTextAttributesKey("WXML_COLON", CssHighlighter.CSS_COLON)
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        val textAttributesKey = when (iElementType) {
            WXMLTypes.START_TAG_START,
            WXMLTypes.START_TAG_END,
            WXMLTypes.END_TAG_START,
            WXMLTypes.EMPTY_ELEMENT_END -> WXML_TAG
            WXMLTypes.ATTRIBUTE_NAME -> WXML_ATTRIBUTE_NAME
            WXMLTypes.EQ,
            WXMLTypes.STRING_START,
            WXMLTypes.STRING_END,
            WXMLTypes.STRING_CONTENT -> WXML_ATTRIBUTE_VALUE
            WXMLTypes.TAG_NAME -> WXML_TAG_NAME
            WXMLTypes.COMMENT_START,
            WXMLTypes.COMMENT_CONTENT,
            WXMLTypes.COMMONT_END -> WXML_COMMENT
            // expr
            WXMLTypes.IDENTIFIER -> WXML_IDENTIFIER
            WXMLTypes.LEFT_BRACKET,
            WXMLTypes.RIGHT_BRACKET -> WXML_BRACKET
            WXMLTypes.COMMA -> WXML_COMMA
            WXMLTypes.NUMBER -> WXML_NUMBER
            WXMLTypes.FALSE,
            WXMLTypes.TRUE,
            WXMLTypes.NULL -> WXML_NATIVE_VALUE
            WXMLTypes.PLUS,
            WXMLTypes.MINUS,
            WXMLTypes.MULTIPLY,
            WXMLTypes.DIVIDE,
            WXMLTypes.RESIDUAL,
            WXMLTypes.NOT_EQ,
            WXMLTypes.EQ,
            WXMLTypes.NOT_STRICT_EQ,
            WXMLTypes.QUESTION_MARK,
            WXMLTypes.EXPAND_KEYWORD,
            WXMLTypes.EXCLAMATION_MARK,
            WXMLTypes.STRICT_EQ -> WXML_OPERATOR
            WXMLTypes.COLON -> WXML_COLON
            else -> null
        }
        return textAttributesKey?.let { arrayOf(it) } ?: emptyArray()
    }

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_WXMLLexer(null))
    }

}