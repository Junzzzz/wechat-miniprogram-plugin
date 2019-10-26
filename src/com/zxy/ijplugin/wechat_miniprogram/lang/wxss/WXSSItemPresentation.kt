package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.presentation.java.SymbolPresentationUtil

abstract class WXSSItemPresentation(private val element: PsiElement) : ItemPresentation {

    override fun getLocationString(): String? {
        val filePathPresentation = SymbolPresentationUtil.getFilePathPresentation(element.containingFile)
        val psiDocumentManager = PsiDocumentManager.getInstance(element.project)
        val document = psiDocumentManager.getDocument(element.containingFile)
                ?: return filePathPresentation
        return filePathPresentation + ":" + document.getLineNumber(element.textOffset)
    }

    override fun getPresentableText(): String? {
        return element.text
    }
}