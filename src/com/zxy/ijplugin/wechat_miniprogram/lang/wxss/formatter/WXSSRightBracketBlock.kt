package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings

class WXSSRightBracketBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(node, alignment = WXSSAlignments.LEFT_ALIGNMENT, codeStyleSettings = codeStyleSettings) {
    override fun isLeaf(): Boolean {
        return true
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun canBuildChildBlock(node: ASTNode): Boolean {
        return false
    }
}