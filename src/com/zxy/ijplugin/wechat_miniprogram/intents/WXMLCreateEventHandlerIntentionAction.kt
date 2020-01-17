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

package com.zxy.ijplugin.wechat_miniprogram.intents

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSObjectLiteralExpression
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.parentOfType
import com.zxy.ijplugin.wechat_miniprogram.context.MyJSPredefinedLibraryProvider
import com.zxy.ijplugin.wechat_miniprogram.context.findRelatePsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.JavaScriptElementFactory
import com.zxy.ijplugin.wechat_miniprogram.utils.addProperty

/**
 * 在组件对应的js文件中创建事件处理函数
 */
class WXMLCreateEventHandlerIntentionAction : IntentionAction, PsiElementBaseIntentionAction() {
    override fun getFamilyName(): String {
        return "Create Event Handler"
    }

    override fun getText(): String {
        return "Create event handler function at component js file"
    }

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        val element = InjectedLanguageManager.getInstance(project).getInjectionHost(psiElement) ?: return false
        if (element.node.elementType !== WXMLTypes.STRING_TEXT || editor == null) return false
        val wxmlAttribute = element.parentOfType<WXMLAttribute>() ?: return false
        // 属性是事件属性
        if (!wxmlAttribute.isEvent) return false
        // 属性值是正确的js标识符
        val text = element.text
        if (!text.matches(Regex("^[a-zA-Z_$][a-zA-Z0-9_$]*"))) return false

        val jsFile = findRelatePsiFile<JSFile>(element.containingFile.originalFile) ?: return false
        val callExpression = ComponentJsUtils.findComponentOrPageCallExpression(jsFile) ?: return false
        // 只要存在Page或Component方法调用切有第一个参数为对象即可
        val optionsObject = ComponentJsUtils.findCallExpressionFirstObjectArg(callExpression) ?: return false
        val options = optionsObject.properties
        val isComponent = callExpression.methodExpression?.text == "Component"
        val functions = if (isComponent) {
            (options.find {
                it.name == "methods"
            } ?: return true).let {
                it.value as? JSObjectLiteralExpression
            }?.properties ?: return false
        } else {
            options
        }.filter {
            !MyJSPredefinedLibraryProvider.PAGE_LIFETIMES.contains(it.name)
        }
        return !functions.any {
            it.name == text
        }
    }

    override fun invoke(project: Project, editor: Editor, psiElement: PsiElement) {
        val element = InjectedLanguageManager.getInstance(project).getInjectionHost(psiElement) ?: return
        val jsFile = findRelatePsiFile<JSFile>(element.containingFile) ?: return
        val callExpression = ComponentJsUtils.findComponentOrPageCallExpression(jsFile) ?: return
        val optionsObject = ComponentJsUtils.findCallExpressionFirstObjectArg(callExpression) ?: return
        val options = optionsObject.properties
        val isComponent = callExpression.methodExpression?.text == "Component"
        val text = element.text
        val property = if (isComponent) {
            val methodsProperty = options.find {
                it.name == "methods"
            }
            if (methodsProperty == null) {
                runWriteAction {
                    optionsObject.addProperty(
                            JavaScriptElementFactory.createProperty(
                                    project, """methods:{
                            ${text}(){
                                                            
                            }    
                        }""".trimMargin()
                            )
                    )
                }
            } else {
                val functionsObject = (methodsProperty.value as? JSObjectLiteralExpression ?: return)
                runWriteAction {
                    functionsObject.addProperty(
                            JavaScriptElementFactory.createProperty(
                                    project, """
                        $text(){
                                                                                    
                        }
                    """.trimIndent()
                            )
                    )
                }
            }
        } else {
            runWriteAction {
                optionsObject.addProperty(
                        JavaScriptElementFactory.createProperty(
                                project, """
                    $text(){
                        
                    }
                """.trimIndent()
                        )
                )
            }
        }
        val descriptor = OpenFileDescriptor(project, jsFile.virtualFile)
        val wxssFileEditor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true)
        wxssFileEditor?.let {
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(it.document)
            it.caretModel.moveToOffset(property.textOffset + text.length + 1)
        }
        CodeStyleManager.getInstance(project).reformat(optionsObject)
    }
}