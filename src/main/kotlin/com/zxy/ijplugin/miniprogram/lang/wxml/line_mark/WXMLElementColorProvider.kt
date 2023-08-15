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

package com.zxy.ijplugin.miniprogram.lang.wxml.line_mark

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlToken
import com.intellij.xml.util.ColorMap
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.utils.findChildOfType
import java.awt.Color
import java.util.*

class WXMLElementColorProvider : ElementColorProvider {
    override fun setColorTo(psiElement: PsiElement, color: Color) {
        val hex = String.format("#%02x%02x%02x", color.red, color.blue, color.green).uppercase(Locale.ENGLISH)
        val xmlToken = PsiFileFactory.getInstance(psiElement.project).createFileFromText(
            "dummy.wxml", WXMLFileType.INSTANCE, """
            <a k="$hex"></a>
        """.trimIndent()
        ).findChildOfType<XmlAttributeValue>()!!.children[1] as XmlToken
        psiElement.replace(xmlToken)
    }

    override fun getColorFrom(psiElement: PsiElement): Color? {
        if (psiElement is XmlToken && isWechatMiniProgramContext(psiElement)) {
            val attributeValue = psiElement.parent as? XmlAttributeValue ?: return null
            val xmlAttribute = attributeValue.parent as? XmlAttribute ?: return null
            val name = xmlAttribute.name
            if (name.endsWith("color") || name.endsWith("Color")) {
                // 在属性值的字符串中
                // 并且属性包含color
                return ColorMap.getColor(psiElement.text)
            }
        }
        return null
    }

}