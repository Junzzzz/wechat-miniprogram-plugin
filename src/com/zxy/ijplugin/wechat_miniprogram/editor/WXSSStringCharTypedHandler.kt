package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSStringCharTypedHandler : TypedHandlerDelegate() {

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        // 当键入一个字符串的引号时
        // 如果光标后面存在字符串的结束字符
        // 那么阻止输入并将光标移出改字符串
        if ((c=='"'||c=='\'')&& file.language===WXSSLanguage.INSTANCE){
            val viewProvider = file.viewProvider
            val offset = editor.caretModel.offset
            val rightElement = viewProvider.findElementAt(offset)?:return Result.CONTINUE
            if ((c=='"'&&rightElement.node.elementType===WXSSTypes.STRING_END_DQ) || (c=='\''&&rightElement.node.elementType===WXSSTypes.STRING_END_SQ)){
                editor.caretModel.moveToOffset(offset+1)
                return Result.STOP
            }
        }
        return Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        // 当键入一个字符串的起始符号时
        // 自动完成字符串的结束
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return Result.CONTINUE
        if ((c == '"' || c == '\'') && psiFile.language == WXSSLanguage.INSTANCE) {
            val viewProvider = psiFile.viewProvider
            val offset = editor.caretModel.offset
            val inputCharElement = viewProvider.findElementAt(offset - 1) ?: return Result.CONTINUE
            if (c == '\'' && inputCharElement.node.elementType == WXSSTypes.STRING_START_SQ && !isClosedString(
                            inputCharElement
                    )) {
                // 自动补全单引号
                editor.document.insertString(offset, "'")
            } else if (c == '"' && inputCharElement.node.elementType == WXSSTypes.STRING_START_DQ && !isClosedString(
                            inputCharElement
                    )) {
                // 自动补全双引号
                editor.document.insertString(offset, "\"")
            }
        }
        return Result.CONTINUE
    }

    private fun isClosedString(element: PsiElement): Boolean {
        val stringEndElementType = when {
            element.node.elementType == WXSSTypes.STRING_START_SQ -> WXSSTypes.STRING_END_SQ
            element.node.elementType == WXSSTypes.STRING_START_DQ -> WXSSTypes.STRING_END_DQ
            else -> error("element is'not string start")
        }
        val next = element.node.treeNext ?: return false
        return if (next.elementType == stringEndElementType) {
            true
        } else {
            next.elementType == WXSSTypes.STRING_CONTENT && next.treeNext.elementType == stringEndElementType
        }
    }
}