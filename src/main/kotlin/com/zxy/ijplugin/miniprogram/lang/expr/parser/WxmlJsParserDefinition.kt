/*
 *     Copyright (c) [2019] [zxy]
 *     [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *     You can use this software according to the terms and conditions of the Mulan PSL v1.
 *     You may obtain a copy of Mulan PSL v1 at:
 *         http://license.coscl.org.cn/MulanPSL
 *     THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *     PURPOSE.
 *     See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.miniprogram.lang.expr.parser

import com.intellij.lang.javascript.JSFlexAdapter
import com.intellij.lang.javascript.JavascriptParserDefinition
import com.intellij.lang.javascript.settings.JSRootConfiguration
import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IFileElementType
import com.zxy.ijplugin.miniprogram.lang.expr.WxmlJsLanguage

class WxmlJsParserDefinition : JavascriptParserDefinition() {

    companion object {
        private val FILE: IFileElementType = JSFileElementType.create(WxmlJsLanguage.INSTANCE)

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