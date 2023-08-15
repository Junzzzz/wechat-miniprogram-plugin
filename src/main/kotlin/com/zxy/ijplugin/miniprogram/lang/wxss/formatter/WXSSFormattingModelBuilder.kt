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
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.lang.css.CSSLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.css.codeStyle.CssCodeStyleSettings
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder

class WXSSFormattingModelBuilder : CssFormattingModelBuilder() {

    override fun createExtension(settings: CodeStyleSettings): CssFormattingExtension {
        return object : CssFormattingExtension(
            settings.getCommonSettings(CSSLanguage.INSTANCE),
            settings.getCustomSettings(
                CssCodeStyleSettings::class.java
            )
        ) {

            override fun createTermListBlock(
                _node: ASTNode?, indent: Indent?, alignment: Alignment?, shouldIndentContent: Boolean
            ): CssTermListBlock {
                return WXSSTermListBlock(_node, indent, this, alignment, shouldIndentContent)
            }

            override fun createPropertyBlock(
                _node: ASTNode?, indent: Indent?, extension: CssFormattingExtension?, alignment: Alignment?,
                childAlignment: Alignment?
            ): CssPropertyBlock {
                return WXSSPropertyBlock(_node, indent, extension, alignment, childAlignment)
            }
        }
    }
}