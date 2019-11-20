package com.zxy.ijplugin.wechat_miniprogram.rename

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage

/**
 * TODO 重命名改变多个元素
 * @see com.intellij.psi.css.actions.rename.CssClassOrIdRenameHandler
 */
class WXSSIdRenameHandler : PsiElementRenameHandler() {
    override fun isAvailableOnDataContext(dataContext: DataContext): Boolean {
        val editor = CommonDataKeys.EDITOR.getData(
                dataContext
        ) ?: return false
        val psiFile = CommonDataKeys.PSI_FILE.getData(dataContext) ?: return false
        if (psiFile.language == WXMLLanguage.INSTANCE || WXSSLanguage.INSTANCE == psiFile.language) {
            return true
        }
        return false
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
        super.invoke(project, elements, dataContext)
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext) {
        super.invoke(project, editor, file, dataContext)
    }
}