package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement


class WXMLCommentStartBlock(node: ASTNode) : WXMLLeafBlock(
        node,
        Wrap.createWrap(WrapType.NORMAL, false),
        null
) {

    override fun getIndent(): Indent? {
        val parent = PsiTreeUtil.getParentOfType(
                this.node.psi, WXMLElement::class.java
        )
        return if (parent == null) Indent.getNoneIndent() else Indent.getNormalIndent()
    }
}