package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.highlighting.BraceMatcher
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLBraceMatcher : BraceMatcher {
    override fun isLBraceToken(iterator: HighlighterIterator, p1: CharSequence?, p2: FileType?): Boolean {
        val tokenType = iterator.tokenType
        return tokenType == WXMLTypes.START_TAG_START && findEndTag(iterator)
    }

    private fun findEndTag(iterator: HighlighterIterator): Boolean {
        var tokenType: IElementType? = null
        var balance = 0
        // 记录迭代次数
        var count = 0
        while (balance >= 0) {
            iterator.advance()
            ++count
            if (iterator.atEnd()) {
                break
            }
            tokenType = iterator.tokenType
            if (tokenType == WXMLTypes.START_TAG_START) {
                balance++
            }
            if (tokenType == WXMLTypes.EMPTY_ELEMENT_END || tokenType == WXMLTypes.END_TAG_END) {
                balance--
            }
        }
        while (count-- > 0) {
            iterator.retreat()
        }
        return tokenType == WXMLTypes.EMPTY_ELEMENT_END || tokenType == WXMLTypes.END_TAG_END
    }

    override fun isRBraceToken(iterator: HighlighterIterator, p1: CharSequence?, p2: FileType?): Boolean {
        val tokenType = iterator.tokenType
        return tokenType == WXMLTypes.END_TAG_END || tokenType == WXMLTypes.EMPTY_ELEMENT_END
    }

    override fun getCodeConstructStart(p0: PsiFile?, p1: Int): Int {
        return p1
    }

    override fun isStructuralBrace(p0: HighlighterIterator?, p1: CharSequence?, p2: FileType?): Boolean {
        return true
    }

    override fun isPairedBracesAllowedBeforeType(p0: IElementType, p1: IElementType?): Boolean {
        return false
    }

    override fun isPairBraces(p0: IElementType?, p1: IElementType?): Boolean {
        return (p0 == WXMLTypes.START_TAG_START && p1 == WXMLTypes.END_TAG_END)
                || (p1 == WXMLTypes.START_TAG_START && p0 == WXMLTypes.END_TAG_END)
                || (p0 == WXMLTypes.START_TAG_START && p1 == WXMLTypes.EMPTY_ELEMENT_END)
                || (p1 == WXMLTypes.START_TAG_START && p0 == WXMLTypes.EMPTY_ELEMENT_END)
    }

    override fun getBraceTokenGroupId(p0: IElementType?): Int {
        return 1
    }

    override fun getOppositeBraceTokenType(p0: IElementType): IElementType? {
        return when (p0) {
            WXMLTypes.END_TAG_END -> return WXMLTypes.START_TAG_START
            WXMLTypes.EMPTY_ELEMENT_END -> return WXMLTypes.START_TAG_START
            else -> null
        }
    }
}