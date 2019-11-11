package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findPrevSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*

class WXMLTagCloseTypedHandler : TypedHandlerDelegate() {

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        // 当键入一个斜杠时
        // 根据上下文，完成未闭合的标签
        if (c == '/' && file.language === WXMLLanguage.INSTANCE) {
            val offset = editor.caretModel.offset
            val prevElement = file.viewProvider.findElementAt(offset - 1)
            val wxmlOpenedElement = if (prevElement is PsiWhiteSpace) {
                // 处于空格处
                // 看前一个元素是不是开标签
                PsiTreeUtil.findChildOfType(
                        prevElement.findPrevSibling { it is WXMLElement }, WXMLOpenedElement::class.java
                )
            } else {
                // 处于开元素的最后位置
                PsiTreeUtil.getParentOfType(
                        prevElement, WXMLOpenedElement::class.java
                )
            }

            if (wxmlOpenedElement!=null && wxmlOpenedElement.textRange.endOffset <= offset) {
                // 单标签
                // 完成闭合标签
                editor.document.insertString(offset, "/>")
                editor.caretModel.moveToOffset(offset + 2)
                return Result.STOP
            }
            val element = file.viewProvider.findElementAt(offset)
            if (element!=null && element.elementType==WXMLTypes.START_TAG_END){
                val wxmlStartTag = PsiTreeUtil.getParentOfType(element,WXMLStartTag::class.java)
                val nextSibling = wxmlStartTag?.nextSibling
                if (nextSibling is WXMLEndTag){
                    val range = nextSibling.textRange
                    // 删除结束标签
                    // 让这个标签变成闭合标签
                    editor.document.deleteString(range.startOffset,range.endOffset)
                    return Result.CONTINUE
                }
            }
        }
        return Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {

        if (file.language === WXMLLanguage.INSTANCE) {
            val psiFile = TypeHandlerDelegateUtils.commitDocumentAndGetPsiFile(project, editor)
                    ?: return Result.CONTINUE
            val viewProvider = psiFile.viewProvider
            val offset = editor.caretModel.offset
            val inputCharElement = viewProvider.findElementAt(offset - 1) ?: return Result.CONTINUE
            if (c == '/') {
                // 当键入一个斜杠时
                // 根据上下文，完成未闭合的标签
                if (inputCharElement.node.elementType === WXMLTypes.END_TAG_START) {
                    // 键入的结束标签的头部 </
                    val prevStartTagName = getPrevStartTagName(inputCharElement)
                    if (prevStartTagName != null) {
                        editor.document.insertString(offset, "$prevStartTagName>")
                        editor.caretModel.moveToOffset(offset + prevStartTagName.length + 1)
                    }
                }
            } else if (c == '>') {
                // 当键入一个标签结束符号
                // 如果此时正好构成一个标签开始
                if (inputCharElement.node.elementType === WXMLTypes.START_TAG_END) {
                    inputCharElement.findPrevSibling { it.node.elementType == WXMLTypes.TAG_NAME }?.let {
                        editor.document.insertString(offset, "</${it.text}>")
                    }
                }
            }
        }
        return Result.CONTINUE
    }

    private fun getPrevStartTagName(psiElement: PsiElement): String? {
        return PsiTreeUtil.getParentOfType(psiElement, WXMLElement::class.java)?.tagName
    }

}