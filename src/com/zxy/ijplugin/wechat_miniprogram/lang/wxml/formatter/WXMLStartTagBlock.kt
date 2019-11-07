package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag

class WXMLStartTagBlock(startTag: WXMLStartTag) : AbstractBlock(
        startTag.node, Wrap.createWrap(WrapType.NONE, false),
        Alignment.createAlignment()
) {
    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildChildren(): MutableList<Block> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLeaf(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}