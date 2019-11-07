package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.formatter

import com.intellij.formatting.*
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement

class WXMLFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(psiElement: PsiElement, codeStyleSettings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                psiElement.containingFile,
                object : AbstractBlock(
                        psiElement.node, Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment()
                ) {
                    override fun isLeaf(): Boolean {
                        return false
                    }

                    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
                        return null
                    }

                    override fun buildChildren(): MutableList<Block> {
                        return psiElement.children
                                .mapNotNull {
                                    if (it is WXMLElement) {
                                        WXMLElementBlock(it, codeStyleSettings)
                                    } else {
                                        WXMLLeafBlock.createLeafBlockForIgnoredNode(it.node)
                                    }
                                }
                                .toMutableList()
                    }

                },
                codeStyleSettings
        )
    }

}