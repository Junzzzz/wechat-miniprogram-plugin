package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.parser

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
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.lexer._WXMLLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl.WXMLExprImpl

class WXMLParserDefinition : ParserDefinition {

    companion object {
        val iFileElementType = IFileElementType(WXMLLanguage.INSTANCE)
        val COMMENTS = TokenSet.create(WXMLTypes.COMMENT)
    }

    override fun createParser(p0: Project?): PsiParser {
        return WXMLParser()
    }

    override fun createFile(p0: FileViewProvider): PsiFile {
        return WXMLPsiFile(p0)
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getFileNodeType(): IFileElementType {
        return iFileElementType
    }

    override fun createLexer(p0: Project?): Lexer {
        return FlexAdapter(_WXMLLexer(null))
    }

    override fun createElement(astNode: ASTNode): PsiElement {
        if (astNode.elementType === WXMLExprElementType.INSTANCE){
            return WXMLExprImpl(astNode)
        }
        return WXMLTypes.Factory.createElement(astNode)
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }
}