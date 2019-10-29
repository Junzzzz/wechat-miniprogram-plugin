package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.PsiBuilderFactory
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.tree.ILazyParseableElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.lexer._WXMLExprLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.parser.WXMLExprParser
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage


open class WXMLEmbedExprElementType : ILazyParseableElementType("EXPR") {

    companion object {
        @JvmField
        val INSTANCE = WXMLEmbedExprElementType()
    }

    override fun getLanguage(): Language {
        return WXMLLanguage.INSTANCE
    }

    override fun parseContents(chameleon: ASTNode): ASTNode {
        val factory = PsiBuilderFactory.getInstance()
        val parentElement = chameleon.treeParent.psi
        val project = parentElement.project
        val builder = factory.createBuilder(project,FlexAdapter(_WXMLExprLexer(null)), chameleon)
        val parser = WXMLExprParser()
        return parser.parse(this, builder).firstChildNode
    }

}