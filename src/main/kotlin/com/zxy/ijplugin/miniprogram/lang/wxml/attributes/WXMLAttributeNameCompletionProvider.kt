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

package com.zxy.ijplugin.miniprogram.lang.wxml.attributes

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.impl.source.xml.XmlAttributeReference
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.tag.WXMLElementDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.tag.WxmlCustomComponentDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLAttributeInsertUtils
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLUtils
import com.zxy.ijplugin.miniprogram.qq.QMLFileType

class WXMLAttributeNameCompletionProvider : CompletionProvider<CompletionParameters>() {

    companion object {
        val STRUCTURE_ATTRIBUTES = arrayOf("for", "elif", "else", "key", "if", "for-index", "for-item")

        const val NO_VALUE_ATTRIBUTE = "else"

        val NO_BRACE_ATTRIBUTES = arrayOf("for-index", "for-item", "key")

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
                        it == desc.name || it == "model:" + desc.name
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
                        it == desc.key || it == "model:" + desc.key
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
                    when {
                        it.endsWith(NO_VALUE_ATTRIBUTE) -> {
                            LookupElementBuilder.create(it)
                        }
                        NO_BRACE_ATTRIBUTES.any { noBraceAttribute ->
                            it.endsWith(noBraceAttribute)
                        } -> {
                            LookupElementBuilder.create(it)
                                    .withInsertHandler(WXMLAttributeNameInsertHandler.DoubleQuotaInsertHandler())
                        }
                        else -> {
                            LookupElementBuilder.create(it).withInsertHandler(
                                    WXMLAttributeNameInsertHandler.DoubleBraceInsertHandler()
                            )
                        }
                    }
                })
    }


}