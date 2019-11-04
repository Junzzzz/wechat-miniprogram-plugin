package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WXMLFileType : LanguageFileType(WXMLLanguage.INSTANCE) {

    companion object {
        @JvmField
        val INSTANCE = WXMLFileType()
    }

    override fun getIcon(): Icon? {
        return WXMLIcons.FILE
    }

    override fun getName(): String {
        return "WXML"
    }

    override fun getDefaultExtension(): String {
        return "wxml"
    }

    override fun getDescription(): String {
        return "Wechat xml template"
    }

}