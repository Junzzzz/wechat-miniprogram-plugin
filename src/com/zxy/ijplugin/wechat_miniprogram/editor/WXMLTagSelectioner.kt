package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.editorActions.wordSelection.AbstractWordSelectioner
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLClosedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

/**
 * 处理双击选择WXML元素开始标签的开始位置
 * @see WXMLTypes.START_TAG_START
 * @see com.intellij.codeInsight.editorActions.XmlTagSelectioner
 * @see com.intellij.codeInsight.editorActions.HtmlSelectioner
 */
class WXMLStartTagSelectioner : AbstractWordSelectioner() {
    override fun canSelect(psiElement: PsiElement): Boolean {
        return psiElement.node.elementType == WXMLTypes.START_TAG_START
    }

    override fun select(
            psiElement: PsiElement, charSequence: CharSequence, p2: Int, p3: Editor
    ): MutableList<TextRange> {
        val parent = psiElement.parent
        val result = ArrayList<TextRange>(1)
        if (parent is WXMLStartTag) {
            result.add(parent.textRange)
        } else if (parent is WXMLClosedElement) {
            result.add(parent.textRange)
        }
        return result
    }

    override fun getMinimalTextRangeLength(element: PsiElement, text: CharSequence, cursorOffset: Int): Int {
        return 2
    }
}

/**
 * 处理双击选择WXML元素结束标签的开始位置
 * @see WXMLTypes.END_TAG_START
 */
class WXMLEndTagSelectioner : AbstractWordSelectioner() {
    override fun canSelect(psiElement: PsiElement): Boolean {
        return psiElement.node.elementType == WXMLTypes.END_TAG_START
    }

    override fun select(
            psiElement: PsiElement, editorText: CharSequence, cursorOffset: Int, editor: Editor
    ): MutableList<TextRange> {
        return mutableListOf(PsiTreeUtil.getParentOfType(psiElement, WXMLEndTag::class.java)!!.textRange)
    }

    override fun getMinimalTextRangeLength(element: PsiElement, text: CharSequence, cursorOffset: Int): Int {
        return 3
    }
}