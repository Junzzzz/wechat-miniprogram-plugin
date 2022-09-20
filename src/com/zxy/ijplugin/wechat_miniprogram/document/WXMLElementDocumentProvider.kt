/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

@file:Suppress("DEPRECATION")

package com.zxy.ijplugin.wechat_miniprogram.document

import com.intellij.codeInsight.documentation.DocumentationManager.ORIGINAL_ELEMENT_KEY
import com.intellij.codeInsight.documentation.DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.smartPointers.SmartPointerManagerImpl
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLElementDescription
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.tag.WXMLElementDescriptor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory

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