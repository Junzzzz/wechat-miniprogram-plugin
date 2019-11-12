package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.line_mark

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.xml.util.ColorMap
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory
import java.awt.Color

class WXMLElementColorProvider : ElementColorProvider {
    override fun setColorTo(psiElement: PsiElement, color: Color) {
        val hex = String.format("#%02x%02x%02x", color.red, color.blue, color.green).toUpperCase()
        psiElement.replace(WXMLElementFactory.createStringText(psiElement.project, hex))
    }

    override fun getColorFrom(psiElement: PsiElement): Color? {
        if (psiElement is WXMLStringText) {
            return ColorMap.getColor(psiElement.text)
        }
        return null
    }


}