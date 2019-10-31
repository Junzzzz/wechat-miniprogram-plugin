package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSSelectorGroupBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(node,alignment= WXSSAlignments.LEFT_ALIGNMENT, codeStyleSettings = codeStyleSettings) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .before(WXSSTypes.COMMA)
                .spaces(0)
                .after(WXSSTypes.COMMA)
                .spaces(1)
                .getSpacing(this, p0, p1)
    }

}