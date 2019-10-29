package com.zxy.ijplugin.wechat_miniprogram.lang.expr.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSFlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLExprParserDefinition :
        ParserDefinition {
    override fun createParser(p0: Project?): PsiParser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createFile(p0: FileViewProvider?): PsiFile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringLiteralElements(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileNodeType(): IFileElementType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createElement(p0: ASTNode?): PsiElement {
        return WXMLTypes.Factory.createElement(p0)
    }

    override fun getCommentTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createLexer(project: Project?): Lexer {
        return JSFlexAdapter(DialectOptionHolder.JS_1_8)
    }

}