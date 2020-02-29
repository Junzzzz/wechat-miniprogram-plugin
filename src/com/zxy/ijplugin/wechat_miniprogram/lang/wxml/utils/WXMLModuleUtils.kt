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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.wxmlToXmlDeprecatedMessage

object WXMLModuleUtils {

    /**
     * 从一个wxml文件以及找到一个template的定义
     * 包含其导入的文件
     */
    @Deprecated(wxmlToXmlDeprecatedMessage)
    fun findTemplateDefinition(wxmlPsiFile: WXMLPsiFile, templateName: String): PsiElement? {
        val elements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java)
        // 在本文件中找
        this.findTemplateDefinitionWithSingleFile(elements, templateName)?.let {
            return it
        }
        // 找到所有的import的文件引用
        val fileReferences = findImportedFileReferences(elements)
        for (fileReference in fileReferences) {
            val resolveFile = fileReference.resolve()
            if (resolveFile is WXMLPsiFile) {
                findTemplateDefinitionWithSingleFile(resolveFile, templateName).let {
                    return it
                }
            }
        }
        return null
    }

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
                findTemplateDefinitionOnlySingleFile(resolveFile, templateName).let {
                    return it
                }
            }
        }
        return null
    }

    private fun findSrcImportedFileReferences(
            elements: Collection<XmlTag>
    ): Sequence<FileReference> {
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
    }

    @Deprecated(wxmlToXmlDeprecatedMessage)
    private fun findImportedFileReferences(
            elements: Collection<WXMLElement>
    ): Sequence<FileReference> {
        return elements.asSequence().filter {
            it.tagName == "import"
        }.map { wxmlElement ->
            PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                it.name == "src"
            }
        }.mapNotNull {
            PsiTreeUtil.findChildOfType(it, WXMLStringText::class.java)
        }.mapNotNull { wxmlStringText ->
            wxmlStringText.references.findLast {
                it is FileReference
            }
        }.filterIsInstance<FileReference>()
    }

    @Deprecated(wxmlToXmlDeprecatedMessage)
    private fun findTemplateDefinitionWithSingleFile(wxmlPsiFile: WXMLPsiFile, templateName: String): WXMLStringText? {
        return this.findTemplateDefinitionWithSingleFile(
                PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java), templateName
        )
    }

    private fun findTemplateDefinitionOnlySingleFile(
            wxmlPsiFile: WXMLPsiFile, templateName: String
    ): XmlAttributeValue? {
        return this.findTemplateDefinitionWithSingleFile(
                PsiTreeUtil.findChildrenOfType(wxmlPsiFile, XmlTag::class.java), templateName
        )
    }

    @Deprecated(wxmlToXmlDeprecatedMessage)
    private fun findTemplateDefinitionWithSingleFile(
            wxmlElements: Collection<WXMLElement>, templateName: String
    ): WXMLStringText? {
        val templateElements = wxmlElements.filter {
            it.tagName == "template"
        }
        for (templateElement in templateElements) {
            val attributes = PsiTreeUtil.findChildrenOfType(templateElement, WXMLAttribute::class.java)
            val nameAttribute = attributes.find {
                it.name == "name"
            }
            if (nameAttribute != null) {
                val stringText = PsiTreeUtil.findChildOfType(
                        nameAttribute, WXMLStringText::class.java
                )
                if (stringText != null && stringText.text == templateName) {
                    return stringText
                }
            }
        }
        return null
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

    fun findTemplateDefinitions(wxmlPsiFile: WXMLPsiFile): List<WXMLStringText> {
        val wxmlElements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java)
        return findTemplateDefinitions(wxmlElements)
    }

    private fun findTemplateDefinitions(wxmlElements: Collection<WXMLElement>): List<WXMLStringText> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.tagName == "template"
        }.mapNotNull { wxmlElement ->
            PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                it.name == "name"
            }
        }.mapNotNull {
            PsiTreeUtil.findChildOfType(
                    it, WXMLStringText::class.java
            )
        }
    }

    fun findValidIncludeTags(wxmlElements: Collection<WXMLElement>): List<WXMLElement> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.tagName == "include" && PsiTreeUtil.findChildrenOfType(
                    wxmlElement, WXMLAttribute::class.java
            ).any {
                it.name == "src" && PsiTreeUtil.findChildOfType(
                        it, WXMLStringText::class.java
                )?.references?.lastOrNull()?.resolve() is WXMLPsiFile
            }
        }
    }

    fun isTemplateNameAttributeStringText(wxmlStringText: WXMLStringText): Boolean {
        return isMatchTagNameAndAttributeName(wxmlStringText, "template", "name")
    }

    fun isSlotNameAttributeStringText(wxmlStringText: WXMLStringText): Boolean {
        return isMatchTagNameAndAttributeName(wxmlStringText, "slot", "name")
    }

    private fun isMatchTagNameAndAttributeName(
            wxmlStringText: WXMLStringText, tagName: String, attributeName: String
    ): Boolean {
        return PsiTreeUtil.getParentOfType(wxmlStringText, WXMLAttribute::class.java)?.let {
            it.name == attributeName && PsiTreeUtil.getParentOfType(it, WXMLElement::class.java)?.tagName == tagName
        } == true
    }

    fun findTemplateDefinitionsWithImports(wxmlPsiFile: WXMLPsiFile): List<WXMLStringText> {
        val results = arrayListOf<WXMLStringText>()
        val elements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java)
        results.addAll(findTemplateDefinitions(elements))
        val fileReferences = this.findImportedFileReferences(elements)
        results.addAll(fileReferences.mapNotNull { it.resolve()  }.mapNotNull { it as? WXMLPsiFile }.toList().toTypedArray().flatMap {
            findTemplateDefinitions(it)
        })
        return results
    }

}