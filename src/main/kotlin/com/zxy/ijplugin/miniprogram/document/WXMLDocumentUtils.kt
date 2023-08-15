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

package com.zxy.ijplugin.miniprogram.document

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElement

/**
 * 判断一个元素是否是json的字符串，
 * 并且位于elementDescriptions.json文件中
 */
internal fun isInsideJsonConfigFile(element: PsiElement): Boolean {
    if (element is JsonStringLiteral) {
        val jsonFile = element.containingFile
        if (jsonFile is JsonFile && jsonFile.name == "elementDescriptions.json") {
            val topLevelValue = jsonFile.topLevelValue
            if (topLevelValue is JsonObject) {
                return true
            }
        }
    }
    return false
}