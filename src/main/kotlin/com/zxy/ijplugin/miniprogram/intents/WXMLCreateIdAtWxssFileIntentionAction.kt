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
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlTokenType
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.reference.WXMLIdReference

class WXMLCreateIdAtWxssFileIntentionAction : WXMLCreateSelectorAtWxssFileIntentionAction() {
    private lateinit var smartPsiElementPointer: SmartPsiElementPointer<WXSSPsiFile>

    private lateinit var id: String

    override val wxssPsiFile: WXSSPsiFile
        get() = this.smartPsiElementPointer.element!!

    override fun getSelectorText(): String {
        return "#$id"
    }

    override fun getFamilyName(): String {
        return "Create Id Selector"
    }

    override fun getText(): String {
        return "Create Id Selector At Component WXSS File"
    }

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        if (psiElement.node.elementType !== XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN || editor == null) return false
        val reference = psiElement.containingFile.findReferenceAt(editor.caretModel.offset)
        if (reference is WXMLIdReference && reference.multiResolve(false).isEmpty()) {
            RelateFileHolder.STYLE.findFile(psiElement.containingFile.originalFile)?.let {
                it as WXSSPsiFile
            }?.let {
                this.smartPsiElementPointer = SmartPointerManager.createPointer(it)
                this.id = reference.canonicalText
                return true
            }
        }
        return false
    }
}