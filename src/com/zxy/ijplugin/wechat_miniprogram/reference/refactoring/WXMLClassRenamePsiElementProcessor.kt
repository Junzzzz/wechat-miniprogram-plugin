package com.zxy.ijplugin.wechat_miniprogram.reference.refactoring

import com.intellij.psi.PsiElement
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector

class WXMLClassRenamePsiElementProcessor: RenamePsiElementProcessor() {
    override fun canProcessElement(psiElement: PsiElement): Boolean {
        return psiElement is WXSSClassSelector
    }

    override fun renameElement(
            element: PsiElement, newName: String, usages: Array<out UsageInfo>, listener: RefactoringElementListener?
    ) {

        super.renameElement(element, newName, usages, listener)
    }

}