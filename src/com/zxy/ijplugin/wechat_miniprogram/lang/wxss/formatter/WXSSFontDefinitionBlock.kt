package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSFontDefinitionBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        WXSSStyleDefinitionBlock(node,codeStyleSettings = codeStyleSettings) {

    override fun isLeaf(): Boolean = false

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .after(WXSSTypes.FONT_FACE_KEYWORD)
                .spaces(1).getSpacing(this, p0, p1)
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

}