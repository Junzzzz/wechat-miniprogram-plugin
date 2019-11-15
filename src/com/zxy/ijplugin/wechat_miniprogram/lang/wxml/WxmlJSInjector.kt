package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.lang.javascript.JavaScriptSupportLoader
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.WxmlJsLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLOpenedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLText
import com.zxy.ijplugin.wechat_miniprogram.utils.toTextRange

class WxmlJSInjector : MultiHostInjector {
    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(WXMLText::class.java, WXMLStringText::class.java)
    }

    override fun getLanguagesToInject(multiHostRegistrar: MultiHostRegistrar, psiElement: PsiElement) {
        if (psiElement is WXMLText) {
            val wxmlOpenedElement = PsiTreeUtil.getParentOfType(psiElement, WXMLOpenedElement::class.java)
            if (wxmlOpenedElement != null) {
                if ((wxmlOpenedElement.parent as WXMLElement).tagName == "wxs") {
                    // 对wxs标签注入js语言
                    multiHostRegistrar.startInjecting(JavaScriptSupportLoader.JAVASCRIPT_1_5)
                            .addPlace(null, null, psiElement, TextRange(0, psiElement.textLength))
                            .doneInjecting()
                } else {
                    // 对元素内容中的双括号注入js语言
                    searchDoubleBraceAndInject(psiElement, multiHostRegistrar)
                }
            }
        } else if (psiElement is WXMLStringText) {
            // 对字符串中的双括号注入js语言
            searchDoubleBraceAndInject(psiElement, multiHostRegistrar)
        }
    }
}

private fun searchDoubleBraceAndInject(
        psiElement: PsiLanguageInjectionHost,
        multiHostRegistrar: MultiHostRegistrar
) {
    val inserts = Regex("\\{\\{.+?}}").findAll(psiElement.text)
    inserts.forEach {
        multiHostRegistrar.startInjecting(WxmlJsLanguage.INSTANCE)
                .addPlace(null, null, psiElement, it.range.toTextRange())
                .doneInjecting()
    }
}
