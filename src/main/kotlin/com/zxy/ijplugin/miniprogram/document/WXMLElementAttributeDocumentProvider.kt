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
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import com.intellij.psi.xml.XmlAttribute
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLElementFactory
import com.zxy.ijplugin.miniprogram.reference.MarkupTwoWayBindingReference
import com.zxy.ijplugin.miniprogram.utils.findStringPropertyValue

/**
 * 对wxml自带的组件的属性提供文档
 */
class WXMLElementAttributeDocumentProvider : DocumentationProvider {

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement): String? {
        if (element is JsonStringLiteral && originalElement is XmlAttribute) {
            val attributeDescriptor: WXMLAttributeDescriptor? = if (originalElement.name.startsWith("model:")) {
                originalElement.references.filterIsInstance<MarkupTwoWayBindingReference>().firstOrNull()
                    ?.getAttributeDescriptor() as? WXMLAttributeDescriptor
            } else {
                (originalElement.descriptor as? WXMLAttributeDescriptor)
            }
            attributeDescriptor?.wxmlElementAttributeDescription?.let {
                return "Element Attribute " + originalElement.parent.name + "." + it.key + " " + it.description
            }
        }
        return null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (element is JsonStringLiteral && element.containingFile.name == "elementDescriptions.json") {
            val elementName = (element.parent?.parent?.parent?.parent?.parent?.parent as? JsonProperty)?.name
                ?: return null
            val jsonObject = element.parent?.parent as? JsonObject ?: return null
            val key = jsonObject.findStringPropertyValue("key") ?: return null
            WXMLMetadata.getElementDescriptions(element.project).find {
                it.name == elementName
            }?.attributeDescriptorPresetElementAttributeDescriptors?.find {
                it.key == key
            }?.let { wxmlAttributeDescriptor ->
                val stringBuilder = StringBuilder()
                // 头部
                stringBuilder.append("<div class='definition'><pre>")
                    .append("<a href='${DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL}elements/$elementName'>$elementName</a>.$key")
                    .append("</pre>")
                if (wxmlAttributeDescriptor.description != null) {
                    stringBuilder.append("<pre>")
                        .append(wxmlAttributeDescriptor.description)
                        .append("</pre>")
                }
                stringBuilder.append("</div>")

                // 内容
                stringBuilder.append(SECTIONS_START)
                val contents = mapOf(
                    "类型" to wxmlAttributeDescriptor.types.joinToString(" | "),
                    "是否必须指定" to if (wxmlAttributeDescriptor.required) "是" else "否",
                    "默认值" to (wxmlAttributeDescriptor.default?.toString() ?: "无")
                ).toMutableMap().apply {
                    if (wxmlAttributeDescriptor.enums.isNotEmpty()) {
                        this["可选值"] = wxmlAttributeDescriptor.enums.joinToString("  ")
                    }
                }
                contents.forEach {
                    stringBuilder.append("<tr>")
                        .append(SECTION_START)
                        .append(GRAYED_START)
                        .append(it.key)
                        .append(GRAYED_END)
                        .append(SECTION_END)
                        .append(SECTION_START)
                        .append(it.value)
                        .append(SECTION_END)
                        .append("</tr>")
                }
                stringBuilder.append(SECTIONS_END)

                return stringBuilder.toString()
            }
        }
        return null
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?, link: String, element: PsiElement
    ): PsiElement? {
        if (element is JsonStringLiteral && element.containingFile.name == "elementDescriptions.json") {
            val elementName = link.removePrefix("elements/")
            val definedElement = WXMLMetadata.getElementDescriptions(element.project).find {
                it.name == elementName
            }?.definedElement ?: return null
            definedElement.putUserData(
                ORIGINAL_ELEMENT_KEY,
                SmartPointerManagerImpl.createPointer(
                    WXMLElementFactory.createTagName(element.project, elementName)
                )
            )
            return definedElement
        }
        return null
    }

}