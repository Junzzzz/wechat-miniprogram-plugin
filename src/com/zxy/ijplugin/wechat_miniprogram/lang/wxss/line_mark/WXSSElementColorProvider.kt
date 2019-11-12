package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.line_mark

import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorHexUtil
import com.intellij.xml.util.ColorMap
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory
import java.awt.Color

class WXSSElementColorProvider : ElementColorProvider {

    override fun setColorTo(psiElement: PsiElement, color: Color) {

        val hex = String.format("#%02x%02x%02x", color.red, color.blue, color.green).toUpperCase()
        psiElement.replace(WXSSElementFactory.createValue(psiElement.project, hex))
    }

    override fun getColorFrom(psiElement: PsiElement): Color? {
        if (psiElement.node.elementType == WXSSTypes.HASH) {
            return ColorHexUtil.fromHex(psiElement.text)
        } else if (psiElement.node.elementType == WXSSTypes.ATTRIBUTE_VALUE_LITERAL) {
            return ColorMap.getColor(psiElement.text)
        }
        return null
    }

}