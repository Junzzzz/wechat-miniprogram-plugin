package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.ASTNode
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiPolyVariantReferenceBase
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

    /**
     * 如果这是自定义组件
     * 那么获取它所在的js文件
     */
    fun getDefinitionJsFile():JSFile?{
        val componentNameJsonLiteral = this.reference?.resolve() as? JsonStringLiteral
        val lastComponentPathReference = (componentNameJsonLiteral?.parent as? JsonProperty)?.value?.references?.lastOrNull() as? PsiPolyVariantReferenceBase<*>
        return lastComponentPathReference?.multiResolve(
                false
        )?.mapNotNull { it.element }?.find { it is JSFile } as? JSFile
    }
}