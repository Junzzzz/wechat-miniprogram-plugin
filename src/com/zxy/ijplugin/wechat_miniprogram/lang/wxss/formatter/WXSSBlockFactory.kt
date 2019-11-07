package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.IFileElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

object WXSSBlockFactory {

    fun createBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings): Block {
        return when {
            node.elementType is IFileElementType -> WXSSFileBlock(node, codeStyleSettings)
            node.elementType == WXSSTypes.STYLE_DEFINITION -> WXSSStyleDefinitionBlock(node, codeStyleSettings)
            node.elementType == WXSSTypes.FONT_DEFINITION -> WXSSFontDefinitionBlock(node, codeStyleSettings)
//            node.elementType == WXSSTypes.STYLE_STATEMENT_SECTION -> WXSSStyleStatementSectionBlock(
//                    node, codeStyleSettings
//            )
            node.elementType == WXSSTypes.SELECTOR_GROUP -> WXSSSelectorGroupBlock(
                    node, codeStyleSettings = codeStyleSettings
            )
            node.elementType == WXSSTypes.STYLE_STATEMENT_COLLECTION -> WXSSStyleStatementCollectionBlock(
                    node, codeStyleSettings
            )
            node.elementType == WXSSTypes.RIGHT_BRACKET -> WXSSRightBracketBlock(node, codeStyleSettings)
            node.elementType == WXSSTypes.ATTRIBUTE_VALUE -> WXSSAttributeValueBlock(node, codeStyleSettings)
            node.elementType == WXSSTypes.STYLE_STATEMENT -> WXSSStyleStatementBlock(node, codeStyleSettings)
            else -> WXSSDefaultBlock(node, codeStyleSettings = codeStyleSettings)
        }
    }

}