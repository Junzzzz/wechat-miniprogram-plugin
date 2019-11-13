package com.zxy.ijplugin.wechat_miniprogram.reference.usage

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.lexer._WXSSLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSFindUsageProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(
                FlexAdapter(_WXSSLexer(null)),
                TokenSet.create(WXSSTypes.CLASS, WXSSTypes.ID, WXSSTypes.ELEMENT_NAME),
                TokenSet.create(WXSSTypes.COMMENT),
                TokenSet.EMPTY
        )
    }

    override fun getNodeText(psiElement: PsiElement, p1: Boolean): String {
        return (psiElement as PsiNamedElement).name ?: ""
    }

    override fun getDescriptiveName(psiElement: PsiElement): String {
        return this.getNodeText(psiElement, false)
    }

    override fun getType(psiElement: PsiElement): String {
        return if (psiElement is WXSSClassSelector || psiElement is WXSSIdSelector) {
            "selector"
        } else {
            ""
        }
    }

    override fun getHelpId(p0: PsiElement): String? {
        return null
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is PsiNamedElement
    }
}