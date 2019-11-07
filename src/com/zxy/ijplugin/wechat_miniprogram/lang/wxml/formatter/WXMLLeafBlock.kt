package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

open class WXMLLeafBlock(node: ASTNode) :
        AbstractBlock(node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment()) {

    companion object{
        fun createLeafBlockForIgnoredNode(node: ASTNode): WXMLLeafBlock? {
            return if (node.elementType===TokenType.WHITE_SPACE){
                null
            }else{
                WXMLLeafBlock(node)
            }
        }
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return null
    }

    override fun buildChildren(): MutableList<Block> {
        return mutableListOf()
    }

    override fun isLeaf(): Boolean {
        return true
    }
}