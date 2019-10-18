package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.xml.XMLLanguage

class WxmlLanguage : XMLLanguage(WxmlLanguage.INSTANCE, "Wxml") {

    companion object {
        val INSTANCE = WxmlLanguage()
    }
}