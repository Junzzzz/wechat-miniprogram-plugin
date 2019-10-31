package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import java.util.*

open class WXSSStyleDefinitionBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(node, codeStyleSettings = codeStyleSettings, alignment = WXSSAlignments.LEFT_ALIGNMENT) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .after(WXSSTypes.SELECTOR_GROUP)
                .spaces(1)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()
        var child: ASTNode? = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                if (child.elementType === WXSSTypes.STYLE_STATEMENT_SECTION) {
                    blocks.addAll(getChildrenByASTNode(child))
                } else {
                    val block = WXSSBlockFactory.createBlock(child, codeStyleSettings)
                    blocks.add(block)
                }
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun canBuildChildBlock(node: ASTNode): Boolean {
        return true
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }
}