package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import java.util.*

class WXSSAttributeValueBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(
                node, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, false), codeStyleSettings = codeStyleSettings
        ) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .before(WXSSTypes.ATTRIBUTE_VALUE)
                .spaces(1)
                .before(TokenSet.create(WXSSTypes.COMMA))
                .spaces(0)
                .before(WXSSTypes.SEMICOLON)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>()
        var child: ASTNode? = node.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                if (child.elementType === WXSSTypes.ATTRIBUTE_VALUE) {
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

}