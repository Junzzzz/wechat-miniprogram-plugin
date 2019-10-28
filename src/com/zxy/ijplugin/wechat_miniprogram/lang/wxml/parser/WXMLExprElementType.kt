package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.tree.ILazyParseableElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.parser.WXSSParser


open class WXMLExprElementType: ILazyParseableElementType("EXPR") {

    companion object {
        @JvmField
        val INSTANCE = WXMLExprElementType()
    }

    override fun getLanguage(): Language {
        return WXMLLanguage.INSTANCE
    }

    override fun parseContents(chameleon: ASTNode): ASTNode {
        val factory = PsiBuilderFactory.getInstance()
        val parentElement = chameleon.treeParent.psi
        val project = parentElement.project
        val builder = factory.createBuilder(project,chameleon)
        val parser = WXSSParser()
        return parser.parse(this, builder)
    }

}