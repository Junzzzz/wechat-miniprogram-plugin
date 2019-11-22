package com.zxy.ijplugin.wechat_miniprogram.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.findAttribute

class WXMLIncludeTagFolding : FoldingBuilderEx() {
    override fun getPlaceholderText(astNode: ASTNode): String? {
        // TODO 使用引用文件的内容作为占位文字
        val psiElement = astNode.psi
        if (psiElement is WXMLElement) {
            val resolveResult = psiElement.findAttribute("src")?.string?.stringText?.references?.lastOrNull()?.resolve()
            if (resolveResult is WXMLPsiFile) {
                return resolveResult.text
            }
        }
        return "..."
    }

    override fun buildFoldRegions(
            rootElement: PsiElement, document: Document, quick: Boolean
    ): Array<FoldingDescriptor> {
        val elements = PsiTreeUtil.findChildrenOfType(rootElement, WXMLElement::class.java)
        return WXMLModuleUtils.findValidIncludeTags(elements).mapNotNull {
            val tagNameNode = PsiTreeUtil.findChildOfType(it, WXMLStartTag::class.java)?.node?.findChildByType(
                    WXMLTypes.TAG_NAME
            ) ?: return@mapNotNull null
            FoldingDescriptor(
                    it.node,
                    TextRange(
                            tagNameNode.textRange.endOffset + 1,
                            it.textRange.endOffset
                    )
            )
        }.toTypedArray()
    }

    override fun isCollapsedByDefault(p0: ASTNode): Boolean {
        return true
    }

}