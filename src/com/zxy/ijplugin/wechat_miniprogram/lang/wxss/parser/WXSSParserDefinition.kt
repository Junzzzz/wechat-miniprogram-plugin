package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.parser

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
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer._WXSSLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSParserDefinition : ParserDefinition {

    companion object {
        val iFileElementType = IFileElementType(WXSSLanguage.INSTANCE)
        val COMMENTS = TokenSet.create(WXSSTypes.COMMENT)
    }

    override fun createParser(p0: Project?): PsiParser {
        return WXSSParser()
    }

    override fun createFile(p0: FileViewProvider): PsiFile {
        return WXSSPsiFile(p0)
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
        return WXSSTypes.Factory.createElement(p0)
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }
}