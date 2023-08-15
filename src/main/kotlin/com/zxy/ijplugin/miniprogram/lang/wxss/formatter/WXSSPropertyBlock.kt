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
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.css.CssTermType
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.util.completion.TimeUserLookup
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder
import com.intellij.psi.xml.XmlTokenType
import com.zxy.ijplugin.miniprogram.lang.wxss.psi.impl.WXSSTermImpl

open class WXSSPropertyBlock(
    _node: ASTNode?, indent: Indent?, extension: CssFormattingModelBuilder.CssFormattingExtension?,
    alignment: Alignment?, childAlignment: Alignment?
) : CssFormattingModelBuilder.CssPropertyBlock(
    _node, indent,
    extension, alignment, childAlignment
) {
    override fun getSpacing(formatterBlock: Block?, formatterBlock2: Block): Spacing? {
        return if (formatterBlock is CssFormattingModelBuilder.CssFormatterBlock && formatterBlock2 is CssFormattingModelBuilder.CssFormatterBlock) {
            val codeStyleSettings = this.customSettings
            if (codeStyleSettings.VALUE_ALIGNMENT == 1 && formatterBlock2.myType === CssElementTypes.CSS_COLON) {
                Spacing.createSpacing(1, 1, 0, false, 0)
            } else if (myExtension.isLineComment(formatterBlock.myType)) {
                Spacing.createSpacing(0, 0, 1, true, this.keepBlankLines)
            } else if (formatterBlock.myType !== CssElementTypes.CSS_LPAREN && formatterBlock2.myType !== CssElementTypes.CSS_RPAREN) {
                if (formatterBlock2.myType === CssElementTypes.CSS_COMMA) {
                    Spacing.createSpacing(0, 0, 0, false, 0)
                } else if (formatterBlock.myType === CssElementTypes.CSS_COMMA) {
                    Spacing.createSpacing(1, 1, 0, true, 0)
                } else if (formatterBlock2.myType === CssElementTypes.CSS_LPAREN && this.node
                        .elementType === CssElementTypes.CSS_IMPORT
                ) {
                    Spacing.createSpacing(0, 1, 0, false, 0)
                } else {
                    val textOfFormatterBlock2 = StringUtil.toLowerCase(
                        formatterBlock2.node.text
                    )
                    if (formatterBlock.myType !== CssElementTypes.CSS_MINUS && formatterBlock.myType !== CssElementTypes.CSS_ASTERISK && (formatterBlock.myType !== CssElementTypes.CSS_PLUS || formatterBlock2.myType !== CssElementTypes.CSS_NUMBER) && (formatterBlock.myType !== CssElementTypes.CSS_IDENT || formatterBlock2.myType !== CssElementTypes.CSS_PLUS) && (formatterBlock.myType !== CssElementTypes.CSS_PLUS || formatterBlock2.myType !== CssElementTypes.CSS_IDENT) && formatterBlock2.myType !== CssElementTypes.CSS_COLON && formatterBlock2.myType !== CssElementTypes.CSS_SEMICOLON && formatterBlock2.myType !== CssElementTypes.CSS_PERCENT && (formatterBlock.myType !== CssElementTypes.CSS_NUMBER && formatterBlock.myType !== XmlTokenType.XML_COMMENT_START || formatterBlock2.myType !== CssElementTypes.CSS_IDENT || WXSSTermImpl.getTypeBySuffix(
                            textOfFormatterBlock2
                        ) === CssTermType.UNKNOWN && "n" != textOfFormatterBlock2 && "x" != textOfFormatterBlock2 && !TimeUserLookup.isTimeSuffix(
                            textOfFormatterBlock2
                        ))
                    ) {
                        if (formatterBlock2.myType === CssElementTypes.CSS_LPAREN) {
                            Spacing.createSpacing(0, 1, 0, false, 0)
                        } else if (formatterBlock.myType === CssElementTypes.CSS_COLON) {
                            if (codeStyleSettings.SPACE_AFTER_COLON) Spacing.createSpacing(
                                1, 1, 0, false, 0
                            ) else Spacing.createSpacing(0, 0, 0, false, 0)
                        } else {
                            Spacing.createSpacing(1, 1, 0, false, 0)
                        }
                    } else {
                        Spacing.createSpacing(0, 0, 0, false, 0)
                    }
                }
            } else {
                Spacing.createSpacing(0, 0, 0, true, 0)
            }
        } else {
            null
        }
    }
}