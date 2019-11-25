package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
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

/**
 * 获取一个文件相对于项目根目录的路径
 * 以'/'开头
 */
fun VirtualFile.getPathRelativeToRoot(project: Project): String? {
    val rootPath = ProjectFileIndex.SERVICE.getInstance(
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
    return this.getPathRelativeToRoot(project)?.removeSuffix(".${this.extension}")
}