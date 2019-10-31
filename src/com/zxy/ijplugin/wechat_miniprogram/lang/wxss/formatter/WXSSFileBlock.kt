package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes

class WXSSFileBlock(node:ASTNode,codeStyleSettings: CodeStyleSettings) : AbstractWXSSBlock(
        node, Wrap.createWrap(WrapType.NONE, false),
        Alignment.createAlignment(),
        codeStyleSettings
) {

    override fun isLeaf(): Boolean = false

    override fun getSpacing(p0: Block?, p1: Block): Spacing? =
            SpacingBuilder(codeStyleSettings, WXSSLanguage.INSTANCE)
                    .around(
                            TokenSet.create(
                                    WXSSTypes.FONT_DEFINITION, WXSSTypes.IMPORT, WXSSTypes.STYLE_DEFINITION
                            )
                    )
                    .blankLines(1)
                    .getSpacing(this, p0, p1)

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

}