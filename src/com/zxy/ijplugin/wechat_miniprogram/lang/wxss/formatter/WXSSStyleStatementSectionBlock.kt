package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
@Deprecated("这个块暂时没啥用")
class WXSSStyleStatementSectionBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(node, codeStyleSettings = codeStyleSettings) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(this.codeStyleSettings, WXSSLanguage.INSTANCE)
                .around(TokenSet.create(WXSSTypes.LEFT_BRACKET, WXSSTypes.RIGHT_BRACKET))
                .blankLines(0)
                .getSpacing(this, p0, p1)
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

}