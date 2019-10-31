package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings


open class WXSSDefaultBlock(
        node: ASTNode, wrap: Wrap = Wrap.createWrap(WrapType.NONE, false),
        alignment: Alignment = Alignment.createAlignment(true),
        codeStyleSettings: CodeStyleSettings
) : AbstractWXSSBlock(node, wrap, alignment,codeStyleSettings) {

    override fun isLeaf(): Boolean = true

    override fun getSpacing(p0: Block?, p1: Block): Spacing? = null

    override fun getIndent(): Indent? = Indent.getNoneIndent()

    override fun canBuildChildBlock(node: ASTNode): Boolean = false

}