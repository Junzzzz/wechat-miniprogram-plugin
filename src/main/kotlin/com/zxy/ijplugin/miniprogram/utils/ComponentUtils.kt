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

import com.intellij.json.psi.JsonFile
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxs.WXSFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.qq.QSFileType
import com.zxy.ijplugin.miniprogram.qq.QSSFileType
import com.zxy.ijplugin.miniprogram.settings.MiniProgramType
import com.zxy.ijplugin.miniprogram.settings.MyProjectSettings

fun isComponentFile(psiFile: PsiFile): Boolean {
    return psiFile is JsonFile || psiFile.project.let {
        MyProjectSettings.getState(psiFile.project).miniprogramType
    }.let {
        (it == MiniProgramType.QQ && (psiFile.fileType == QMLFileType.INSTANCE || psiFile.fileType == QSSFileType.INSTANCE || psiFile.fileType == QSFileType.INSTANCE))
                || psiFile.fileType === WXMLFileType.INSTANCE
                || psiFile.fileType == WXSSFileType.INSTANCE
                || psiFile.fileType == WXSFileType.INSTANCE
    }
}