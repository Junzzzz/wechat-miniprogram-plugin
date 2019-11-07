package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.util.elementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*

class WXMLElementBlock(element: WXMLElement) :
        AbstractBlock(element.node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment()) {
    override fun isLeaf(): Boolean {

    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun buildChildren(): MutableList<Block> {
        val result: MutableList<Block> = mutableListOf()
        val wxmlElement = (this.node.psi as WXMLElement)
        wxmlElement.openedElement?.let {
            // start tag
            result.add(WXMLStartTagBlock(it.startTag))
            // element
            it.elementContent?.children?.filterIsInstance<WXMLElement>()?.map { WXMLElementBlock(it) }?.apply {
                result.addAll(this)
            }
        }
        wxmlElement.closedElement?.let { wxmlClosedElement ->
            result.addAll(
                    wxmlClosedElement.children.mapNotNull {
                        when {
                            // tag name
                            it.elementType === WXMLTypes.TAG_NAME -> WXMLTagNameBlock(it.node)
                            // attribute
                            it is WXMLAttribute -> WXMLAttributeBlock(it)
                            else -> null
                        }
                    }
            )
        }
        return result
    }

    override fun getIndent(): Indent? {
        return Indent.getNormalIndent()
    }
}