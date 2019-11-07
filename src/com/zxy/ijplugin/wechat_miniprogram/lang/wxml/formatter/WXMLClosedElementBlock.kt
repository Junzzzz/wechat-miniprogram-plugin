package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.mapChildrenNotNull
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLClosedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLClosedElementBlock(element: WXMLClosedElement, private val codeStyleSettings: CodeStyleSettings) :
        AbstractBlock(element.node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment()) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXMLLanguage.INSTANCE)
                .after(WXMLTypes.ATTRIBUTE)
                .spaces(1)
                .between(WXMLTypes.ATTRIBUTE, WXMLTypes.ATTRIBUTE)
                .spaces(1)
                .before(WXMLTypes.EMPTY_ELEMENT_END)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        return this.node.mapChildrenNotNull  {
            when {
                // tag name
                it.elementType === WXMLTypes.TAG_NAME -> WXMLTagNameBlock(it)
                // attribute
                it.psi is WXMLAttribute -> WXMLAttributeBlock(it.psi as WXMLAttribute, codeStyleSettings)
                it.elementType === WXMLTypes.EMPTY_ELEMENT_END -> WXMLEmptyElementEndBlock(it)
                else -> WXMLLeafBlock.createLeafBlockForIgnoredNode(it)
            }
        }.toMutableList()
    }
}