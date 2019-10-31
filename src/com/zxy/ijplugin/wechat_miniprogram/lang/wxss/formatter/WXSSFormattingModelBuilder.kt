package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings

class WXSSFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(psiElement: PsiElement, codeStyleSettings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                psiElement.containingFile, WXSSBlockFactory.createBlock(psiElement.node,codeStyleSettings),
                codeStyleSettings
        )
    }

}