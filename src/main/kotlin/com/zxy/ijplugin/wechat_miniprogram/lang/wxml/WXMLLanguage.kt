package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.xml.XMLLanguage

object WXMLLanguage : XMLLanguage(INSTANCE, "WXML") {
    @JvmStatic
    var EVENT_ATTRIBUTE_PREFIX_ARRAY = arrayOf("bind", "catch", "bind:", "catch:", "mut-bind", "mut-bind:")

    @JvmStatic
    var INSTANCE = WXMLLanguage
}
