package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi

import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage


class WXSSTokenType(debugName: String) :
        IElementType(debugName, WXSSLanguage.INSTANCE){
    override fun toString(): String {
        return this.javaClass.simpleName + "." + super.toString()
    }
}


object WXSSTokenTypes {

    @JvmField
    val WXSS_IMPORT = WXSSTokenType("WXSS_IMPORT")

    @JvmField
    val WXSS_STRING_START = WXSSTokenType("WXSS_STRING_START")

    @JvmField
    val WXSS_STRING_END = WXSSTokenType("WXSS_STRING_END")

    @JvmField
    val WXSS_STRING_CONTENT = WXSSTokenType("WXSS_STRING_CONTENT")

}