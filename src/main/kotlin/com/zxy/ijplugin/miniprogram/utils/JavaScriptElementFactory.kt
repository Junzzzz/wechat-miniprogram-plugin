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

import com.intellij.lang.javascript.JavascriptLanguage
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory

object JavaScriptElementFactory {

    fun createProperty(project: Project, text: String): JSProperty {
        return createDummyFile(
            project, """
            ({
                $text
            })
        """.trimIndent()
        ).findChildOfType()!!
    }

    private fun createDummyFile(project: Project, fileContent: String): PsiFile {
        val name = "dummy.js"
        return PsiFileFactory.getInstance(project).createFileFromText(name, JavascriptLanguage.INSTANCE, fileContent)
    }

}