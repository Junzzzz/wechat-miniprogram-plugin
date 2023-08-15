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

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlElementType
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile

class WXMLExtractComponentIntentionAction : PsiElementBaseIntentionAction() {
    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun getFamilyName(): String {
        return "Extract WXML Code Segment"
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (!isWechatMiniProgramContext(project) || element.containingFile is WXMLPsiFile) {
            return false
        }
        return getSelectedTags(element, editor).isNotEmpty()
    }

    override fun getText(): String {
        return "Extract Component"
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        editor ?: return
        val selectedTags = getSelectedTags(element, editor)
        WXMLExtractComponentRefactoring(project, selectedTags, editor).perform()
    }

    private fun getSelectedTags(element: PsiElement, editor: Editor?): List<XmlTag> {
        val file = element.containingFile ?: return emptyList()
        if (editor == null || !editor.selectionModel.hasSelection()) {
            // 如果当前没有选择元素
            val type = element.node.elementType
            val parent = element.parent as? XmlTag
            if (parent != null && (type == XmlElementType.XML_NAME ||
                        type == XmlElementType.XML_START_TAG_START ||
                        type == XmlElementType.XML_TAG_NAME)
            ) {
                return listOf(parent)
            }
            if (element is XmlTag) return listOf(element)
            return emptyList()
        }
        var start = editor.selectionModel.selectionStart
        val end = editor.selectionModel.selectionEnd

        val list = mutableListOf<XmlTag>()
        while (start < end) {
            while (file.findElementAt(start) is PsiWhiteSpace && start < end) start++
            if (start == end) break
            val tag = PsiTreeUtil.findElementOfClassAtOffset(file, start, XmlTag::class.java, true)
                ?: return emptyList()
            val textRange = tag.textRange
            if (textRange.startOffset !in start until end) break
            if (textRange.endOffset > end) return emptyList()
            list.add(tag)
            start = textRange.endOffset
        }
        return list
    }
}