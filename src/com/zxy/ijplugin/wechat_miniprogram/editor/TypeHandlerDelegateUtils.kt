package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

object TypeHandlerDelegateUtils {
    fun commitDocumentAndGetPsiFile(
            project: Project, editor: Editor
    ): PsiFile? {
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        return  PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
    }
}