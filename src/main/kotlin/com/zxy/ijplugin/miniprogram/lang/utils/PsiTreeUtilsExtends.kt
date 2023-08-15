/*
 *     Copyright (c) [2019] [zxy]
 *     [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *     You can use this software according to the terms and conditions of the Mulan PSL v1.
 *     You may obtain a copy of Mulan PSL v1 at:
 *         http://license.coscl.org.cn/MulanPSL
 *     THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *     PURPOSE.
 *     See the Mulan PSL v1 for more details.
 */

@file:Suppress("unused")

package com.zxy.ijplugin.miniprogram.lang.utils

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