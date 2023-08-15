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

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class QQCompatibleRelateFileHolder(
    private val onlyQQFileType: LanguageFileType,
    private val compatibleWeiXinFileType: LanguageFileType
) :
    RelateFileHolder() {
    override fun findFile(files: Array<PsiFile>, project: Project): PsiFile? {
        return if (project.isQQContext()) {
            files.find {
                it.fileType == onlyQQFileType
            } ?: files.find {
                it.fileType == compatibleWeiXinFileType
            }
        } else {
            files.find {
                it.fileType == compatibleWeiXinFileType
            }
        }
    }


}