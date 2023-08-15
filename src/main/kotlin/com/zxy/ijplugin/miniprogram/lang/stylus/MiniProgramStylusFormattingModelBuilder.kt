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
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.miniprogram.lang.utils.StyleLanguageUtils
import org.jetbrains.plugins.stylus.StylusLanguage
import org.jetbrains.plugins.stylus.formatter.StylusFormattingModelBuilder
import org.jetbrains.plugins.stylus.settings.StylusCodeStyleSettings

class MiniProgramStylusFormattingModelBuilder : StylusFormattingModelBuilder() {

    override fun createExtension(settings: CodeStyleSettings): StylusFormattingExtension {
        return object : StylusFormattingModelBuilder.StylusFormattingExtension(
            settings.getCommonSettings(StylusLanguage.INSTANCE),
            settings.getCustomSettings(StylusCodeStyleSettings::class.java)
        ) {

            override fun createTermListBlock(
                _node: ASTNode?, indent: Indent?, alignment: Alignment?, shouldIndentContent: Boolean
            ): CssTermListBlock {
                return if (StyleLanguageUtils.isMiniProgramContext(_node)) {
                    MyStylusTermListBlock(_node, indent, this, alignment)
                } else {
                    super.createTermListBlock(_node, indent, alignment, shouldIndentContent)
                }
            }
        }
    }

}