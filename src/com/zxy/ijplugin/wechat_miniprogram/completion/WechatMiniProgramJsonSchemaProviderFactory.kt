package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType

class WechatMiniProgramJsonSchemaProviderFactory:JsonSchemaProviderFactory {
    override fun getProviders(project: Project): MutableList<JsonSchemaFileProvider> {
        return mutableListOf(WechatMiniProgramProjectConfigJsonSchemaFileProvider())
    }
}

class WechatMiniProgramProjectConfigJsonSchemaFileProvider : JsonSchemaFileProvider {

    override fun getName(): String {
        return "app.json"
    }

    override fun isAvailable(virtualFile: VirtualFile): Boolean {
        return virtualFile.name == "app.json"
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