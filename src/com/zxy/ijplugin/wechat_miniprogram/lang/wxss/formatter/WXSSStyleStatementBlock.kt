package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSStyleStatementBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(node, codeStyleSettings = codeStyleSettings) {

    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .before(WXSSTypes.ATTRIBUTE_NAME)
                .spaces(0)
                .after(WXSSTypes.COLON)
                .spaces(1)
                .before(WXSSTypes.SEMICOLON)
                .spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun getIndent(): Indent? {
        return Indent.getSpaceIndent(0)
    }

}

