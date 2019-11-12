package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.openapi.util.TextRange

fun String.substring(textRange: TextRange):String{
    return this.substring(textRange.startOffset,textRange.endOffset)
}

fun String.replace(textRange: TextRange,replaceString:CharSequence):String{
    return this.replaceRange(textRange.startOffset,textRange.endOffset,replaceString)
}