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

package com.zxy.ijplugin.miniprogram.lang.wxml.tag

import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlElementDescriptor
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLUtils

/**
 * [https://developers.weixin.qq.com/miniprogram/dev/reference/api/Component.html]
 */
class WxmlCustomComponentDescriptor(private val element: JsonProperty) : WXMLBasicElementDescriptor() {
    override fun getDefaultValue(): String? {
        return null
    }

    override fun getName(context: PsiElement?): String {
        return this.name
    }

    override fun getName(): String {
        return this.element.name
    }

    override fun getElementsDescriptors(context: XmlTag?): Array<XmlElementDescriptor> {
        return emptyArray()
    }

    override fun init(element: PsiElement?) {

    }

    override fun getDefaultName(): String {
        return this.name
    }


    override fun getQualifiedName(): String {
        return (this.element.value as? JsonStringLiteral)?.value ?: this.name
    }


    override fun getDeclaration(): JsonProperty {
        return this.element
    }

    override fun getAttributeDescriptor(attributeName: String, context: XmlTag?): XmlAttributeDescriptor? {
        return super.getAttributeDescriptor(attributeName, context) ?: WXMLUtils.getCustomComponentAttributeDescriptors(
            this
        ).find {
            it.name == attributeName
        } ?: AnyXmlAttributeDescriptor(attributeName)
    }

}