package com.zxy.ijplugin.wechat_miniprogram.context

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement

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

enum class RelateFileType{
    WXML,
    JSON,
    JS,
    WXSS
}

fun findRelateFile(relateFileType: RelateFileType){

}