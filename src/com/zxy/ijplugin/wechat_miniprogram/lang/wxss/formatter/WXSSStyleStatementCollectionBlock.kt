package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSStyleStatementCollectionBlock(node: ASTNode, codeStyleSettings: CodeStyleSettings) :
        AbstractWXSSBlock(
                node,
                Wrap.createWrap(WrapType.ALWAYS, true),
                Alignment.createAlignment(true),
                codeStyleSettings = codeStyleSettings
        ) {
    override fun isLeaf(): Boolean {
        return false
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? {
        return SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                .after(WXSSTypes.SEMICOLON).blankLines(0)
                .before(WXSSTypes.SEMICOLON).spaces(0)
                .getSpacing(this, p0, p1)
    }

    override fun getIndent(): Indent? {
        return Indent.getNormalIndent()
    }
}