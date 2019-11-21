package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.WxmlJsLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WxmlJSInjector.Companion.DOUBLE_BRACE_REGEX
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.isEventHandler
import com.zxy.ijplugin.wechat_miniprogram.utils.toTextRange

class WxmlJSInjector : MultiHostInjector {

    companion object {
        val DOUBLE_BRACE_REGEX = Regex("\\{\\{(.+?)}}")
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(WXMLText::class.java, WXMLStringText::class.java)
    }

    override fun getLanguagesToInject(multiHostRegistrar: MultiHostRegistrar, psiElement: PsiElement) {
        when (psiElement) {
            is WXMLText -> {
                injectInText(psiElement, multiHostRegistrar)
            }
            is WXMLStringText -> {
                injectInStringText(psiElement, multiHostRegistrar)
            }
        }
    }

    private fun injectInStringText(
            psiElement: WXMLStringText,
            multiHostRegistrar: MultiHostRegistrar
    ) {
        val element = PsiTreeUtil.getParentOfType(psiElement, WXMLElement::class.java)
        if (element == null || element.tagName == "wxs") {
            // wxs 标签的属性不支持 {{}} 语法
            return
        }
        if (PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)?.let {
                    it.isEventHandler()
                } == true && !DOUBLE_BRACE_REGEX.matches(psiElement.text)) {
            // 此属性是事件
            // 并且属性值中没有双括号
            multiHostRegistrar.startInjecting(WxmlJsLanguage.INSTANCE)
                    .addPlace(null, null, psiElement, TextRange(0, psiElement.textLength))
                    .doneInjecting()
        } else {
            // 对字符串中的双括号注入js语言
            searchDoubleBraceAndInject(psiElement, multiHostRegistrar)
        }
    }

    private fun injectInText(
            psiElement: WXMLText,
            multiHostRegistrar: MultiHostRegistrar
    ) {
        val wxmlOpenedElement = PsiTreeUtil.getParentOfType(psiElement, WXMLOpenedElement::class.java)
        if (wxmlOpenedElement != null) {
            if ((wxmlOpenedElement.parent as WXMLElement).tagName == "wxs") {
                // 对wxs标签注入js语言
                multiHostRegistrar.startInjecting(WxmlJsLanguage.INSTANCE)
                        .addPlace(null, null, psiElement, TextRange(0, psiElement.textLength))
                        .doneInjecting()
                return
            }
        }

        // 对文本中的双括号注入js语言
        searchDoubleBraceAndInject(psiElement, multiHostRegistrar)
    }
}

private fun searchDoubleBraceAndInject(
        psiElement: PsiLanguageInjectionHost,
        multiHostRegistrar: MultiHostRegistrar
) {
    val inserts = DOUBLE_BRACE_REGEX.findAll(psiElement.text)
    inserts.forEach {
        val range = it.groups[1]?.range
        if (range != null) {
            multiHostRegistrar.startInjecting(WxmlJsLanguage.INSTANCE)
                    .addPlace(null, null, psiElement, range.toTextRange())
                    .doneInjecting()
        }
    }
}
