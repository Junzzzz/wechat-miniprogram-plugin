package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.psi.tree.IElementType


class WXSSElementType(debugName: String) :
        IElementType(debugName, WXSSLanguage.INSTANCE)


object WXSSElementTypes {

    @JvmField
    val WXSS_IMPORT = WXSSElementType("WXSS_IMPORT")

    @JvmField
    val WXSS_STRING_START = WXSSElementType("WXSS_STRING")

    @JvmField
    val WXSS_STRING_END = WXSSElementType("WXSS_STRING_END")

}