package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceService

open class WXMLTag(node:ASTNode) : ContributedReferenceHost, ASTWrapperPsiElement(node) {

    override fun getReferences(): Array<PsiReference> {
        return PsiReferenceService.getService().getContributedReferences(this)
    }

    open fun getTagNameNode():ASTNode?{
        return this.node.findChildByType(WXMLTypes.TAG_NAME)
    }

    open fun getTagName():String?{
        return this.getTagNameNode()?.text
    }

    override fun getReference(): PsiReference? {
        return this.references.firstOrNull()
    }
}