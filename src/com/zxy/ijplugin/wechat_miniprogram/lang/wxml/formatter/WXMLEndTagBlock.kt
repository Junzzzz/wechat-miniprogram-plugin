package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.mapChildrenNotNull
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.isMultiLine

class WXMLEndTagBlock(element: WXMLEndTag, private val codeStyleSettings: CodeStyleSettings) :
        AbstractBlock(
                element.node, if ((element.parent.parent as WXMLElement).isMultiLine()) {
            Wrap.createWrap(WrapType.ALWAYS, true)
        } else {
            Wrap.createWrap(WrapType.NONE, false)
        },
                if ((element.parent.parent as WXMLElement).isMultiLine()) {
                    (element.parent.parent as WXMLElement).getUserData(WXMLStartTagBlock.WXML_START_TAG_KEY)
                            ?: Alignment.createAlignment()
                } else {
                    Alignment.createAlignment()
                }
        ) {

    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(this.codeStyleSettings, WXMLLanguage.INSTANCE)
                .before(WXMLTypes.END_TAG_END)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun buildChildren(): MutableList<Block> {
        return this.node.mapChildrenNotNull {
            WXMLLeafBlock.createLeafBlockForIgnoredNode(it)
        }.toMutableList()
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }
}