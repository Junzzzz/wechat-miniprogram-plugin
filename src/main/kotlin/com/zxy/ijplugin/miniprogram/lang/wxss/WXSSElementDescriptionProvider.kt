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

package com.zxy.ijplugin.miniprogram.lang.wxss

import com.intellij.psi.PsiElement
import com.intellij.psi.css.CssElementDescriptorProvider
import com.intellij.psi.css.CssSimpleSelector
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.attributes.WXMLAttributeNameCompletionProvider.Companion.IGNORE_COMMON_ATTRIBUTE_TAG_NAMES
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType

class WXSSElementDescriptionProvider : CssElementDescriptorProvider() {
    override fun getDeclarationsForSimpleSelector(p0: CssSimpleSelector): Array<PsiElement> {
        return PsiElement.EMPTY_ARRAY
    }

    override fun isMyContext(element: PsiElement?): Boolean {
        return element != null && isWechatMiniProgramContext(element)
    }

    override fun isPossibleSelector(selector: String, context: PsiElement): Boolean {
        return selector == "page" || WXMLMetadata.getElementDescriptions(context.project).any { it.name == selector }
    }

    override fun getSimpleSelectors(context: PsiElement): Array<String> {
        val result = mutableListOf("page")
        val wxssPsiFile = (context.containingFile as? WXSSPsiFile)
        if (wxssPsiFile != null) {
            val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(wxssPsiFile)
            if (wxmlPsiFile != null) {
                result.addAll(wxmlPsiFile.findChildrenOfType<XmlTag>().distinctBy { it.name }.map {
                    it.name
                }.filter {
                    // 忽略部分标签
                    !IGNORE_COMMON_ATTRIBUTE_TAG_NAMES.contains(it)
                })
            }
        }
        return result.toTypedArray()
    }
}