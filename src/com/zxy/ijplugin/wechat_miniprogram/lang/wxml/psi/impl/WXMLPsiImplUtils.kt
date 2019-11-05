package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl

import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*

object WXMLPsiImplUtils {

    @JvmStatic
    fun getEndTag(element: WXMLStartTag): WXMLEndTag? {
        do {
            val next = element.nextSibling ?: return null
            if (next is WXMLEndTag) {
                return next
            }
        } while (true)
    }

    @JvmStatic
    fun getStartTag(element: WXMLEndTag): WXMLStartTag? {
        do {
            val prev = element.prevSibling ?: return null
            if (prev is WXMLStartTag) {
                return prev
            }
        } while (true)
    }

    @JvmStatic
    fun getTagName(element:WXMLElement): String {
        val wrapPsiElement = PsiTreeUtil.findChildOfType(element,WXMLStartTag::class.java)?:PsiTreeUtil.findChildOfType(element,WXMLClosedElement::class.java)!!
        return wrapPsiElement.node.findChildByType(WXMLTypes.TAG_NAME)!!.text
    }

    @JvmStatic
    fun getName(element:WXMLAttribute):String{
        return element.node.firstChildNode.text
    }

}