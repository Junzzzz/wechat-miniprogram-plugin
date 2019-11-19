package com.zxy.ijplugin.wechat_miniprogram.rename

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.RenameHandler

/**
 * TODO 重命名改变多个元素
 * @see com.intellij.psi.css.actions.rename.CssClassOrIdRenameHandler
 */
class WXSSIdRenameHandler:RenameHandler {
    override fun isAvailableOnDataContext(p0: DataContext): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(p0: Project, p1: Editor?, p2: PsiFile?, p3: DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(p0: Project, p1: Array<out PsiElement>, p2: DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}