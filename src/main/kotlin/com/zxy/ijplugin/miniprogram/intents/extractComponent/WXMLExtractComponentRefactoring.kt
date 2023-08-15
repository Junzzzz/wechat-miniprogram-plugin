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

package com.zxy.ijplugin.miniprogram.intents.extractComponent

import com.intellij.json.psi.JsonFile
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.zxy.ijplugin.miniprogram.action.CreateWechatMiniProgramComponentAction.Companion.JSON_TEMPLATE_NAME
import com.zxy.ijplugin.miniprogram.action.CreateWechatMiniProgramComponentAction.Companion.JS_TEMPLATE_NAME
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.utils.ComponentFilesCreator
import com.zxy.ijplugin.miniprogram.utils.ComponentJsonUtils

class WXMLExtractComponentRefactoring(
    private val project: Project,
    private val list: List<XmlTag>,
    private val editor: Editor
) {

    fun perform() {
        if (list.isEmpty()) return
        val containingFile = list[0].containingFile
        val psiDirectory = containingFile.parent
        if (containingFile == null ||
            psiDirectory == null ||
            !CommonRefactoringUtil.checkReadOnlyStatus(project, containingFile)
        ) return

        // 询问组件名称
        val askComponentNameDialog = Messages.InputDialog(
            "Enter new component name", "Extract Component", null, null, object : InputValidator {
                override fun checkInput(inputString: String?): Boolean {
                    return inputString != null && inputString.matches(Regex("[a-z-_]+"))
                }

                override fun canClose(inputString: String?): Boolean {
                    return checkInput(inputString)
                }
            })
        if (askComponentNameDialog.showAndGet()) {
            runWriteAction {
                val componentName = askComponentNameDialog.inputString!!

                // 创建组件文件
                ComponentFilesCreator.createWechatComponentFiles(
                    componentName, psiDirectory, JS_TEMPLATE_NAME,
                    JSON_TEMPLATE_NAME,
                    list.joinToString("\n") {
                        it.text
                    }
                )

                editor.selectionModel.removeSelection()

                val startOffset = list.first().textRange.startOffset
                // 删除原来选择的标签
                editor.document.deleteString(startOffset, list.last().textRange.endOffset)

                // 添加组件元素
                editor.document.insertString(startOffset, "<$componentName></$componentName>")

                // 在json文件中注册此组件
                (RelateFileHolder.JSON.findFile(containingFile) as? JsonFile)?.let {
                    (psiDirectory.findFile("$componentName.json") as? JsonFile)?.let { targetComponentJsonFile ->
                        ComponentJsonUtils.registerComponent(it, targetComponentJsonFile)
                    }
                }
            }

        }

    }

}