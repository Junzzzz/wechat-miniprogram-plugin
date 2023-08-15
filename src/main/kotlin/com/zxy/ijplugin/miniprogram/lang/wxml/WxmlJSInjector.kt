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

package com.zxy.ijplugin.miniprogram.lang.wxml

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.impl.source.xml.XmlTextImpl
import com.intellij.psi.xml.XmlAttribute
import com.zxy.ijplugin.miniprogram.lang.expr.WxmlJsLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WxmlJSInjector.Companion.DOUBLE_BRACE_REGEX
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.isEventHandler
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.valueTextRangeInSelf
import com.zxy.ijplugin.miniprogram.utils.toTextRange

class WxmlJSInjector : MultiHostInjector {

    companion object {
        val DOUBLE_BRACE_REGEX = Regex("\\{\\{(.+?)}}")
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(XmlTextImpl::class.java, XmlAttributeValueImpl::class.java)
    }

    override fun getLanguagesToInject(multiHostRegistrar: MultiHostRegistrar, psiElement: PsiElement) {
        if (psiElement.language is WXMLLanguage) {
            when (psiElement) {
                is XmlTextImpl -> {
                    injectInText(psiElement, multiHostRegistrar)
                }

                is XmlAttributeValueImpl -> {
                    injectInAttributeValue(psiElement, multiHostRegistrar)
                }
            }
        }
    }

    private fun injectInAttributeValue(
        psiElement: XmlAttributeValueImpl,
        multiHostRegistrar: MultiHostRegistrar
    ) {

        val attribute = psiElement.parent as? XmlAttribute ?: return
        val element = attribute.parent
        val attributeName = attribute.name
        val tagName = element.name
        if (tagName == "wxs" || tagName == "include" || tagName == "import"
            || (tagName == "template" && attributeName == "name")
            || attributeName == "wx:for-item" || attributeName == "wx:key" || attributeName == "wx:for-index"
        ) {
            // wxs等特殊标签 标签的属性不支持 {{}} 语法
            // wx:for 相关的一些辅助属性不支持 {{}} 语法
            return
        }
        if (attribute.isEventHandler() && !DOUBLE_BRACE_REGEX.matches(psiElement.text)) {
            // 此属性是事件
            // 并且属性值中没有双括号
            multiHostRegistrar.startInjecting(WxmlJsLanguage.INSTANCE)
                .addPlace(null, "();", psiElement, psiElement.valueTextRangeInSelf())
                .doneInjecting()
        } else {
            // 对字符串中的双括号注入js语言
            searchDoubleBraceAndInject(psiElement, multiHostRegistrar)
        }
    }

    private fun injectInText(
        psiElement: XmlTextImpl,
        multiHostRegistrar: MultiHostRegistrar
    ) {
        // 2.0版本后wxs标签在Lexer中直接注入js

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
