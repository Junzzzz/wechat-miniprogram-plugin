package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.zxy.ijplugin.wechat_miniprogram.utils.substring

/**
 * WXML class 属性值的引用
 * @param textRange 此class在字符串中的位置
 */
class WXMLClassReference(psiElement:PsiElement,textRange:TextRange): PsiReferenceBase<PsiElement>(psiElement,textRange),
        PsiPolyVariantReference {
    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        val cssClass = this.element.text.substring(this.rangeInElement)
        val project = this.element.project
        val wxmlFile = this.element.containingFile.virtualFile
    }

    override fun resolve(): PsiElement? {

        return null
    }

}