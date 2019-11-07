package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute

class WXMLAttributeBlock(element: WXMLAttribute) : AbstractBlock(
        element.node,
        Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, false),
        Alignment.createAlignment()
) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {

    }

    override fun buildChildren(): MutableList<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}