package com.zxy.ijplugin.wechat_miniprogram.action

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage

abstract class CreateWechatMiniProgramFileGroupAction<T : DialogWrapper> : WechatAction() {

    final override fun actionPerformed(anActionEvent: AnActionEvent) {
        val project = anActionEvent.project ?: return
        val psiDirectory = getPsiDirectory(anActionEvent) ?: return

        val dialog = this.createDialog(project)
        if (dialog.showAndGet()) {
            onDialogConfirm(dialog)

            val fileName = this.getFileName()
            val fileTemplateManager = FileTemplateManager.getInstance(project)
            val fileTemplateDefaultProps = fileTemplateManager.defaultProperties
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    // 通过template创建js和json文件
                    val jsComponentTemplate = fileTemplateManager.getInternalTemplate(
                            this.getJsComponentTemplateName()
                    )
                    FileTemplateUtil.createFromTemplate(
                            jsComponentTemplate, fileName,
                            fileTemplateDefaultProps, psiDirectory
                    )
                    val jsonComponentTemplate = fileTemplateManager.getInternalTemplate(
                            this.getJsonComponentTemplate()
                    )
                    FileTemplateUtil.createFromTemplate(
                            jsonComponentTemplate, fileName, fileTemplateDefaultProps,
                            psiDirectory
                    )

                    // 创建空的wxml和wxss文件
                    val wxmlFile = PsiFileFactory.getInstance(project).createFileFromText(
                            fileName + "." + WXMLFileType.INSTANCE.defaultExtension,
                            WXMLLanguage.INSTANCE, ""
                    )
                    val wxssFile = PsiFileFactory.getInstance(project).createFileFromText(
                            fileName + "." + WXSSFileType.INSTANCE.defaultExtension,
                            WXSSLanguage.INSTANCE, ""
                    )
                    psiDirectory.add(wxmlFile)
                    psiDirectory.add(wxssFile)

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

    abstract fun onDialogConfirm(dialog: T)

    abstract fun createDialog(project: Project): T

    abstract fun getMessage(): String

    abstract fun getJsComponentTemplateName(): String

    abstract fun getJsonComponentTemplate(): String

    abstract fun getFileName():String

    abstract fun getActionName():String

    private fun getPsiDirectory(
            anActionEvent: AnActionEvent
    ): PsiDirectory? {
//        val psiElement = LangDataKeys.PSI_ELEMENT.getData(anActionEvent.dataContext)?:return null
//        if (psiElement is PsiDirectory){
//            return psiElement
//        }else if (psiElement.containingFile!=null){
//            return psiElement
//        }
        return LangDataKeys.IDE_VIEW.getData(anActionEvent.dataContext)?.orChooseDirectory
    }
}