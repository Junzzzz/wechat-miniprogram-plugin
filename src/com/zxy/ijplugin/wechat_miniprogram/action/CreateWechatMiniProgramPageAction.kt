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
import com.intellij.ui.layout.panel
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * 创建微信小程序页面
 * 可选是否使用Component API
 */
class CreateWechatMiniProgramPageAction : WechatAction() {

    companion object{
        const val NAME = "Create Wechat Mini Program Page"
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val project = anActionEvent.project ?: return
        val psiDirectory = getPsiDirectory(anActionEvent) ?: return

        val dialog = Dialog(project)
        if (dialog.showAndGet()) {
            val pageName = dialog.pageName
            val fileTemplateManager = FileTemplateManager.getInstance(project)
            val fileTemplateDefaultProps = fileTemplateManager.defaultProperties
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val apiName = if (dialog.useComponentApi) "Component" else "Page"
                    val jsComponentTemplate = fileTemplateManager.getInternalTemplate(
                            "Wechat mini program page js file($apiName API)"
                    )
                    FileTemplateUtil.createFromTemplate(
                            jsComponentTemplate, pageName,
                            fileTemplateDefaultProps, psiDirectory
                    )
                    val jsonComponentTemplate = fileTemplateManager.getInternalTemplate(
                            "Wechat mini program page json file($apiName API)"
                    )
                    FileTemplateUtil.createFromTemplate(
                            jsonComponentTemplate, pageName, fileTemplateDefaultProps,
                            psiDirectory
                    )
                    val wxmlFile = PsiFileFactory.getInstance(project).createFileFromText(pageName+"."+WXMLFileType.INSTANCE.defaultExtension,WXMLLanguage.INSTANCE, "")
                    val wxssFile = PsiFileFactory.getInstance(project).createFileFromText(pageName+"."+WXSSFileType.INSTANCE.defaultExtension,WXSSLanguage.INSTANCE, "")
                    psiDirectory.add(wxmlFile)
                    psiDirectory.add(wxssFile)

                    // 当文件创建完成之后  wxmlFile 获得焦点
                    FileEditorManager.getInstance(project).openFile(wxmlFile.virtualFile,true)
                } catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages
                                .showErrorDialog(
                                        project,
                                        "Unable to create page " + pageName + "\n" + e.localizedMessage,
                                        NAME
                                )
                    }
                }
            }
        }

    }

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

    class Dialog(project: Project) : DialogWrapper(project, false) {

        /**
         * 是否使用ComponentApi
         */
        var useComponentApi = false

        var pageName = ""

        init {
            init()
            this.title = NAME
        }

        private lateinit var preferredFocusedComponent: JComponent

        override fun createCenterPanel(): JComponent? {
            return panel {
                row {
                    label("Page name:")
                    JTextField("")
                    preferredFocusedComponent = textField({ pageName }, { pageName = it }).component
                    preferredFocusedComponent
                }
                row {
                    checkBox("Use component API",{useComponentApi},{useComponentApi=it})
                }
            }
        }

        override fun getPreferredFocusedComponent(): JComponent? {
            return preferredFocusedComponent
        }
    }

}