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

package com.zxy.ijplugin.miniprogram.utils

import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonBooleanLiteral
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

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

/**
 * 获取一个文件相对于项目根目录的路径
 * 以'/'开头
 */
fun VirtualFile.getPathRelativeToRoot(project: Project): String? {
    val rootPath = ProjectFileIndex.getInstance(
        project
    ).getContentRootForFile(this)?.path ?: return null
    return this.path.removePrefix(rootPath)
}

/**
 * 获取一个文件相对于项目根目录的路径
 * 移除扩展名
 * @see getPathRelativeToRoot
 */
fun VirtualFile.getPathRelativeToRootRemoveExt(project: Project): String? {
    return this.getPathRelativeToRoot(project)?.let {
        if (this.isDirectory) {
            it
        } else {
            it.removeSuffix(".${this.extension}")
        }
    }
}

/**
 * @see PsiTreeUtil.findChildrenOfType
 */
inline fun <reified T : PsiElement> PsiElement.findChildrenOfType(): MutableCollection<T> {
    return PsiTreeUtil.findChildrenOfType(this, T::class.java)
}

/**
 * @see PsiTreeUtil.findChildOfType
 */
inline fun <reified T : PsiElement> PsiElement.findChildOfType(): T? {
    return PsiTreeUtil.findChildOfType(this, T::class.java)
}

/**
 * @see PsiTreeUtil.getParentOfType
 */
inline fun <reified T : PsiElement> PsiElement.getParentOfType(): T? {
    return PsiTreeUtil.getParentOfType(this, T::class.java)
}

fun JsonObject.findStringPropertyValue(key: String): String? {
    return (this.findProperty(key)?.value as? JsonStringLiteral)?.value
}

fun JsonObject.findStringArrayPropertyValue(key: String): Array<String>? {
    return (this.findProperty(key)?.value as? JsonArray)?.valueList?.mapNotNull {
        it as? JsonStringLiteral
    }?.map {
        it.value
    }?.toTypedArray()
}

fun JsonObject.findBooleanPropertyValue(key: String): Boolean? {
    return (this.findProperty(key)?.value as? JsonBooleanLiteral)?.value
}

fun JsonObject.findPropertyValue(key: String): Any? {
    return findStringArrayPropertyValue(key) ?: findStringArrayPropertyValue(key) ?: findBooleanPropertyValue(key)
}