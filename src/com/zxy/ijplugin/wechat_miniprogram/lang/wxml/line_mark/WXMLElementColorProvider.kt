package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.line_mark

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.xml.util.ColorMap
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory
import java.awt.Color

class WXMLElementColorProvider : ElementColorProvider {
    override fun setColorTo(psiElement: PsiElement, color: Color) {
        val hex = String.format("#%02x%02x%02x", color.red, color.blue, color.green).toUpperCase()
        psiElement.replace(WXMLElementFactory.createStringText(psiElement.project, hex))
    }

    override fun getColorFrom(psiElement: PsiElement): Color? {
        if (psiElement is WXMLStringText && PsiTreeUtil.getParentOfType(
                        psiElement, WXMLAttribute::class.java
                )?.name?.contains("color") == true) {
            // 在属性值的字符串中
            // 并且属性包含color
            return ColorMap.getColor(psiElement.text)
        }
        return null
    }


}