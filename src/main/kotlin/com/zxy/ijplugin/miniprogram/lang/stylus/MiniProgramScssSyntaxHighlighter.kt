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

import com.intellij.lexer.Lexer
import com.intellij.psi.css.impl.util.scheme.CssElementDescriptorFactory2
import org.jetbrains.plugins.stylus.StylusSyntaxHighlighter
import org.jetbrains.plugins.stylus.highlighting.StylusHighlighterLexer

class MiniProgramStylusSyntaxHighlighter : StylusSyntaxHighlighter() {

    override fun getHighlightingLexer(): Lexer {
        return StylusHighlighterLexer(CssElementDescriptorFactory2.getInstance().valueIdentifiers.apply {
            this.add("rpx")
        })
    }

}