package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
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
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        val textAttributesKey = when (iElementType) {
            WXMLTypes.START_TAG_START,
            WXMLTypes.START_TAG_END,
            WXMLTypes.END_TAG_START,
            WXMLTypes.EMPTY_ELEMENT_END -> WXML_TAG
            WXMLTypes.ATTRIBUTE_NAME -> WXML_ATTRIBUTE_NAME
            WXMLTypes.ATTRIBUTE_VALUE,
            WXMLTypes.EQ,
            WXMLTypes.STRING_START,
            WXMLTypes.STRING_END,
            WXMLTypes.STRING_CONTENT -> WXML_ATTRIBUTE_VALUE
            WXMLTypes.TAG_NAME -> WXML_TAG_NAME
            WXMLTypes.COMMENT -> WXML_COMMENT
            else -> null
        }
        return textAttributesKey?.let { arrayOf(it) } ?: emptyArray()
    }

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_WXMLLexer(null))
    }

}