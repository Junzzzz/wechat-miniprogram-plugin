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

package com.zxy.ijplugin.miniprogram.context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.zxy.ijplugin.miniprogram.settings.EnableSupportType
import com.zxy.ijplugin.miniprogram.settings.MiniProgramType
import com.zxy.ijplugin.miniprogram.settings.MyProjectSettings

fun isWechatMiniProgramContext(psiElement: PsiElement): Boolean {
    return isWechatMiniProgramContext(psiElement.project)
}

/**
 * @param strict 如果为false则找到project.config.json即可
 * 如果为true 还需判断里面的compileType = miniprogram
 */
fun isWechatMiniProgramContext(project: Project, strict: Boolean = true): Boolean {
    if (MyProjectSettings.getState(project).enableSupport == EnableSupportType.ENABLE) {
        return true
    }
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            val projectConfigJsonFile = baseDir.children.find { it.name == "project.config.json" } ?: return false
            return !strict || runReadAction {
                // 读取文件内容
                val gson = Gson()
                val configs = gson.fromJson<Map<String, Any>>(
                    VfsUtil.loadText(projectConfigJsonFile),
                    object : TypeToken<Map<String, Any>>() {}.type
                )
                configs["compileType"] == "miniprogram"
            }
        }
    }
    return findProjectConfigJsonFile(project)?.let { jsonFile ->
        ((jsonFile.children.getOrNull(0) as? JsonObject)?.propertyList?.find {
            it.name == "compileType"
        }?.value as? JsonStringLiteral)?.value == "miniprogram"
    } == true
}

fun findProjectConfigJsonVirtualFile(project: Project): VirtualFile? {
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            return baseDir.children.find { it.name == "project.config.json" }
        }
    }
    return null
}

fun findProjectConfigJsonFile(project: Project): JsonFile? {
    return findProjectConfigJsonVirtualFile(project)?.let {
        runReadAction {
            // 读取文件内容创建文件
            PsiManager.getInstance(project).findFile(it) as? JsonFile
        }
    }
}

fun Project.isQQContext(): Boolean {
    return MyProjectSettings.getState(
        this
    ).miniprogramType === MiniProgramType.QQ
}

fun findMiniProgramRootDir(project: Project): PsiDirectory? {
    val jsonFile = findProjectConfigJsonFile(project)
    return jsonFile?.let {
        ((jsonFile.children.getOrNull(0) as? JsonObject)?.propertyList?.find {
            it.name == "miniprogramRoot"
        }?.value as? JsonStringLiteral)
    }?.let { jsonStringLiteral ->
        FileReferenceSet(jsonStringLiteral).allReferences.last {
            it.text.isNotBlank()
        }?.resolve()
    } as? PsiDirectory ?: findProjectRootDir(project)
}

fun findProjectRootDir(project: Project): PsiDirectory? {
    return project.basePath?.let {
        LocalFileSystem.getInstance().findFileByPath(it)
    }?.let {
        runReadAction {
            // 读取文件内容创建文件
            PsiManager.getInstance(project).findDirectory(it)
        }
    }
}

/**
 * Find "miniprogram_npm" Directory
 */
fun findMiniProgramNpmRootDir(project: Project): PsiDirectory? {
    return findProjectRootDir(project)?.let {
        if (it.findFile("package.json") != null && it.findSubdirectory("node_modules") != null) {
            // package.json is existing
            // node_modules is existing
            it.findSubdirectory("miniprogram_npm")
        } else {
            null
        }
    }
}