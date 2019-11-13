package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector

class WXSSClassSelectorSelfReference(wxssClassSelector: WXSSClassSelector, textRange: TextRange) :
        PsiReferenceBase.Immediate<PsiElement>(wxssClassSelector, textRange, true, wxssClassSelector) {
    override fun handleElementRename(newElementName: String): PsiElement {
        return (this.element as PsiNamedElement).setName(newElementName)
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return element.isEquivalentTo(this.element)
                || (element is WXSSClassSelector && (this.element as WXSSClassSelector).className == element.className)
    }
}