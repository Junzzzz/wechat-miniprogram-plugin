package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSQuoteHandler :
        SimpleTokenSetQuoteHandler(
                WXSSTypes.STRING_START_DQ, WXSSTypes.STRING_END_DQ
        ){
    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean {
        val tokenType = iterator.tokenType
        return if (!this.myLiteralTokenSet.contains(tokenType)) {
            false
        } else {
            val start = iterator.start
            val end = iterator.end
            end - start >= 1 && iterator.let {
                it.advance()
                offset == it.end - 1
            }
        }
    }
}