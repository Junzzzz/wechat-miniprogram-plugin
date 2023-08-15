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
import com.zxy.ijplugin.miniprogram.lang.wxs.WXSFileType
import com.zxy.ijplugin.miniprogram.qq.QSFileType
import com.zxy.ijplugin.miniprogram.reference.PathAttribute

/**
 * 检查wxml中的wxs标签的导入是否有效
 */
class WxmlWxsModuleImportInspection :
    WXMLElementPathAttributeInspection(arrayOf(PathAttribute("wxs", "src"))) {
    override fun getFileTypeText(project: Project): String {
        return if (project.isQQContext()) "wxs,qs" else "wxs"
    }

    override fun match(psiFile: PsiFile): Boolean {
        return psiFile.fileType == WXSFileType.INSTANCE || (psiFile.project.isQQContext() && psiFile.fileType == QSFileType.INSTANCE)
    }
}
