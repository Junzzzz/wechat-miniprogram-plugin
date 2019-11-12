package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.mapChildrenNotNull
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLString
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLAttributeBlock(element: WXMLAttribute, private val codeStyleSettings: CodeStyleSettings) : AbstractBlock(
        element.node,
        Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, false),
        Alignment.createAlignment()
) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXMLLanguage.INSTANCE)
                .after(WXMLTypes.ATTRIBUTE_NAME)
                .spaces(0)
                .after(WXMLTypes.EQ)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        return this.node.mapChildrenNotNull {
            when {
                it.elementType == WXMLTypes.ATTRIBUTE_NAME -> WXMLAttributeNameBlock(it)
                it.elementType == WXMLTypes.EQ -> WXMLEqualBlock(it)
                it.psi is WXMLString -> WXMLStringBlock(it.psi as WXMLString)
                else -> WXMLLeafBlock.createLeafBlockForIgnoredNode(it)
            }
        }.toMutableList()
    }
}