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

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTokenType
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.reference.WXMLClassReference


abstract class WXMLCreateClassAtWxssFileIntentionAction : WXMLCreateSelectorAtWxssFileIntentionAction() {

    protected lateinit var className: String

    override lateinit var wxssPsiFile: WXSSPsiFile

    final override fun getFamilyName(): String {
        return "Create Class Selector"
    }

    override fun getSelectorText(): String {
        return ".${this.className}"
    }


}

private fun isAvailableElementAndEditor(
    psiElement: PsiElement, editor: Editor?
) = psiElement.node.elementType !== XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN || editor == null

class WXMLCreateClassAtComponentWxssFileIntentionAction : WXMLCreateClassAtWxssFileIntentionAction() {

    override fun getText(): String {
        return "Create Class Selector At Component WXSS File"
    }

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        if (isAvailableElementAndEditor(psiElement, editor)) return false
        val reference = psiElement.containingFile.findReferenceAt(editor!!.caretModel.offset)
        if (reference is WXMLClassReference) {
            if (reference.multiResolve(false).isEmpty()) {
                val className = reference.canonicalText
                val psiFile = RelateFileHolder.STYLE.findFile(psiElement.containingFile)
                val wxssPsiFile = psiFile?.let {
                    it as? WXSSPsiFile
                } ?: return false
                super.wxssPsiFile = wxssPsiFile
                super.className = className
                return true
            }
        }
        return false
    }

}

class WXMLCreateClassAtAppWxssFileIntentionAction : WXMLCreateClassAtWxssFileIntentionAction() {

    override fun getText(): String {
        return "Create Class Selector At app.wxss"
    }

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        if (isAvailableElementAndEditor(psiElement, editor)) return false
        val reference = psiElement.containingFile.findReferenceAt(editor!!.caretModel.offset)
        if (reference is WXMLClassReference && reference.multiResolve(false).isEmpty()) {
            RelateFileHolder.STYLE.findAppFile(psiElement.project)?.let {
                it as WXSSPsiFile
            }?.let {
                super.wxssPsiFile = it
                super.className = reference.canonicalText
                return true
            }
        }
        return false

    }
}