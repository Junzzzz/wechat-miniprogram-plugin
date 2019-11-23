package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty

object ComponentJsonUtils {

    fun getUsingComponentItems(jsonPsiFile: JsonFile): MutableList<JsonProperty>? {
        val root = jsonPsiFile.topLevelValue as? JsonObject ?: return null
        val usingComponentsProperty = root.findProperty("usingComponents") ?: return null
        val usingComponentsObjectValue = usingComponentsProperty.value as? JsonObject ?: return null
        return usingComponentsObjectValue.propertyList
    }

}