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

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlElement
import com.intellij.xml.XmlAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPresetElementAttributeDescription

class WXMLAttributeDescriptor(val wxmlElementAttributeDescription: WXMLPresetElementAttributeDescription) :
    XmlAttributeDescriptor {
    override fun getDefaultValue(): String? {
        return this.wxmlElementAttributeDescription.default?.toString()
    }

    override fun validateValue(p0: XmlElement?, p1: String?): String? {
        return null
    }

    override fun getName(p0: PsiElement?): String {
        return wxmlElementAttributeDescription.key
    }

    override fun getName(): String {
        return wxmlElementAttributeDescription.key
    }

    override fun isRequired(): Boolean {
        return wxmlElementAttributeDescription.required
    }

    override fun hasIdRefType(): Boolean {
        return false
    }

    override fun init(p0: PsiElement?) {

    }

    override fun isFixed(): Boolean {
        return true
    }

    override fun getDeclaration(): PsiElement? {
        return this.wxmlElementAttributeDescription.definedElement
    }

    override fun isEnumerated(): Boolean {
        return this.wxmlElementAttributeDescription.enums.isNotEmpty()
    }

    override fun getEnumeratedValues(): Array<String>? {
        return this.wxmlElementAttributeDescription.enums
    }

    override fun hasIdType(): Boolean {
        return false
    }
}