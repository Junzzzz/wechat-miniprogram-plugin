package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult

/**
 * WXML class 属性值的引用
 * @param textRange 此class在字符串中的位置
 */
class WXMLClassReference(psiElement:PsiElement,textRange:TextRange): PsiReferenceBase<PsiElement>(psiElement,textRange),
        PsiPolyVariantReference {
    override fun resolve(): PsiElement? {
//        val cssClass = this.element.text.substring(textRange)
//        val project = this.element.project
//        val wxmlFile = this.element.containingFile.virtualFile
        return null
    }

    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}