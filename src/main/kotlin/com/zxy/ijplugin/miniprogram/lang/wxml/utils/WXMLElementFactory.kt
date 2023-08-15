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

package com.zxy.ijplugin.miniprogram.lang.wxml.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.utils.findChildOfType

object WXMLElementFactory {

    fun createAttributeName(project: Project, attributeName: String, tagName: String = "text"): PsiElement {
        val psiFile = createDummyFile(
            project, """
            <$tagName $attributeName/>
        """.trimIndent()
        )
        return psiFile.findChildOfType<XmlAttribute>()?.firstChild!!
    }

    private fun createDummyFile(project: Project, fileContent: String): PsiFile {
        val name = "dummy.wxml"
        return PsiFileFactory.getInstance(project).createFileFromText(name, WXMLFileType.INSTANCE, fileContent)
    }

    fun createTagName(project: Project, elementName: String): PsiElement {
        val psiFile = createDummyFile(
            project, """
            <$elementName/>
        """.trimIndent()
        )
        return psiFile.findChildOfType<XmlTag>()?.firstChild?.nextSibling!!
    }

}