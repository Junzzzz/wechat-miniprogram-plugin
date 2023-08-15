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

package com.zxy.ijplugin.miniprogram.lang.less

import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.miniprogram.lang.utils.StyleLanguageUtils
import com.zxy.ijplugin.miniprogram.lang.wxss.formatter.WXSSPropertyBlock
import com.zxy.ijplugin.miniprogram.lang.wxss.formatter.WXSSTermListBlock
import org.jetbrains.plugins.less.LESSLanguage
import org.jetbrains.plugins.less.formatter.LessFormattingModelBuilder
import org.jetbrains.plugins.less.settings.LessCodeStyleSettings

class MiniProgramLessFormattingModelBuilder : LessFormattingModelBuilder() {

    override fun createExtension(settings: CodeStyleSettings): LessFormattingExtension {
        return object : LessFormattingExtension(
            settings.getCommonSettings(LESSLanguage.INSTANCE),
            settings.getCustomSettings(LessCodeStyleSettings::class.java)
        ) {
            override fun createTermListBlock(
                _node: ASTNode?, indent: Indent?, alignment: Alignment?, shouldIndentContent: Boolean
            ): CssTermListBlock {
                return if (StyleLanguageUtils.isMiniProgramContext(_node)) {
                    WXSSTermListBlock(_node, indent, this, alignment, shouldIndentContent)
                } else {
                    super.createTermListBlock(_node, indent, alignment, shouldIndentContent)
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

        }
    }
}