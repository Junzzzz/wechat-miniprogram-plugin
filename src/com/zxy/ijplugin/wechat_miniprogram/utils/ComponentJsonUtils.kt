package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.json.psi.JsonBooleanLiteral
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty

object ComponentJsonUtils {

    fun getUsingComponentItems(jsonPsiFile: JsonFile): MutableList<JsonProperty>? {
        val usingComponentsObjectValue = getUsingComponentPropertyValue(jsonPsiFile)
        return usingComponentsObjectValue?.propertyList
    }

    fun getUsingComponentPropertyValue(jsonPsiFile: JsonFile): JsonObject? {
        val root = jsonPsiFile.topLevelValue as? JsonObject ?: return null
        val usingComponentsProperty = root.findProperty("usingComponents") ?: return null
        return usingComponentsProperty.value as? JsonObject ?: return null
    }

    /**
     * 判断一个json文件是否是Component的配置文件
     * 具有{"component":"true"}
     */
    fun isComponentConfiguration(jsonPsiFile: JsonFile): Boolean {
        val root = jsonPsiFile.topLevelValue as? JsonObject ?: return false
        val isComponentProperty = root.findProperty("component") ?: return false
        return isComponentProperty.value?.let {
            it is JsonBooleanLiteral && it.value
        } == true
    }

}