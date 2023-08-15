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

import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTokenType
import com.intellij.xml.XmlAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLCustomComponentAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.tag.WXMLElementDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.tag.WxmlCustomComponentDescriptor
import com.zxy.ijplugin.miniprogram.utils.ComponentJsUtils
import com.zxy.ijplugin.miniprogram.utils.ComponentWxmlUtils

object WXMLUtils {

    @JvmStatic
    fun getWXMLAttributeDescriptors(tag: XmlTag?): Array<XmlAttributeDescriptor> {
        val result = mutableSetOf<XmlAttributeDescriptor>()
        val xmlElementDescriptor = tag?.descriptor ?: return emptyArray()
        if (xmlElementDescriptor is WxmlCustomComponentDescriptor) {
            result.addAll(this.getCustomComponentAttributeDescriptors(xmlElementDescriptor))
        } else if (xmlElementDescriptor is WXMLElementDescriptor) {
            val wxmlElementDescription = xmlElementDescriptor.wxmlElementDescription
            wxmlElementDescription.attributeDescriptorPresetElementAttributeDescriptors.map {
                WXMLAttributeDescriptor(it)
            }.let {
                result.addAll(it)
            }
        }
        return result.toTypedArray()
    }

    /**
     * 根据属性名粗略判断是否是事件
     */
    @JvmStatic
    fun likeEventAttribute(attributeName: String): Boolean {
        return WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.any { attributeName.startsWith(it) }
    }

    fun generateEventAttributeFullName(eventNames: Array<String>): List<String> {
        return eventNames.flatMap { eventName ->
            WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.map {
                it + eventName
            }
        }
    }

    fun getCustomComponentAttributeDescriptors(
        wxmlCustomComponentDescriptor: WxmlCustomComponentDescriptor
    ): Array<WXMLCustomComponentAttributeDescriptor> {
        return ComponentWxmlUtils.findCustomComponentDefinitionJsFile(
            wxmlCustomComponentDescriptor.declaration
        )?.let { jsFile ->
            ComponentJsUtils.findPropertiesItems(jsFile)
        }?.map {
            WXMLCustomComponentAttributeDescriptor(it)
        }?.toTypedArray() ?: emptyArray()
    }

    fun isCommonAttributes(attributeName: String): Boolean {
        return WXMLMetadata.COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS.any {
            it.key === attributeName
        }
    }
}

fun XmlAttribute.isEventHandler(): Boolean {
    val name = this.name
    return WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.any {
        name.startsWith(it)
    }
}

fun XmlAttributeValue.valueTextRangeInSelf(): TextRange {
    return TextRange.create(1, this.value.length + 1)
}

fun XmlTag.nameTextRangeInSelf(): TextRange? {
    return this.children.find { it.elementType == XmlTokenType.XML_NAME }?.textRangeInParent
}