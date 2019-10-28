package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.PsiBuilderFactory
import com.intellij.psi.util.elementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLExpr
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.parser.WXSSParser

object WXMLPsiImplUtils {


    fun getLanguage(element: WXMLExpr): Language {
        return WXMLLanguage.INSTANCE
    }

    fun parseContents(element: WXMLExpr, chameleon: ASTNode): ASTNode {
        val factory = PsiBuilderFactory.getInstance()
        val parentElement = chameleon.treeParent.psi
        val project = parentElement.project
        val builder = factory.createBuilder(project, chameleon)
        val parser = WXSSParser()
        return parser.parse(element.elementType!!, builder)
    }

}