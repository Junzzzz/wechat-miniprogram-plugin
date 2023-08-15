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

package com.zxy.ijplugin.miniprogram.completion

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext

class WechatMiniProgramJsonSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): MutableList<JsonSchemaFileProvider> {
        return mutableListOf(AppJsonSchemaFileProvider(project), PageJsonSchemaFileProvider(project))
    }
}

fun isAppJsonFile(project: Project, virtualFile: VirtualFile): Boolean {
    return isRootFile(project, virtualFile) && virtualFile.name == "app.json"
}

fun isPageJsonFile(project: Project, virtualFile: VirtualFile): Boolean {
    return runReadAction {
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
        virtualFile.extension == "json" && psiFile?.let {
            RelateFileHolder.MARKUP.findFile(it)
        } != null
    }
}

fun isRootFile(project: Project, virtualFile: VirtualFile): Boolean {
    val contentRoot = ProjectFileIndex.getInstance(
        project
    ).getContentRootForFile(virtualFile)
    // 在根目录下且名称为 app.json
    return virtualFile.parent == contentRoot
}

fun isAppWxssFile(project: Project, virtualFile: VirtualFile): Boolean {
    val contentRoot = ProjectFileIndex.getInstance(
        project
    ).getContentRootForFile(virtualFile)
    // 在根目录下且名称为 app.wxss
    return virtualFile.parent == contentRoot && virtualFile.name == "app.wxss"
}

private class AppJsonSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {

    override fun getName(): String {
        return "app.json"
    }

    override fun isAvailable(virtualFile: VirtualFile): Boolean {
        return isWechatMiniProgramContext(project) && isAppJsonFile(project, virtualFile)
    }

    override fun getSchemaFile(): VirtualFile? {
        return JsonSchemaProviderFactory.getResourceFile(
            this.javaClass,
            "/jsonSchemas/app.json"
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.schema
    }

}

private class PageJsonSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {
    override fun getName(): String {
        return "page.js"
    }

    override fun isAvailable(virtualFile: VirtualFile): Boolean {
        return isWechatMiniProgramContext(this.project) && isPageJsonFile(project, virtualFile)
    }

    override fun getSchemaFile(): VirtualFile? {
        return JsonSchemaProviderFactory.getResourceFile(
            this.javaClass,
            "/jsonSchemas/page.json"
        )
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.schema
    }
}