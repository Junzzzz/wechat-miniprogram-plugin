package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReferenceBase as PsiPolyVariantReferenceBase1

abstract class MyMultiReference<T : PsiElement>(element: T, range: TextRange? = null, soft: Boolean = false) :
        PsiPolyVariantReferenceBase1<T>(element, range, soft) {

    override fun resolve(): PsiElement? {
        val results = this.multiResolve(false)
        return results.getOrNull(0)?.element
    }
}