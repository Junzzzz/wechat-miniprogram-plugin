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

package com.zxy.ijplugin.miniprogram.lang.wxss.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder.CssFormatterBlock
import com.intellij.psi.templateLanguages.OuterLanguageElement

open class WXSSTermListBlock(
    _node: ASTNode?, indent: Indent?, extension: CssFormattingModelBuilder.CssFormattingExtension?,
    alignment: Alignment?, private val shouldIndentContent: Boolean
) : CssFormattingModelBuilder.CssTermListBlock(_node, indent, extension, alignment, shouldIndentContent) {

    override fun getSpacing(formatterBlock: Block?, formatterBlock2: Block): Spacing? {
        return if (formatterBlock is CssFormatterBlock && formatterBlock2 is CssFormatterBlock) {
            if (formatterBlock.myType !== CssElementTypes.CSS_COLON && formatterBlock2.myType !== CssElementTypes.CSS_COLON && formatterBlock.myType !== CssElementTypes.CSS_EQ && formatterBlock2.myType !== CssElementTypes.CSS_EQ && formatterBlock.myType !== CssElementTypes.CSS_PERIOD && formatterBlock2.myType !== CssElementTypes.CSS_PERIOD) {
                if (formatterBlock.myType !== CssElementTypes.CSS_SLASH && formatterBlock2.myType !== CssElementTypes.CSS_SLASH && formatterBlock.node !is OuterLanguageElement && formatterBlock2.node !is OuterLanguageElement) {
                    WXSSPropertyBlock(this.node, this.indent, this.myExtension, this.alignment, null).getSpacing(
                        formatterBlock, formatterBlock2
                    )
                } else Spacing.getReadOnlySpacing()
            } else {
                Spacing.createSpacing(0, 0, 0, false, 0)
            }
        } else {
            null
        }
    }

    override fun shouldIndentContent(): Boolean {
        return shouldIndentContent
    }


}