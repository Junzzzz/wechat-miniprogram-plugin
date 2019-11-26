package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import com.zxy.ijplugin.wechat_miniprogram.context.isWechatMiniProgramContext

class WechatMiniProgramJsonSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): MutableList<JsonSchemaFileProvider> {
        return mutableListOf(AppJsonSchemaFileProvider(project), PageJsonSchemaFileProvider(project))
    }
}

fun isAppJsonFile(project: Project, virtualFile: VirtualFile): Boolean {
    val contentRoot = ProjectFileIndex.SERVICE.getInstance(
            project
    ).getContentRootForFile(virtualFile)
    // 在根目录下且名称为 app.json
    return virtualFile.parent == contentRoot && virtualFile.name == "app.json"
}

private class AppJsonSchemaFileProvider(private val project: Project) : JsonSchemaFileProvider {

    override fun getName(): String {
        return "app.json"
    }

    override fun isAvailable(virtualFile: VirtualFile): Boolean {
        return isAppJsonFile(project, virtualFile)
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
        return isWechatMiniProgramContext(this.project) && !isAppJsonFile(project, virtualFile)
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