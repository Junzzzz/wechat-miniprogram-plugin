package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class WXSSParserDefinition : ParserDefinition {

    companion object{
        val iFileElementType = IFileElementType(WXSSLanguage.INSTANCE)
    }

    override fun createFile(p0: FileViewProvider?): PsiFile {
        return WXSSPsiFile(p0!!)
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getFileNodeType(): IFileElementType {
        return iFileElementType
    }

    override fun createLexer(p0: Project?): Lexer {
        return FlexAdapter(_WXSSLexer(null))
    }

    override fun createElement(p0: ASTNode?): PsiElement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommentTokens(): TokenSet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createParser(p0: Project?): PsiParser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}