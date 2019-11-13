package com.zxy.ijplugin.wechat_miniprogram.context

import com.intellij.json.JsonFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType

fun isWechatMiniProgramContext(psiElement: PsiElement): Boolean {
    return isWechatMiniProgramContext(psiElement.project)
}

fun isWechatMiniProgramContext(project: Project): Boolean {
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            val projectConfigJsonFile = baseDir.children.find { it.name == "project.config.json" }
            return projectConfigJsonFile != null
        }
    }
    return false
}

enum class RelateFileType(val fileType: LanguageFileType) {
    WXML(WXMLFileType.INSTANCE),
    JSON(JsonFileType.INSTANCE),
    JS(JavaScriptFileType.INSTANCE),
    WXSS(WXSSFileType.INSTANCE)
}

/**
 * 找到指定的文件所对应的指定扩展的文件
 * 例如：
 * 已知一个wxss
 * 找到其对应的wxml文件
 */
fun findRelateFile(originFile: VirtualFile, relateFileType: RelateFileType): VirtualFile? {
    return originFile.parent.children.find { it.nameWithoutExtension == originFile.nameWithoutExtension && it.extension == relateFileType.fileType.defaultExtension }
}

fun findAppFile(project: Project, relateFileType: RelateFileType): VirtualFile? {
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            return baseDir.findChild("app." + relateFileType.fileType.defaultExtension)
        }
    }
    return null
}