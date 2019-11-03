package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSBraceMatcher : PairedBraceMatcher {
    override fun getCodeConstructStart(psiFIle: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    override fun getPairs(): Array<BracePair> {
        return arrayOf(
                BracePair(WXSSTypes.LEFT_PARENTHESES, WXSSTypes.RIGHT_PARENTHESES, false),
                BracePair(WXSSTypes.LEFT_BRACKET, WXSSTypes.RIGHT_BRACKET, false)
        )
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}