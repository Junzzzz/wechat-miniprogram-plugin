package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findAppFile

object AppJsonUtils {

    fun findUsingComponentsValue(project: Project): JsonObject? {
        val jsonFile = findAppFile(project, RelateFileType.JSON)
        val jsonPsiFile = jsonFile?.let {
            val psiManager = PsiManager.getInstance(project)
            psiManager.findFile(it)
        }
        return (jsonPsiFile as? JsonFile)?.let {
            ComponentJsonUtils.getUsingComponentPropertyValue(it)
        }
    }

}