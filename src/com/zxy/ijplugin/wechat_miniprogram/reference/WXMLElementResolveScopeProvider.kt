package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.lang.javascript.psi.resolve.JSElementResolveScopeProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope

class WXMLElementResolveScopeProvider : JSElementResolveScopeProvider {
    override fun getElementResolveScope(element: PsiElement): GlobalSearchScope? {
        return null
    }
}