package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WxmlFileType : LanguageFileType(WxmlLanguage.INSTANCE) {

    companion object {
        @JvmField
        val INSTANCE = WxmlFileType()
    }

    override fun getIcon(): Icon? {
        return WxmlIcons.FILE
    }

    override fun getName(): String {
        return "Wxml"
    }

    override fun getDefaultExtension(): String {
        return "wxml"
    }

    override fun getDescription(): String {
        return "Wechat xml template"
    }

}