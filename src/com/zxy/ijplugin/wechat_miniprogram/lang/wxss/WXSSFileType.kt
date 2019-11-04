package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WXSSFileType :LanguageFileType(WXSSLanguage.INSTANCE){

    companion object{
        @JvmField
        val INSTANCE = WXSSFileType()
    }

    override fun getIcon(): Icon? {
        return WXSSIcons.FILE
    }

    override fun getName(): String {
        return "WXSS"
    }

    override fun getDefaultExtension(): String {
        return "wxss"
    }

    override fun getDescription(): String {
        return "WXSS file"
    }

}