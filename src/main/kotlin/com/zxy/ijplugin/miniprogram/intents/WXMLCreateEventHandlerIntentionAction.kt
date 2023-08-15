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

package com.zxy.ijplugin.miniprogram.intents

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
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.context.MyJSPredefinedLibraryProvider
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLUtils
import com.zxy.ijplugin.miniprogram.utils.ComponentJsUtils
import com.zxy.ijplugin.miniprogram.utils.JavaScriptElementFactory
import com.zxy.ijplugin.miniprogram.utils.addProperty

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
        val element = InjectedLanguageManager.getInstance(project).getInjectionHost(psiElement) as? XmlAttributeValue
            ?: return false
        if (editor == null) return false
        val wxmlAttribute = (element.parent as? XmlAttribute) ?: return false
        // 属性是事件属性
        if (!WXMLUtils.likeEventAttribute(wxmlAttribute.name)) return false
        // 属性值是正确的js标识符
        val text = element.value
        if (!text.matches(Regex("^[a-zA-Z_$][a-zA-Z0-9_$]*"))) return false
        val jsFile = RelateFileHolder.SCRIPT.findFile(element.containingFile.originalFile) as? JSFile ?: return false
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
        val element = InjectedLanguageManager.getInstance(project).getInjectionHost(psiElement) as? XmlAttributeValue
            ?: return
        val jsFile = RelateFileHolder.SCRIPT.findFile(element.containingFile) as? JSFile ?: return
        val callExpression = ComponentJsUtils.findComponentOrPageCallExpression(jsFile) ?: return
        val optionsObject = ComponentJsUtils.findCallExpressionFirstObjectArg(callExpression) ?: return
        val options = optionsObject.properties
        val isComponent = callExpression.methodExpression?.text == "Component"
        val text = element.value
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