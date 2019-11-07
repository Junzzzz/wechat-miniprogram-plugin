package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock

class WXMLTagNameBlock(node: ASTNode) :
        AbstractBlock(node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment()) {
    override fun isLeaf(): Boolean {
        return true
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun buildChildren(): MutableList<Block> {
        return mutableListOf()
    }
}