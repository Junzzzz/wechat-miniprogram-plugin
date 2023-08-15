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

import com.intellij.json.psi.JsonFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.miniprogram.utils.getPathRelativeToRootRemoveExt
import javax.swing.JComponent
import kotlin.properties.Delegates

/**
 * 创建微信小程序页面
 * 可选是否使用Component API
 */
class CreateWechatMiniProgramPageAction :
    CreateWechatMiniProgramFileGroupAction<CreateWechatMiniProgramPageAction.Dialog>() {
    override fun getActionName(): String {
        return NAME
    }

    private lateinit var pageName: String

    private var isComponentApi by Delegates.notNull<Boolean>()

    private val apiName: String
        get() {
            return if (this.isComponentApi) "Component" else "Page"
        }

    override fun created(psiDirectory: PsiDirectory) {
        val project = psiDirectory.project
        val dirPath = psiDirectory.virtualFile.getPathRelativeToRootRemoveExt(
            project
        )?.removePrefix("/")
        if (dirPath != null) {
            RelateFileHolder.JSON.findAppFile(project)?.let {
                val jsonPsiFile = it as? JsonFile
                if (jsonPsiFile != null) {
                    // 在app.json中注册该页面
                    AppJsonUtils.registerPage(jsonPsiFile, "$dirPath/$pageName")
                }
            }
        }
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

    companion object {
        const val NAME = "Wechat Mini Program Page"
    }

    class Dialog(project: Project) : DialogWrapper(project, false) {

        /**
         * 是否使用ComponentApi
         */
        var useComponentApi = false

        var pageName = ""

        init {
            init()
            this.title = "Create $NAME"
        }

        private lateinit var preferredFocusedComponent: JComponent

        override fun createCenterPanel(): JComponent? {
            return panel {
                row {
                    label("Page name:")
                    textField().apply {
                        this.bindText({ pageName }, { pageName = it })
                        preferredFocusedComponent = this.component
                    }
                }
                row {
                    checkBox("Use component API").bindSelected({ useComponentApi }, { useComponentApi = it })
                }
            }
        }

        override fun getPreferredFocusedComponent(): JComponent? {
            return preferredFocusedComponent
        }
    }

}