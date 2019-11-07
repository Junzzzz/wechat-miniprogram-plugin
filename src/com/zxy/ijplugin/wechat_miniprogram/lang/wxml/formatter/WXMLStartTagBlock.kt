package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.openapi.util.Key
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.mapChildrenNotNull
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.isMultiLine

class WXMLStartTagBlock(startTag: WXMLStartTag, private val codeStyleSettings: CodeStyleSettings) : AbstractBlock(
        startTag.node, Wrap.createWrap(WrapType.NONE, false),
        Alignment.createAlignment()
) {

    companion object {
        val WXML_START_TAG_KEY = Key.create<Alignment>("WXMLStartTagBlock#Alignment")
    }

    init {
        val wxmlElement = startTag.parent.parent as WXMLElement
        if (wxmlElement.isMultiLine()) {
            wxmlElement.putUserData(WXML_START_TAG_KEY, this.alignment)
        }
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXMLLanguage.INSTANCE)
                .after(WXMLTypes.ATTRIBUTE)
                .spaces(1)
                .between(WXMLTypes.ATTRIBUTE, WXMLTypes.ATTRIBUTE)
                .spaces(1)
                .before(WXMLTypes.START_TAG_END)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        return this.node.mapChildrenNotNull {
            when {
                // tag name
                it.elementType == WXMLTypes.TAG_NAME -> WXMLTagNameBlock(it)
                // attribute
                it.psi is WXMLAttribute -> WXMLAttributeBlock(it.psi as WXMLAttribute, codeStyleSettings)
                it.elementType == WXMLTypes.START_TAG_END -> WXMLStartTagEndBlock(it)
                else -> WXMLLeafBlock.createLeafBlockForIgnoredNode(it)
            }
        }.toMutableList()
    }

    override fun isLeaf(): Boolean {
        return false
    }
}