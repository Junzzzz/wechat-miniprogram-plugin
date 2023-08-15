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

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlElement
import com.intellij.xml.XmlAttributeDescriptor

class WXMLCustomComponentAttributeDescriptor(private val element: JSProperty) : XmlAttributeDescriptor {

    override fun getDefaultValue(): String? {
        val value = this.declaration.value
        if (value is JSObjectLiteralExpression) {
            val valueValue = value.findProperty("value")?.value
            return if (valueValue is JSLiteralExpression) {
                valueValue.value?.toString()
            } else {
                valueValue?.text
            }
        }
        return null
    }

    override fun validateValue(context: XmlElement?, value: String?): String? {
        return null
    }

    override fun getName(context: PsiElement?): String {
        return this.name
    }

    override fun getName(): String {
        return this.element.name ?: ""
    }

    override fun isRequired(): Boolean {
        return false
    }

    override fun hasIdRefType(): Boolean {
        return false
    }

    override fun init(element: PsiElement?) {

    }

    override fun isFixed(): Boolean {
        return false
    }

    override fun getDeclaration(): JSProperty {
        return this.element
    }

    override fun isEnumerated(): Boolean {
        return false
    }

    override fun getEnumeratedValues(): Array<String>? {
        return null
    }

    override fun hasIdType(): Boolean {
        return false
    }
}