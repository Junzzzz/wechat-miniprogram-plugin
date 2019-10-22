package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi

import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage


class WXSSTokenType(debugName: String) :
        IElementType(debugName, WXSSLanguage.INSTANCE){
    override fun toString(): String {
        return this.javaClass.simpleName + super.toString()
    }
}
