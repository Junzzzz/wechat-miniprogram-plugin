package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import java.util.*



class WXSSBlock(node: ASTNode, wrap: Wrap, alignment: Alignment,private val spacingBuilder: SpacingBuilder)
    : AbstractBlock(node, wrap, alignment) {

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()
        var child: ASTNode? = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                val block = WXSSBlock(
                        child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(),
                        spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return spacingBuilder.getSpacing(this,p0,p1)
    }

}