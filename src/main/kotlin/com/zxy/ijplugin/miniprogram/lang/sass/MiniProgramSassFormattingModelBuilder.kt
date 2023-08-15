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

package com.zxy.ijplugin.miniprogram.lang.sass

import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.css.CssBlock
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.miniprogram.lang.utils.StyleLanguageUtils
import com.zxy.ijplugin.miniprogram.lang.wxss.formatter.WXSSPropertyBlock
import com.zxy.ijplugin.miniprogram.lang.wxss.formatter.WXSSTermListBlock
import org.jetbrains.plugins.sass.formatter.SassFormattingModelBuilder
import org.jetbrains.plugins.sass.psi.SASSElementTypes
import org.jetbrains.plugins.sass.settings.SassCodeStyleSettings
import org.jetbrains.plugins.scss.SCSSLanguage
import org.jetbrains.plugins.scss.psi.SassScssFunctionBodyImpl

class MiniProgramSassFormattingModelBuilder : SassFormattingModelBuilder() {

    override fun createExtension(settings: CodeStyleSettings): SassFormattingExtension {
        return object : SassFormattingModelBuilder.SassFormattingExtension(
            settings.getCommonSettings(SCSSLanguage.INSTANCE),
            settings.getCustomSettings(SassCodeStyleSettings::class.java)
        ) {
            override fun createPropertyBlock(
                _node: ASTNode, extension: CssFormattingExtension?, alignment: Alignment?,
                childAlignment: Alignment?
            ): CssPropertyBlock {
                return if (StyleLanguageUtils.isMiniProgramContext(_node)) {
                    val indent = if (_node.treeParent
                            .elementType !== SASSElementTypes.PROPERTY_RULESET && (PsiTreeUtil.getParentOfType(
                            _node.psi,
                            CssBlock::class.java
                        ) != null || PsiTreeUtil.getParentOfType(
                            _node.psi,
                            SassScssFunctionBodyImpl::class.java
                        ) != null)
                    ) Indent.getNormalIndent(
                        true
                    ) else Indent.getNoneIndent()
                    WXSSPropertyBlock(_node, indent, extension, alignment, childAlignment)
                } else {
                    super.createPropertyBlock(_node, extension, alignment, childAlignment)
                }
            }

            override fun createPropertyBlock(
                _node: ASTNode?, indent: Indent?, extension: CssFormattingExtension?, alignment: Alignment?,
                childAlignment: Alignment?
            ): CssPropertyBlock {
                return if (StyleLanguageUtils.isMiniProgramContext(_node)) {
                    WXSSPropertyBlock(_node, indent, extension, alignment, childAlignment)
                } else {
                    super.createPropertyBlock(_node, indent, extension, alignment, childAlignment)
                }
            }

            override fun createTermListBlock(
                _node: ASTNode?, indent: Indent?, alignment: Alignment?, shouldIndentContent: Boolean
            ): CssTermListBlock {
                return if (StyleLanguageUtils.isMiniProgramContext(_node)) {
                    WXSSTermListBlock(_node, indent, this, alignment, shouldIndentContent)
                } else {
                    super.createTermListBlock(_node, indent, alignment, shouldIndentContent)
                }
            }
        }
    }

}