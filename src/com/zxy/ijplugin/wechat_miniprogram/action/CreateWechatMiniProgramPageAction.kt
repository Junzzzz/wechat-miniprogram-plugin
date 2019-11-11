package com.zxy.ijplugin.wechat_miniprogram.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JTextField
import kotlin.properties.Delegates

/**
 * 创建微信小程序页面
 * 可选是否使用Component API
 */
class CreateWechatMiniProgramPageAction : CreateWechatMiniProgramFileGroupAction<CreateWechatMiniProgramPageAction.Dialog>() {
    override fun getActionName(): String {
        return NAME
    }

    private lateinit var pageName:String

    private var isComponentApi by Delegates.notNull<Boolean>()

    private val apiName:String
    get() {
        return  if (this.isComponentApi) "Component" else "Page"
    }

    override fun onDialogConfirm(dialog: Dialog) {
        isComponentApi = dialog.useComponentApi
        pageName = dialog.pageName
    }

    override fun createDialog(project: Project): Dialog {
        return Dialog(project)
    }

    override fun getMessage(): String {
        return "Unable to create page $pageName"
    }

    override fun getJsComponentTemplateName(): String {
        return "Wechat mini program page js file($apiName API)"
    }

    override fun getJsonComponentTemplate(): String {
        return "Wechat mini program page json file($apiName API)"
    }

    override fun getFileName(): String {
        return pageName
    }

    companion object{
        const val NAME = "Create Wechat Mini Program Page"
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