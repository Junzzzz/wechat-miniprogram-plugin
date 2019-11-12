package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        if (psiElement is WXMLStringText) {
                            val attribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                            if (attribute != null && attribute.name == "id"
                                    && psiElement.nextSibling.elementType == WXMLTypes.STRING_END
                                    && psiElement.prevSibling.elementType == WXMLTypes.STRING_START) {
                                // 这个字符串内容必须在 id中
                                // 并且没有整个字符串没有表达式
                                return arrayOf(WXMLIdReference(psiElement))
                            }
                        }

                        return PsiReference.EMPTY_ARRAY
//                        Regex("[_\\-a-zA-Z][_\\-a-zA-Z0-9]+").findAll(psiElement.text)
                    }
                }
        )
    }
}