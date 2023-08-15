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
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLModuleUtils
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.valueTextRangeInSelf

class WXMLTemplateIsAttributeReference(element: XmlAttributeValue) :
    PsiReferenceBase<XmlAttributeValue>(element, element.valueTextRangeInSelf()) {

    override fun resolve(): PsiElement? {
        val wxmlPsiFile = this.element.containingFile
        return WXMLModuleUtils.findTemplateDefinitionXmlAttributeValue(wxmlPsiFile as WXMLPsiFile, this.value)
    }

    override fun getVariants(): Array<Any> {
        return WXMLModuleUtils.findTemplateDefinitionsWithImports(element.containingFile as WXMLPsiFile).toTypedArray()
    }

    override fun isSoft(): Boolean {
        return false
    }

}