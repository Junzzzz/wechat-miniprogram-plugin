package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.css.impl.util.CssHighlighter
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer._WXSSLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val WXSS_COMMA = createTextAttributesKey("WXSS_COMMA", CssHighlighter.CSS_COMMA)
        val WXSS_ATTRIBUTE_NAME = createTextAttributesKey("WXSS_ATTRIBUTE_NAME", CssHighlighter.CSS_ATTRIBUTE_NAME)
        val WXSS_ATTRIBUTE_VALUE_BASIC = createTextAttributesKey("WXSS_ATTRIBUTE_VALUE_BASIC",CssHighlighter.CSS_PROPERTY_VALUE)
        val WXSS_CLASS_NAME = createTextAttributesKey("WXSS_CLASS_NAME",CssHighlighter.CSS_CLASS_NAME)
        val WXSS_ID_SELECTOR = createTextAttributesKey("WXSS_ID_SELECTOR",CssHighlighter.CSS_ID_SELECTOR)
    }

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_WXSSLexer(null))
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        return when (iElementType) {
            WXSSTypes.COMMA -> arrayOf(WXSS_COMMA)
            WXSSTypes.ATTRIBUTE_NAME -> arrayOf(WXSS_ATTRIBUTE_NAME)
            WXSSTypes.ATTRIBUTE_VALUE_LITERAL,
            WXSSTypes.FUNCTION_NAME,
            WXSSTypes.NUMBER,
            WXSSTypes.NUMBER_UNIT -> arrayOf(WXSS_ATTRIBUTE_VALUE_BASIC)
            WXSSTypes.IDENTIFIER -> arrayOf(WXSS_CLASS_NAME)
            WXSSTypes.ID_SELECTOR -> arrayOf(WXSS_ID_SELECTOR)
            else -> emptyArray()
        }
    }

}