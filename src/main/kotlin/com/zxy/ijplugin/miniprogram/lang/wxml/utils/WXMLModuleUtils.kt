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

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile

object WXMLModuleUtils {


    fun findTemplateDefinitionXmlAttributeValue(wxmlPsiFile: WXMLPsiFile, templateName: String): XmlAttributeValue? {
        val elements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, XmlTag::class.java)
        // 在本文件中找
        this.findTemplateDefinitionWithSingleFile(elements, templateName)?.let {
            return it
        }
        // 找到所有的import的文件引用
        val fileReferences = findSrcImportedFileReferences(elements)
        for (fileReference in fileReferences) {
            val resolveFile = fileReference.resolve()
            if (resolveFile is WXMLPsiFile) {
                findTemplateDefinitionOnlySingleFile(resolveFile, templateName)?.let {
                    return it
                }
            }
        }
        return null
    }

    private fun findSrcImportedFileReferences(
        elements: Collection<XmlTag>
    ): Set<FileReference> {
        return elements.asSequence().filter {
            it.name == "import"
        }.mapNotNull { xmlTag ->
            xmlTag.attributes.find {
                it.name == "src"
            }
        }.mapNotNull {
            it.valueElement
        }.mapNotNull { xmlAttributeValue ->
            xmlAttributeValue.references.findLast {
                it is FileReference
            }
        }.filterIsInstance<FileReference>()
            .toSet()
    }

    private fun findTemplateDefinitionOnlySingleFile(
        wxmlPsiFile: WXMLPsiFile, templateName: String
    ): XmlAttributeValue? {
        return this.findTemplateDefinitionWithSingleFile(
            PsiTreeUtil.findChildrenOfType(wxmlPsiFile, XmlTag::class.java), templateName
        )
    }


    private fun findTemplateDefinitionWithSingleFile(
        tags: Collection<XmlTag>, templateName: String
    ): XmlAttributeValue? {
        val templateElements = tags.filter {
            it.name == "template"
        }
        for (templateElement in templateElements) {
            val attributes = templateElement.attributes
            val nameAttribute = attributes.find {
                it.name == "name"
            }
            if (nameAttribute != null) {
                if (nameAttribute.value == templateName) {
                    return nameAttribute.valueElement
                }
            }
        }
        return null
    }

    fun findTemplateDefinitions(wxmlPsiFile: WXMLPsiFile): List<XmlAttributeValue> {
        val wxmlElements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, XmlTag::class.java)
        return findTemplateDefinitions(wxmlElements)
    }

    private fun findTemplateDefinitions(wxmlElements: Collection<XmlTag>): List<XmlAttributeValue> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.name == "template"
        }.mapNotNull { wxmlElement ->
            wxmlElement.getAttribute("name")
        }.mapNotNull {
            it.valueElement
        }
    }

    fun findValidIncludeTags(wxmlElements: Collection<XmlTag>): List<XmlTag> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.name == "include" && wxmlElement.attributes.any {
                it.name == "src" && it.valueElement?.references?.lastOrNull()?.resolve() is WXMLPsiFile
            }
        }
    }

    fun findTemplateDefinitionsWithImports(wxmlPsiFile: WXMLPsiFile): List<XmlAttributeValue> {
        val results = arrayListOf<XmlAttributeValue>()
        val elements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, XmlTag::class.java)
        results.addAll(findTemplateDefinitions(elements))
        val fileReferences = this.findSrcImportedFileReferences(elements)
        results.addAll(
            fileReferences.mapNotNull { it.resolve() }.mapNotNull { it as? WXMLPsiFile }.toList().toTypedArray()
                .flatMap {
                    findTemplateDefinitions(it)
                })
        return results
    }

}