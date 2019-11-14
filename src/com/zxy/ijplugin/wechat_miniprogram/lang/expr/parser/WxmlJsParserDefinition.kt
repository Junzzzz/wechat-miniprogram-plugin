package com.zxy.ijplugin.wechat_miniprogram.lang.expr.parser

import com.intellij.lang.javascript.JSFlexAdapter
import com.intellij.lang.javascript.JavascriptParserDefinition
import com.intellij.lang.javascript.settings.JSRootConfiguration
import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IFileElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.WxmlJsLanguage

class WxmlJsParserDefinition : JavascriptParserDefinition() {

    companion object {
        private val FILE: IFileElementType = JSFileElementType.create(WxmlJsLanguage.INSTANCE)

        const val EXPRESSION: String = "expr"

        fun createLexer(project: Project?): Lexer {
            return JSFlexAdapter(JSRootConfiguration.getInstance(project).languageLevel.dialect.optionHolder)
        }

    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createLexer(project: Project?): Lexer {
        return Companion.createLexer(project)
    }

}