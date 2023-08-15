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

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

/**
 * 创建微信小程序组件
 */
class CreateWechatMiniProgramComponentAction :
    CreateWechatMiniProgramFileGroupAction<CreateWechatMiniProgramComponentAction.Dialog>() {

    companion object {
        const val NAME = "Wechat Mini Program Component"
        const val JSON_TEMPLATE_NAME = "Wechat mini program component json file"
        const val JS_TEMPLATE_NAME = "Wechat mini program component js file"
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
            this.title = "Create $NAME"
        }

        private lateinit var preferredFocusedComponent: JComponent

        override fun createCenterPanel(): JComponent? {
            return panel {
                row {
                    label("Component name:")
                    textField().bindText({ componentName }, { componentName = it }).apply {
                        preferredFocusedComponent = this.component
                    }
                }
            }
        }

        override fun getPreferredFocusedComponent(): JComponent? {
            return preferredFocusedComponent
        }

    }
}