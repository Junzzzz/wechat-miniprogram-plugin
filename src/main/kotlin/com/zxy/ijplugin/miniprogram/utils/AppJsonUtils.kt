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

package com.zxy.ijplugin.miniprogram.utils

import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.*
import com.intellij.openapi.project.Project
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder

object AppJsonUtils {

    fun findUsingComponentsValue(project: Project): JsonObject? {
        val jsonPsiFile = RelateFileHolder.JSON.findAppFile(project)
        return (jsonPsiFile as? JsonFile)?.let {
            ComponentJsonUtils.getUsingComponentPropertyValue(it)
        }
    }

    fun findUsingComponentItems(project: Project): MutableList<JsonProperty>? {
        return findUsingComponentsValue(project)?.propertyList
    }

    fun registerPage(appJsonFile: JsonFile, pagePath: String) {
        val project = appJsonFile.project
        val root = appJsonFile.topLevelValue as? JsonObject ?: return
        val pagesProperty = root.findProperty("pages") ?: return
        val pages = pagesProperty.value as? JsonArray ?: return

        val valueList = pages.valueList
        val string = "\"$pagePath\""
        if (valueList.none {
                it.text == string
            }) {
            // 不存在才会写入app.json的pages数组
            val jsonElementGenerator = JsonElementGenerator(
                project
            )
            val closeBracket = pages.node?.findChildByType(
                JsonElementTypes.R_BRACKET
            )?.psi
            if (valueList.isNotEmpty()) {
                pages.addBefore(jsonElementGenerator.createComma(), closeBracket)
            }
            pages.addBefore(jsonElementGenerator.createStringLiteral(pagePath), closeBracket)
        }

    }
}