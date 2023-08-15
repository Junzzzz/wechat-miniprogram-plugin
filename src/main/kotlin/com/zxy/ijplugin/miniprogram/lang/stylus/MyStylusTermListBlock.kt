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

package com.zxy.ijplugin.miniprogram.lang.stylus

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder.CssFormatterBlock
import com.zxy.ijplugin.miniprogram.lang.wxss.formatter.WXSSTermListBlock
import org.jetbrains.plugins.stylus.formatter.StylusFormattingUtil
import org.jetbrains.plugins.stylus.psi.StylusTokenTypes

class MyStylusTermListBlock(
    _node: ASTNode?, indent: Indent?, extension: CssFormattingModelBuilder.CssFormattingExtension,
    alignment: Alignment?
) : WXSSTermListBlock(
    _node, indent,
    extension, alignment, false
) {

    override fun getSpacing(formatterBlock: Block?, formatterBlock2: Block): Spacing? {
        return if (formatterBlock is CssFormatterBlock && formatterBlock2 is CssFormatterBlock) {
            val slashResult = StylusFormattingUtil.processSlash(
                formatterBlock.node, formatterBlock2.node
            )
            slashResult
                ?: if (formatterBlock.myType === CssElementTypes.CSS_RPAREN) {
                    Spacing.createSpacing(1, 1, 0, false, 0)
                } else if (formatterBlock.myType !== StylusTokenTypes.IF_KEYWORD && formatterBlock2.myType !== StylusTokenTypes.IF_KEYWORD) {
                    if (formatterBlock.myType !== StylusTokenTypes.SPLAT && formatterBlock2.myType !== StylusTokenTypes.SPLAT) {
                        if (formatterBlock.myType === CssElementTypes.CSS_LBRACKET || formatterBlock.myType === CssElementTypes.CSS_RBRACKET || formatterBlock2.myType === CssElementTypes.CSS_LBRACKET || formatterBlock2.myType === CssElementTypes.CSS_RBRACKET) {
                            Spacing.createSpacing(0, 0, 0, false, 0)
                        } else {
                            super.getSpacing(formatterBlock, formatterBlock2)
                        }
                    } else {
                        Spacing.createSpacing(0, 0, 0, false, 0)
                    }
                } else {
                    Spacing.createSpacing(1, 1, 0, false, 0)
                }
        } else {
            super.getSpacing(formatterBlock, formatterBlock2)
        }
    }

}