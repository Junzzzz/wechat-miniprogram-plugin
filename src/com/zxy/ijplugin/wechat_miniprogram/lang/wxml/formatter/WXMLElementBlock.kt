package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.mapChildrenNotNull
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.isInnerElement

class WXMLElementBlock(element: WXMLElement, private val codeStyleSettings: CodeStyleSettings) :
        AbstractBlock(
                element.node,
                // text 不会被强制另起一行
                if (element.isInnerElement()) {
                    Wrap.createWrap(WrapType.NONE, false)
                } else {
                    Wrap.createWrap(
                            WrapType.ALWAYS, true
                    )
                },
                Alignment.createAlignment()
        ) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun buildChildren(): MutableList<Block> {
        val result: MutableList<Block> = mutableListOf()
        val wxmlElement = (this.node.psi as WXMLElement)
        wxmlElement.openedElement?.let { wxmlOpenedElement ->
            result.addAll(wxmlOpenedElement.node.mapChildrenNotNull {
                when (it.psi) {
                    is WXMLStartTag -> (WXMLStartTagBlock(it.psi as WXMLStartTag, codeStyleSettings))
                    is WXMLElement -> (WXMLElementBlock(it.psi as WXMLElement, codeStyleSettings))
                    is WXMLEndTag -> WXMLEndTagBlock(it.psi as WXMLEndTag, codeStyleSettings)
                    else -> WXMLLeafBlock.createLeafBlockForIgnoredNode(it)
                }
            })
        }
        wxmlElement.closedElement?.let { wxmlClosedElement ->
            result.add(
                    WXMLClosedElementBlock(wxmlClosedElement, codeStyleSettings)
            )
        }
        return result
    }

    /**
     * 如果没有父元素那么没有缩进
     * 否则正常缩进
     */
    override fun getIndent(): Indent? {
        return if (PsiTreeUtil.getParentOfType(
                        this.node.psi, WXMLElement::class.java
                ) == null) {
            Indent.getNoneIndent()
        } else {
            Indent.getNormalIndent()
        }
    }
}