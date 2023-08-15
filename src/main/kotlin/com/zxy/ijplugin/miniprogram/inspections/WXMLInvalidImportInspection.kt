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

package com.zxy.ijplugin.miniprogram.inspections

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.reference.PathAttribute

/**
 * 对wxml 中的import和include标签的src属性进行路径检查
 */
class WXMLInvalidImportInspection : WXMLElementPathAttributeInspection(
    arrayOf(
        PathAttribute("import", "src"),
        PathAttribute("include", "src")
    )
) {
    override fun getFileTypeText(project: Project): String {
        return if (project.isQQContext()) "qml,wxml" else "wxml"
    }

    override fun match(psiFile: PsiFile): Boolean {
        return if (psiFile.project.isQQContext()) psiFile is WXMLPsiFile else psiFile.fileType == WXMLFileType.INSTANCE
    }
}