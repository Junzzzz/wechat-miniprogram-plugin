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

package com.zxy.ijplugin.miniprogram.lang.wxss.parser

import com.intellij.lang.css.CSSParserDefinition
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile

class WXSSParserDefinition : CSSParserDefinition() {

    companion object {
        val iFileElementType = IFileElementType(WXSSLanguage.INSTANCE)
    }

//    override fun createParser(p0: Project?): PsiParser {
//        return WXSSParser()
//    }

    override fun createFile(p0: FileViewProvider): PsiFile {
        return WXSSPsiFile(p0)
    }

    override fun getFileNodeType(): IFileElementType {
        return iFileElementType
    }

//    override fun createLexer(p0: Project?): Lexer {
//        return FlexAdapter(_WXSSLexer(null))
//    }

//    override fun createElement(p0: ASTNode?): PsiElement {
//        return WXSSTypes.Factory.createElement(p0)
//    }

//    override fun getCommentTokens(): TokenSet {
//        return COMMENTS
//    }
}