package com.zxy.ijplugin.wechat_miniprogram.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLModuleUtils

class WXMLIncludeTagFolding : FoldingBuilderEx() {
    override fun getPlaceholderText(astNode: ASTNode): String? {
        // TODO 使用引用文件的路径作为占位文字
        return "..."
    }

    override fun buildFoldRegions(
            rootElement: PsiElement, document: Document, quick: Boolean
    ): Array<FoldingDescriptor> {
        val elements = PsiTreeUtil.findChildrenOfType(rootElement, WXMLElement::class.java)
        return WXMLModuleUtils.findValidIncludeTags(elements).map {
            FoldingDescriptor(
                    it.node,
                    TextRange(
                            it.node.findChildByType(WXMLTypes.TAG_NAME)!!.textRange.endOffset + 1,
                            it.textRange.endOffset
                    )
            )
        }.toTypedArray()
    }

    override fun isCollapsedByDefault(p0: ASTNode): Boolean {
        return true
    }

}