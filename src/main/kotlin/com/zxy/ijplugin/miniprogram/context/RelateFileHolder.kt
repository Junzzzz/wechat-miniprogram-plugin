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

import com.intellij.json.JsonFileType
import com.intellij.json.psi.JsonFile
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.qq.QSSFileType

abstract class RelateFileHolder {

    companion object {
        val MARKUP = QQCompatibleRelateFileHolder(QMLFileType.INSTANCE, WXMLFileType.INSTANCE)
        val SCRIPT = SingleFileTypeFileHolder(JavaScriptFileType.INSTANCE)
        val STYLE = QQCompatibleRelateFileHolder(QSSFileType.INSTANCE, WXSSFileType.INSTANCE)
        val JSON = SingleFileTypeFileHolder(JsonFileType.INSTANCE)

        val INSTANCES = arrayOf(MARKUP, SCRIPT, STYLE, JSON)

        fun findInstance(psiFile: PsiFile): RelateFileHolder? {
            return when (psiFile) {
                is WXMLPsiFile -> MARKUP
                is WXSSPsiFile -> STYLE
                is JsonFile -> JSON
                is JSFile -> SCRIPT
                else -> null
            }
        }
    }

    fun findAppFile(project: Project): PsiFile? {
        return this.findFile(findMiniProgramRootDir(project)?.files?.filter {
            it.virtualFile.nameWithoutExtension == "app"
        }?.toTypedArray() ?: return null, project)
    }

    /**
     * 从一组文件中找到相关的文件
     */
    protected abstract fun findFile(files: Array<PsiFile>, project: Project): PsiFile?

    fun findFile(relatedFile: PsiFile): PsiFile? {
        val psiFiles = relatedFile.parent?.files ?: return null
        return this.findFile(
            psiFiles.filter { it.virtualFile.nameWithoutExtension == relatedFile.virtualFile.nameWithoutExtension }
                .toTypedArray(), relatedFile.project
        )
    }
}