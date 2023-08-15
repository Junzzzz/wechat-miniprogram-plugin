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

package com.zxy.ijplugin.miniprogram.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLModuleUtils

class WXMLIncludeTagFolding : FoldingBuilderEx() {
    override fun getPlaceholderText(astNode: ASTNode): String? {
        val psiElement = astNode.psi
        if (psiElement is XmlTag) {
            val resolveResult =
                psiElement.attributes.find { it.name == "src" }?.valueElement?.references?.lastOrNull()?.resolve()
                    ?: return null
            if (resolveResult is WXMLPsiFile) {
                return resolveResult.children.asSequence().filterIsInstance<XmlDocument>().flatMap {
                    it.children.asSequence()
                }.filter {
                    it is XmlTag && it.name != "template" && it.name != "wxs"
                }.joinToString("") {
                    it.text
                }
            }
        }
        return "..."
    }

    override fun buildFoldRegions(
        rootElement: PsiElement, document: Document, quick: Boolean
    ): Array<FoldingDescriptor> {
        val elements = PsiTreeUtil.findChildrenOfType(rootElement, XmlTag::class.java)
        return WXMLModuleUtils.findValidIncludeTags(elements).map {
            FoldingDescriptor(
                it,
                TextRange(
                    it.textRange.startOffset + it.name.length + 1,
                    it.textRange.endOffset
                )
            )
        }.toTypedArray()
    }

    override fun isCollapsedByDefault(p0: ASTNode): Boolean {
        return true
    }

}