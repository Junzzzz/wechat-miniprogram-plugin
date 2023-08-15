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

import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.xml.XmlAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLElementAttributeDescription
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLCustomComponentAttributeDescriptor

object WXMLAttributeInsertUtils {


    @JvmStatic
    fun isBooleanTypeAttribute(xmlAttributeDescriptor: XmlAttributeDescriptor): Boolean {
        if (xmlAttributeDescriptor is WXMLAttributeDescriptor) {
            val wxmlElementAttributeDescription = xmlAttributeDescriptor.wxmlElementAttributeDescription
            if ((wxmlElementAttributeDescription.types.size == 1) && wxmlElementAttributeDescription.default == true) {
                return true
            }
        } else if (xmlAttributeDescriptor is WXMLCustomComponentAttributeDescriptor) {
            val jsProperty = xmlAttributeDescriptor.declaration
            val propertyConfig = jsProperty.value
            if (propertyConfig is JSObjectLiteralExpression) {
                return propertyConfig.findProperty("type")?.value?.text == "Boolean"
            } else if (propertyConfig is JSReferenceExpression) {
                return propertyConfig.text == "Boolean"
            }
        }
        return false
    }

}

fun WXMLElementAttributeDescription.isJsTypeAttribute(): Boolean {
    return this.types.let {
        it.contains(WXMLElementAttributeDescription.ValueType.NUMBER)
                ||
                it.contains(WXMLElementAttributeDescription.ValueType.ARRAY)
                ||
                it.contains(WXMLElementAttributeDescription.ValueType.OBJECT)
    }
}