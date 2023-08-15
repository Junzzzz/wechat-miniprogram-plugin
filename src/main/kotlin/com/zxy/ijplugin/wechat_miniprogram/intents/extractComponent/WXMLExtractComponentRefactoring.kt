/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.wechat_miniprogram.intents.extractComponent

import com.intellij.json.psi.JsonFile
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.zxy.ijplugin.wechat_miniprogram.action.CreateWechatMiniProgramComponentAction.Companion.JSON_TEMPLATE_NAME
import com.zxy.ijplugin.wechat_miniprogram.action.CreateWechatMiniProgramComponentAction.Companion.JS_TEMPLATE_NAME
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentFilesCreator
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsonUtils

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
                !CommonRefactoringUtil.checkReadOnlyStatus(project, containingFile)) return

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