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

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.parentOfType
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor

class MarkupTwoWayBindingReference(xmlAttribute: XmlAttribute) : PsiReferenceBase<XmlAttribute>(
    xmlAttribute,
    xmlAttribute.nameElement.textRangeInParent.let {
        it.cutOut(TextRange("model:".length, it.length))
    },
    true
) {

    override fun resolve(): PsiElement? {
        return this.getAttributeDescriptor()?.declaration
    }

    fun getAttributeDescriptor(): XmlAttributeDescriptor? {
        return this.element.parentOfType<XmlTag>()?.descriptor
            ?.getAttributeDescriptor(this.value, null)
    }

}