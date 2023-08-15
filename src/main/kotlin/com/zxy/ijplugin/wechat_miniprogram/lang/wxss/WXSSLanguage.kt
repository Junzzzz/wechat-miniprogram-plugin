package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType

class WXSSLanguage private constructor() : Language("WXSS") {
    companion object {
        @JvmField
        var INSTANCE = WXSSLanguage()
    }

    override fun getAssociatedFileType(): LanguageFileType? {
        return WXSSFileType.INSTANCE
    }
}
