package com.zxy.ijplugin.wechat_miniprogram.reference.usage

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText

class WXMLFindUsageProvider:FindUsagesProvider {

    override fun getNodeText(psiElement: PsiElement, p1: Boolean): String {
        return (psiElement as PsiNamedElement).name ?: ""
    }

    override fun getDescriptiveName(psiElement: PsiElement): String {
        return this.getNodeText(psiElement, false)
    }

    override fun getType(psiElement: PsiElement): String {
        if (psiElement is WXMLStringText){
            return psiElement.text
        }
        return ""
    }

    override fun getHelpId(p0: PsiElement): String? {
        return null
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is WXMLStringText
    }
}