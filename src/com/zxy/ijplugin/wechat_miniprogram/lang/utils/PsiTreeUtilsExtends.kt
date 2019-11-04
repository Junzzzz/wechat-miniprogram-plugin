package com.zxy.ijplugin.wechat_miniprogram.lang.utils

import com.intellij.psi.PsiElement


fun PsiElement.findPrevSibling(predicate: (psiElement: PsiElement) -> Boolean): PsiElement? {
    var prev = this.prevSibling
    while (prev != null) {
        if (predicate(prev)) {
            return prev
        }
        prev = prev.prevSibling
    }
    return null
}

fun PsiElement.findNextSibling(predicate: (psiElement: PsiElement) -> Boolean): PsiElement? {
    var next = this.nextSibling
    while (next != null) {
        if (predicate(next)) {
            return next
        }
        next = next.nextSibling
    }
    return null
}