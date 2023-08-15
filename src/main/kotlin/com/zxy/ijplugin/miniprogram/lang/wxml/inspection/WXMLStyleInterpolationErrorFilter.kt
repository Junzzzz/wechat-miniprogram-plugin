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

package com.zxy.ijplugin.miniprogram.lang.wxml.inspection

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.parentOfType
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage

/**
 * 过滤掉对style属性中的插值的红色高亮显示
 */
class WXMLStyleInterpolationErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(psiErrorElement: PsiErrorElement): Boolean {
        val containingFile = psiErrorElement.containingFile
        if (containingFile.language == WXMLLanguage.INSTANCE) {
            val xmlAttributeValue = psiErrorElement.parentOfType<XmlAttributeValue>() ?: return false
            val xmlAttribute = xmlAttributeValue.parentOfType<XmlAttribute>() ?: return false
            val valueTextRange = xmlAttributeValue.valueTextRange
            if (xmlAttribute.name == "style") {
                return (valueTextRange.startOffset..valueTextRange.endOffset).none {
                    InjectedLanguageManager.getInstance(psiErrorElement.project)
                        .findInjectedElementAt(psiErrorElement.containingFile, it) != null
                }
            }
        }
        return true
    }
}