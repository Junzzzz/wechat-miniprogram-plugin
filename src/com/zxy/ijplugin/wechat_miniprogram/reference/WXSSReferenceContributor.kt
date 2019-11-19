package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSImport
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSStringText

class WXSSReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        // 解析@import字符串中的路径
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(WXSSStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<out PsiReference> {
                        if (PsiTreeUtil.getParentOfType(psiElement, WXSSImport::class.java) != null) {
                            return FileReferenceSet(psiElement).allReferences
                        }
                        return PsiReference.EMPTY_ARRAY
                    }

                }
        )
    }
}