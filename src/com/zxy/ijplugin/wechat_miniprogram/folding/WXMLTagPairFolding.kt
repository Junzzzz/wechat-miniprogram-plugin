package com.zxy.ijplugin.wechat_miniprogram.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLOpenedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

/**
 * 可折叠wxml的成对标签
 * 只要开始标签和结束标签不在同一行上
 */
class WXMLTagPairFoldingBuilder : FoldingBuilderEx() {
    override fun getPlaceholderText(p0: ASTNode): String? {
        return "..."
    }

    override fun buildFoldRegions(
            rootElement: PsiElement, document: Document, quick: Boolean
    ): Array<FoldingDescriptor> {
        val openedElements = PsiTreeUtil.findChildrenOfType(rootElement, WXMLOpenedElement::class.java)
        return openedElements.map { openedElement ->
            val startTag = openedElement.startTag
            val endTag = openedElement.endTag ?: return@map null
            val startTagOffset = startTag.textOffset
            val endTagOffset = endTag.textOffset
            val startLineNumber = document.getLineNumber(startTagOffset)
            val endLineNumber = document.getLineNumber(endTagOffset)
            if (startLineNumber == endLineNumber) {
                null
            } else {
                val startFoldOffset = startTag.node.findChildByType(WXMLTypes.TAG_NAME)!!.textRange.endOffset
                val endFoldOffset = endTag.textRange.endOffset
                FoldingDescriptor(openedElement.node, TextRange(startFoldOffset, endFoldOffset))
            }
        }.filterNotNull().toTypedArray()
    }

    override fun isCollapsedByDefault(p0: ASTNode): Boolean {
        return false
    }
}