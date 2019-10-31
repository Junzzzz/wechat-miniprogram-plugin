package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import java.util.*

abstract class AbstractWXSSBlock(
        node: ASTNode, wrap: Wrap = Wrap.createWrap(WrapType.NONE, false),
        alignment: Alignment = Alignment.createAlignment(true), protected val codeStyleSettings: CodeStyleSettings
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): MutableList<Block> {
        return getChildrenByASTNode(this.node)
    }

    open fun canBuildChildBlock(node: ASTNode): Boolean {
        return true
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

    protected fun getChildrenByASTNode(node: ASTNode):MutableList<Block> {
        val blocks = ArrayList<Block>()
        var child: ASTNode? = node.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE && canBuildChildBlock(node)) {
                val block = WXSSBlockFactory.createBlock(child, codeStyleSettings)
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

}