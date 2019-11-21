package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

fun String.substring(textRange: TextRange): String {
    return this.substring(textRange.startOffset, textRange.endOffset)
}

fun String.replace(textRange: TextRange, replaceString: CharSequence): String {
    return this.replaceRange(textRange.startOffset, textRange.endOffset, replaceString)
}

fun IntRange.toTextRange(): TextRange {
    return TextRange(this.first, this.last + 1)
}

fun PsiElement.contentRange(): TextRange {
    return TextRange(0, this.textLength)
}