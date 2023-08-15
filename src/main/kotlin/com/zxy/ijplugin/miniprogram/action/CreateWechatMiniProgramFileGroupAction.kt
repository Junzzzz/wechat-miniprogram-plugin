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

package com.zxy.ijplugin.miniprogram.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.utils.ComponentFilesCreator.createWechatComponentFiles

abstract class CreateWechatMiniProgramFileGroupAction<T : DialogWrapper> : WechatAction() {

    final override fun actionPerformed(anActionEvent: AnActionEvent) {
        val project = anActionEvent.project ?: return
        val psiDirectory = getPsiDirectory(anActionEvent) ?: return

        val dialog = this.createDialog(project)
        if (dialog.showAndGet()) {
            onDialogConfirm(dialog)

            val fileName = this.getFileName()
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    // 通过template创建js和json文件
                    createWechatComponentFiles(
                        fileName, psiDirectory,
                        this.getJsComponentTemplateName(),
                        this.getJsonComponentTemplate()
                    )
                    this.created(psiDirectory)
                    val wxmlFile = psiDirectory.findFile(
                        "$fileName." + if (project.isQQContext()) QMLFileType.INSTANCE.defaultExtension else WXMLFileType.INSTANCE.defaultExtension
                    )!!
                    // 当文件创建完成之后  wxmlFile 获得焦点
                    FileEditorManager.getInstance(project).openFile(wxmlFile.virtualFile, true)
                } catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages
                            .showErrorDialog(
                                project,
                                this.getMessage() + "\n" + e.localizedMessage,
                                getActionName()
                            )
                    }
                }
            }
        }
    }

    open fun created(psiDirectory: PsiDirectory) {

    }

    abstract fun onDialogConfirm(dialog: T)

    abstract fun createDialog(project: Project): T

    abstract fun getMessage(): String

    abstract fun getJsComponentTemplateName(): String

    abstract fun getJsonComponentTemplate(): String

    abstract fun getFileName(): String

    abstract fun getActionName(): String

    private fun getPsiDirectory(
        anActionEvent: AnActionEvent
    ): PsiDirectory? {
        return LangDataKeys.IDE_VIEW.getData(anActionEvent.dataContext)?.orChooseDirectory
    }
}