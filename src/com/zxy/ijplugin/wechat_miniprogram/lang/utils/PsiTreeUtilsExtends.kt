package com.zxy.ijplugin.wechat_miniprogram.lang.utils

import com.intellij.psi.PsiElement


fun PsiElement.findSiblingBackward(predicate: (psiElement:PsiElement)->Boolean): PsiElement? {
    var prev = this.nextSibling
    while (prev != null) {
        if (predicate(prev)) {
            return prev
        }
        prev = prev.prevSibling
    }
    return null
}