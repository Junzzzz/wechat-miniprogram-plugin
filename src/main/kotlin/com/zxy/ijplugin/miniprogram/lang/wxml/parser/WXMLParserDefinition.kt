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

package com.zxy.ijplugin.miniprogram.lang.wxml.parser

import com.intellij.lang.PsiParser
import com.intellij.lang.xml.XMLParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile

class WXMLParserDefinition : XMLParserDefinition() {

    companion object {
        val iFileElementType = IFileElementType(WXMLLanguage.INSTANCE)
    }

    override fun createParser(project: Project?): PsiParser {
        return WXMLXmlParser()
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return WXMLPsiFile(viewProvider)
    }

    override fun getFileNodeType(): IFileElementType {
        return iFileElementType
    }
}