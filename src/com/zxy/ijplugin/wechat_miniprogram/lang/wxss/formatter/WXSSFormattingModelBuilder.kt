package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(psiElement: PsiElement, codeStyleSettings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                psiElement.containingFile, WXSSBlock(
                psiElement.node, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, false),
                Alignment.createAlignment(),
                SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                        .around(WXSSTypes.VALUE)
                        .spaceIf(true)
        ),
                codeStyleSettings
        )
    }

}