package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils

object WXMLUtils {
    fun isValidTagName(charSequence: CharSequence): Boolean {
        return charSequence.all { it.isDigit() || (it.isLetter() && it.isLowerCase()) || it == '-' || it == '_' }
    }
}