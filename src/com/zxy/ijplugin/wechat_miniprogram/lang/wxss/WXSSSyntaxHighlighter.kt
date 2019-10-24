package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.css.impl.util.CssHighlighter
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer._WXSSLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val WXSS_COMMA = createTextAttributesKey("WXSS_COMMA", CssHighlighter.CSS_COMMA)
        val WXSS_ATTRIBUTE_NAME = createTextAttributesKey("WXSS_ATTRIBUTE_NAME", CssHighlighter.CSS_PROPERTY_NAME)
        val WXSS_ATTRIBUTE_VALUE_BASIC = createTextAttributesKey(
                "WXSS_ATTRIBUTE_VALUE_BASIC", CssHighlighter.CSS_PROPERTY_VALUE
        )
        val WXSS_CLASS = createTextAttributesKey("WXSS_CLASS", CssHighlighter.CSS_CLASS_NAME)
        val WXSS_ID = createTextAttributesKey("WXSS_ID", CssHighlighter.CSS_ID_SELECTOR)
        val WXSS_NUMBER = createTextAttributesKey("WXSS_NUMBER", CssHighlighter.CSS_NUMBER)
        val WXSS_FUNCTION = createTextAttributesKey("WXSS_FUNCTION", CssHighlighter.CSS_FUNCTION)
        val WXSS_BRACKET = createTextAttributesKey("WXSS_BRACKET", CssHighlighter.CSS_BRACES)
        val WXSS_PARENTHESES = createTextAttributesKey("WXSS_PARENTHESES", CssHighlighter.CSS_BRACKETS)
        val WXSS_CLASS_SELECTOR = createTextAttributesKey("WXSS_CLASS_SELECTOR", CssHighlighter.CSS_DOT)
        val WXSS_COLON = createTextAttributesKey("WXSS_COLON", CssHighlighter.CSS_COLON)
        val WXSS_COLOR = createTextAttributesKey("WXSS_COLOR", CssHighlighter.CSS_COLOR)
        val WXSS_KEYWORD = createTextAttributesKey("WXSS_KEYWORD", CssHighlighter.CSS_KEYWORD)
        val WXSS_COMMENT = createTextAttributesKey("WXSS_COMMENT", CssHighlighter.CSS_COMMENT)
        val WXSS_STRING = createTextAttributesKey("WXSS_STRING", CssHighlighter.CSS_STRING)

        val WXSS_BAD_CHARACTER = createTextAttributesKey("WXSS_BAD_CHARACTER", CssHighlighter.CSS_BAD_CHARACTER)
    }

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_WXSSLexer(null))
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        return when (iElementType) {
            WXSSTypes.COMMA -> arrayOf(WXSS_COMMA)
            WXSSTypes.ATTRIBUTE_NAME -> arrayOf(WXSS_ATTRIBUTE_NAME)
            WXSSTypes.ATTRIBUTE_VALUE_LITERAL -> arrayOf(WXSS_ATTRIBUTE_VALUE_BASIC)
            WXSSTypes.FUNCTION_NAME -> arrayOf(WXSS_FUNCTION)
            WXSSTypes.NUMBER,
            WXSSTypes.NUMBER_UNIT -> arrayOf(WXSS_NUMBER)
            WXSSTypes.CLASS -> arrayOf(WXSS_CLASS)
            WXSSTypes.CLASS_SELECTOR -> arrayOf(WXSS_CLASS_SELECTOR)
            WXSSTypes.ID_SELECTOR,
            WXSSTypes.ID -> arrayOf(WXSS_ID)
            WXSSTypes.LEFT_BRACKET,
            WXSSTypes.RIGHT_BRACKET -> arrayOf(WXSS_BRACKET)
            WXSSTypes.LEFT_PARENTHESES,
            WXSSTypes.RIGHT_PARENTHESES -> arrayOf(WXSS_PARENTHESES)
            WXSSTypes.COLON -> arrayOf(WXSS_COLON)
            WXSSTypes.HASH -> arrayOf(WXSS_COLOR)
            WXSSTypes.IMPORT_KEYWORD -> arrayOf(WXSS_KEYWORD)
            WXSSTypes.COMMENT -> arrayOf(WXSS_COMMENT)
            WXSSTypes.STRING_CONTENT,
            WXSSTypes.STRING_END_DQ,
            WXSSTypes.STRING_START_DQ,
            WXSSTypes.STRING_START_SQ,
            WXSSTypes.STRING_END_SQ -> arrayOf(WXSS_STRING)
            TokenType.BAD_CHARACTER -> arrayOf(WXSS_BAD_CHARACTER)
            else -> emptyArray()
        }
    }

}