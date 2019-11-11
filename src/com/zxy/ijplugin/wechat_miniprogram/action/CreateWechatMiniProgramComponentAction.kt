package com.zxy.ijplugin.wechat_miniprogram.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * 创建微信小程序组件
 */
class CreateWechatMiniProgramComponentAction :
        CreateWechatMiniProgramFileGroupAction<CreateWechatMiniProgramComponentAction.Dialog>() {

    companion object {
        const val NAME = "Create Wechat Mini Program Component"
    }

    private lateinit var componentName: String

    override fun onDialogConfirm(dialog: Dialog) {
        this.componentName = dialog.componentName
    }

    override fun createDialog(project: Project): Dialog {
        return Dialog(project)
    }

    override fun getMessage(): String {
        return "Unable to create page $componentName"
    }

    override fun getJsComponentTemplateName(): String {
        return "Wechat mini program component js file"
    }

    override fun getJsonComponentTemplate(): String {
        return "Wechat mini program component json file"
    }

    override fun getFileName(): String {
        return this.componentName
    }

    override fun getActionName(): String {
        return NAME
    }

    class Dialog(project: Project) : DialogWrapper(project) {

        var componentName = ""

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
                    preferredFocusedComponent = textField({ componentName }, { componentName = it }).component
                    preferredFocusedComponent
                }
            }
        }

        override fun getPreferredFocusedComponent(): JComponent? {
            return preferredFocusedComponent
        }

    }
}