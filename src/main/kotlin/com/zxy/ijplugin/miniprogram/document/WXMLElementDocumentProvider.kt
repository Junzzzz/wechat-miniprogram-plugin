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

@file:Suppress("DEPRECATION")

package com.zxy.ijplugin.miniprogram.document

import com.intellij.codeInsight.documentation.DocumentationManager.ORIGINAL_ELEMENT_KEY
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLElementDescription
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.tag.WXMLElementDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLElementFactory

/**
 * 对WXML的自带组件提供文档
 */
class WXMLElementDocumentProvider : DocumentationProvider {

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement): String? {
        return this.getDescription(originalElement)?.let {
            buildQuickNavigateInfoFromElementDescriptor(it)
        }
    }

    private fun getDescription(element: PsiElement): WXMLElementDescription? {
        return ((element as? XmlTag)?.descriptor as? WXMLElementDescriptor)?.wxmlElementDescription
    }

    private fun buildQuickNavigateInfoFromElementDescriptor(wxmlElementDescription: WXMLElementDescription): String {
        return "Element <code>${wxmlElementDescription.name}</code> ${wxmlElementDescription.description ?: ""}"
    }

    override fun getUrlFor(element: PsiElement, originalElement: PsiElement?): MutableList<String> {
        if (isInsideJsonConfigFile(element)) {
            if (element.parent?.parent?.parent == element.containingFile) {
                val wxmlElementDescription = originalElement?.let(::getDescription)
                return wxmlElementDescription?.url?.let {
                    mutableListOf(it)
                } ?: mutableListOf()
            }
        }
        return mutableListOf()
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (isInsideJsonConfigFile(element) && element is JsonStringLiteral) {
            if (element.parent?.parent?.parent == element.containingFile) {
                val name = element.value
                val wxmlElementDescriptor = WXMLMetadata.getElementDescriptions(element.project).find {
                    it.name == name
                }
                return if (wxmlElementDescriptor != null) {
                    this.generateDocFromElementDescriptor(wxmlElementDescriptor)
                } else {
                    null
                }
            }
        }
        return null
    }

    private fun generateDocFromElementDescriptor(wxmlElementDescription: WXMLElementDescription): String {
        val stringBuilder = StringBuilder()
        // 头部
        stringBuilder.append("<div class='definition'><pre>")
            .append(wxmlElementDescription.name)
            .append("</pre>")
        if (wxmlElementDescription.description != null) {
            stringBuilder.append("<pre>")
                .append(wxmlElementDescription.description)
                .append("</pre>")
        }
        stringBuilder.append("</div>")

        // 内容
        stringBuilder.append(CONTENT_START)
        if (wxmlElementDescription.attributeDescriptorPresetElementAttributeDescriptors.isNotEmpty()) {
            // 属性信息表头
            stringBuilder.append(SECTIONS_START)
            stringBuilder.append(
                """<thead>
                        |<tr>
|<td>属性</td>
|<td>描述</td>
|</tr>
|</thead>""".trimMargin()
            )
            wxmlElementDescription.attributeDescriptorPresetElementAttributeDescriptors.forEach { wxmlElementAttributeDescriptor ->
                stringBuilder.append("<tr>")
                    .append(SECTION_START)
                    .append(GRAYED_START)
                    .append("<a href='${PSI_ELEMENT_PROTOCOL}elements/${wxmlElementDescription.name}/attributes/${wxmlElementAttributeDescriptor.key}'>")
                    .append(wxmlElementAttributeDescriptor.key)
                    .append("</a>")
                    .append(GRAYED_END)
                    .append(SECTION_END)
                    .append(SECTION_START)
                    .append(wxmlElementAttributeDescriptor.description ?: "")
                    .append(SECTION_END)
                    .append("</tr>")
            }
            stringBuilder.append(SECTIONS_END)
        }
        stringBuilder.append(CONTENT_END)

        return stringBuilder.toString()
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager, link: String, element: PsiElement
    ): PsiElement? {
        // 点击元素文档中的某一属性
        // 这个属性对应的文档
        if (isInsideJsonConfigFile(element) && element is JsonStringLiteral) {
            if (element.parent?.parent?.parent == element.containingFile) {

                val tagName = element.value
                val wxmlElementDescriptor = WXMLMetadata.getElementDescriptions(element.project).find {
                    it.name == tagName
                }
                val attributeName = link.removePrefix("elements/$tagName/attributes/")
                val definedElement = wxmlElementDescriptor?.attributeDescriptorPresetElementAttributeDescriptors?.find {
                    it.key == attributeName
                }?.definedElement

                // 将打开一个内存中的元素的文档
                definedElement?.putUserData(
                    ORIGINAL_ELEMENT_KEY, SmartPointerManagerImpl.createPointer(
                        WXMLElementFactory.createAttributeName(element.project, attributeName, tagName)
                    )
                )
                return definedElement
            }
        }
        return null
    }

}