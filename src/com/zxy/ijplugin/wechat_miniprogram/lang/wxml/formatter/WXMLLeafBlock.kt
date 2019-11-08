package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiComment
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

open class WXMLLeafBlock(
        node: ASTNode, wrap: Wrap = Wrap.createWrap(WrapType.NONE, false),
        alignment: Alignment? = Alignment.createAlignment()
) :
        AbstractBlock(node, wrap, alignment) {

    companion object {
        fun createLeafBlockForIgnoredNode(node: ASTNode): WXMLLeafBlock? {
            return when {
                node.elementType === TokenType.WHITE_SPACE -> null
                node.psi is PsiComment -> WXMLCommentStartBlock(node)
                else -> WXMLLeafBlock(node)
            }
        }
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun buildChildren(): MutableList<Block> {
        return mutableListOf()
    }

    override fun isLeaf(): Boolean {
        return true
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }
}