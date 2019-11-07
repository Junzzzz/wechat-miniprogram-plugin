package com.zxy.ijplugin.wechat_miniprogram.breadcrumbs

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement

/**
 * 对WXML标签提供下方面包屑导航
 */
class WXMLElementBreadcrumbsProvider : BreadcrumbsProvider {
    override fun getLanguages(): Array<Language> {
        return arrayOf(WXMLLanguage.INSTANCE)
    }

    override fun getElementInfo(element: PsiElement): String {
        return (element as WXMLElement).tagName
    }

    override fun acceptElement(element: PsiElement): Boolean {
        return element is WXMLElement
    }
}