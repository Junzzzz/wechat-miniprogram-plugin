/*
 *     Copyright (c) [2019] [zxy]
 *     [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *     You can use this software according to the terms and conditions of the Mulan PSL v1.
 *     You may obtain a copy of Mulan PSL v1 at:
 *         http://license.coscl.org.cn/MulanPSL
 *     THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *     PURPOSE.
 *     See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.miniprogram.intents

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.css.CssElementFactory
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile

abstract class WXMLCreateSelectorAtWxssFileIntentionAction : IntentionAction, PsiElementBaseIntentionAction() {

    protected abstract val wxssPsiFile: WXSSPsiFile

    final override fun startInWriteAction(): Boolean {
        return true
    }

    abstract fun getSelectorText(): String

    final override fun invoke(project: Project, editor: Editor?, p2: PsiElement) {
        val selectorText = this.getSelectorText()

        val styleDefinition = CssElementFactory.getInstance(project)
            .createRuleset("$selectorText{\n\t\n}", WXSSLanguage.INSTANCE)
        this.wxssPsiFile.add(
            styleDefinition
        )
        // 将光标移动到样式定义的花括号中间
        val descriptor = OpenFileDescriptor(project, wxssPsiFile.virtualFile)
        val wxssFileEditor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true)
        styleDefinition.navigate(true)
        wxssFileEditor?.caretModel?.moveToOffset(wxssPsiFile.textLength - 2)
        // 格式化代码
        val psiDocumentManager = PsiDocumentManager.getInstance(project)
        psiDocumentManager.doPostponedOperationsAndUnblockDocument(
            psiDocumentManager.getDocument(this.wxssPsiFile) ?: return
        )
        CodeStyleManager.getInstance(project).reformat(this.wxssPsiFile)
    }

}