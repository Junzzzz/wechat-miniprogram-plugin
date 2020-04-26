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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.attributes

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.impl.source.xml.XmlAttributeReference
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.tag.WXMLElementDescriptor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.tag.WxmlCustomComponentDescriptor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLAttributeInsertUtils
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLUtils
import com.zxy.ijplugin.wechat_miniprogram.qq.QMLFileType

class WXMLAttributeNameCompletionProvider : CompletionProvider<CompletionParameters>() {

    companion object {
        val STRUCTURE_ATTRIBUTES = arrayOf("for", "elif", "else", "key", "if")

        val NO_VALUE_ATTRIBUTE = "else"

        /**
         * 忽略公共的属性的标签名
         */
        val IGNORE_COMMON_ATTRIBUTE_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include", "slot")

        /**
         * 忽略wx属性的标签名
         */
        val IGNORE_WX_ATTRIBUTE_TAG_NAMES = arrayOf("template", "wxs", "import", "include")

        /**
         * 忽略公共事件的标签名
         */
        val IGNORE_COMMON_EVENT_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include", "slot")
    }

    override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
    ) {
        val containingFile = parameters.position.containingFile
        if (containingFile !is WXMLPsiFile) return
        val reference = containingFile
                .findReferenceAt(parameters.offset)
        if (reference is XmlAttributeReference) {
            val xmlTag = reference.element.parent
            val descriptor = xmlTag.descriptor
            val xmlAttributes = xmlTag.attributes.map { it.name }.toTypedArray()
            if (descriptor is WxmlCustomComponentDescriptor) {
                // 自定义组件的属性
                val customComponentAttributeDescriptors = WXMLUtils.getCustomComponentAttributeDescriptors(descriptor)
                result.addAllElements(customComponentAttributeDescriptors.filter { desc ->
                    xmlAttributes.none {
                        it == desc.name
                    }
                }.map {
                    if (WXMLAttributeInsertUtils.isBooleanTypeAttribute(it) && it.defaultValue == "false") {
                        LookupElementBuilder.create(it.name)
                    } else {
                        LookupElementBuilder.create(it.name)
                                .withInsertHandler(WXMLAttributeNameInsertHandler.DoubleQuotaInsertHandler())
                    }
                })
                addTwoWayBindings(
                        result, xmlAttributes, customComponentAttributeDescriptors
                        .map {
                            it.name
                        }.toTypedArray()
                )

                addStructureAttribute(result, xmlAttributes, containingFile)
                addCommonAttribute(result, xmlAttributes)
                addCommonEvents(result, xmlAttributes)
            } else if (descriptor is WXMLElementDescriptor) {
                val attributes = descriptor.wxmlElementDescription.attributeDescriptorPresetElementAttributeDescriptors
                // wxml组件属性
                attributes.filter { desc ->
                    xmlAttributes.none {
                        it == desc.key
                    }
                }.map {
                    val insertHandler = WXMLAttributeNameInsertHandler.createFromAttributeDescription(it)
                    LookupElementBuilder.create(it.key).withInsertHandler(insertHandler)
                }.let {
                    result.addAllElements(it)
                }

                addTwoWayBindings(result, xmlAttributes, attributes.map {
                    it.key
                }.toTypedArray())

                // wxml组件事件
                descriptor.wxmlElementDescription.events.filter { event ->
                    xmlAttributes.none { attr ->
                        WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.any {
                            attr == it + event
                        }
                    }
                }.flatMap { event ->
                    WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.map {
                        it + event
                    }
                }.map {
                    LookupElementBuilder.create(it)
                            .withInsertHandler(WXMLAttributeNameInsertHandler.DoubleQuotaInsertHandler())
                }.let {
                    result.addAllElements(it)
                }

                val tagName = descriptor.name
                if (!IGNORE_WX_ATTRIBUTE_TAG_NAMES.contains(tagName)) {
                    // 提供固定的wx前缀完成
                    addStructureAttribute(result, xmlAttributes, containingFile)
                }

                if (!IGNORE_COMMON_ATTRIBUTE_TAG_NAMES.contains(tagName)) {
                    // 公共属性
                    addCommonAttribute(result, xmlAttributes)
                }

                if (!IGNORE_COMMON_EVENT_TAG_NAMES.contains(tagName)) {
                    // 公共事件
                    addCommonEvents(result, xmlAttributes)
                }
            }

            // mark:
            if (!IGNORE_COMMON_ATTRIBUTE_TAG_NAMES.contains(xmlTag.name)) {
                result.addElement(
                        LookupElementBuilder.create("mark:")
                                .withInsertHandler(WXMLAttributeNameInsertHandler.DoubleQuotaInsertHandler())
                )
            }

        }
    }

    private fun addTwoWayBindings(
            result: CompletionResultSet, xmlAttributes: Array<String>, attributes: Array<String>
    ) {
        // 简易双向绑定
        // https://developers.weixin.qq.com/miniprogram/dev/framework/view/two-way-bindings.html
        result.addAllElements(attributes
                .filter { attribute ->
                    xmlAttributes.none {
                        it == attribute
                    }
                }.map {
                    "model:$it"
                }.filter { modelAttribute ->
                    xmlAttributes.none {
                        it == modelAttribute
                    }
                }.map {
                    LookupElementBuilder.create(
                            it
                    ).withInsertHandler(WXMLAttributeNameInsertHandler.DoubleBraceInsertHandler())
                })
    }

    private fun addCommonEvents(result: CompletionResultSet, xmlAttributes: Array<String>) {
        result.addAllElements(
                WXMLUtils.generateEventAttributeFullName(WXMLMetadata.COMMON_ELEMENT_EVENTS).filter { event ->
                    xmlAttributes.none { it == event }
                }.map {
                    LookupElementBuilder.create(it)
                }
        )
    }

    private fun addCommonAttribute(
            result: CompletionResultSet,
            xmlAttributes: Array<out String>
    ) {
        result.addAllElements(
                WXMLMetadata.COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS.filter { attribute -> xmlAttributes.none { it == attribute.key } }
                        .map {
                            LookupElementBuilder.create(it.key).withInsertHandler(
                                    WXMLAttributeNameInsertHandler.createFromAttributeDescription(it)
                            )
                        })
    }

    private fun addStructureAttribute(
            result: CompletionResultSet,
            xmlAttributes: Array<out String>,
            file: WXMLPsiFile
    ) {
        val isQQ = file.fileType === QMLFileType.INSTANCE
        result.addAllElements(
                STRUCTURE_ATTRIBUTES.filter { attribute ->
                    xmlAttributes.none {
                        it == "wx:$attribute" || (isQQ && it == "qq:$attribute")
                    }
                }.flatMap {
                    if (isQQ) {
                        listOf("qq:$it", "wx:$it")
                    } else {
                        listOf("wx:$it")
                    }
                }.map {
                    if (it.endsWith(NO_VALUE_ATTRIBUTE)) {
                        LookupElementBuilder.create(it)
                    } else {
                        LookupElementBuilder.create(it).withInsertHandler(
                                WXMLAttributeNameInsertHandler.DoubleBraceInsertHandler()
                        )
                    }
                })
    }


}