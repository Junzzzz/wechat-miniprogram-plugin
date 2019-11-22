package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLModuleUtils

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
    fun getTagName(element: WXMLElement): String? {
        val wrapPsiElement = PsiTreeUtil.findChildOfType(element, WXMLStartTag::class.java)
                ?: PsiTreeUtil.findChildOfType(element, WXMLClosedElement::class.java)!!
        return wrapPsiElement.node.findChildByType(WXMLTypes.TAG_NAME)?.text
    }

    @JvmStatic
    fun getName(element: WXMLAttribute): String {
        return element.node.firstChildNode.text
    }

    /*text*/
    @JvmStatic
    fun isValidHost(element: WXMLText): Boolean {
        return true
    }

    @JvmStatic
    fun updateText(element: WXMLText, newText: String): PsiLanguageInjectionHost {
        element.replace(WXMLElementFactory.createText(element.project, newText))
        return element
    }

    @JvmStatic
    fun createLiteralTextEscaper(element: WXMLText): LiteralTextEscaper<WXMLText> {
        return LiteralTextEscaper.createSimple(element)
    }

    /*string text*/
    @JvmStatic
    fun getReferences(element: WXMLStringText): Array<PsiReference> {
        return PsiReferenceService.getService().getContributedReferences(element)
    }

    @JvmStatic
    fun isValidHost(element: WXMLStringText): Boolean {
        return true
    }

    @JvmStatic
    fun updateText(element: WXMLStringText, newText: String): PsiLanguageInjectionHost {
        element.replace(WXMLElementFactory.createStringText(element.project, newText))
        return element
    }

    @JvmStatic
    fun createLiteralTextEscaper(element: WXMLStringText): LiteralTextEscaper<WXMLStringText> {
        return LiteralTextEscaper.createSimple(element)
    }

    @JvmStatic
    fun getName(element: WXMLStringText): String? {
        return element.nameIdentifier?.text
    }

    @JvmStatic
    fun getNameIdentifier(element: WXMLStringText): PsiElement? {
        return if (WXMLModuleUtils.isTemplateNameAttributeStringText(element)) element else null
    }

    @JvmStatic
    fun setName(element: WXMLStringText, name: String):PsiElement {
        element.nameIdentifier?.replace(WXMLElementFactory.createStringText(element.project, name))
        return element
    }

}