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
import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlTextImpl
import com.intellij.psi.util.parentOfType
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.context.isQQContext

/**
 * 在wxml中的wxs标签中注入js语言
 */
class WxmlWxsInjector : MultiHostInjector {

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context.language is WXMLLanguage && context is XmlTextImpl) {
            val xmlTag = context.parentOfType<XmlTag>()
            val scriptTagName = if (context.project.isQQContext()) "qs" else "wxs"
            if (xmlTag != null && xmlTag.name == scriptTagName) {
                registrar.startInjecting(JavascriptLanguage.INSTANCE)
                    .addPlace(null, null, context, TextRange.from(0, context.textLength))
                    .doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(XmlTextImpl::class.java)
    }

}