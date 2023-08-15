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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.valueTextRangeInSelf
import com.zxy.ijplugin.miniprogram.utils.ComponentWxmlUtils

/**
 * 使用自定义组件时，子元素的slot属性引用该组件中定义的slot标签的name属性
 */
class WXMLNamedSlotReference(element: XmlAttributeValue) :
    PsiReferenceBase<XmlAttributeValue>(element, element.valueTextRangeInSelf()) {

    override fun resolve(): PsiElement? {
        val slotName = element.value
        return getNamedSlots()?.find {
            it.value == slotName
        }
    }

    private fun getNamedSlots(): List<XmlAttributeValue>? {
        return (element.parent as? XmlAttribute)?.parent?.parentTag?.let {
            ComponentWxmlUtils.findCustomComponentDefinitionWxmlFile(it)
        }?.let {
            PsiTreeUtil.findChildrenOfType(it, XmlTag::class.java)
        }?.asSequence()?.filter { xmlTag ->
            xmlTag.name == "slot"
        }?.mapNotNull { xmlTag ->
            xmlTag.attributes.find {
                it.name == "name"
            }
        }?.mapNotNull {
            it.valueElement
        }?.toList()
    }

    override fun getVariants(): Array<Any> {
        return (getNamedSlots() ?: emptyList()).toTypedArray()
    }

}