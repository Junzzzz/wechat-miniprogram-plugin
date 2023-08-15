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

package com.zxy.ijplugin.miniprogram.lang.wxss.utils

import com.intellij.psi.css.CssImport
import com.intellij.psi.css.CssString
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile

object WXSSModuleUtils {

    fun findImportedFilesWithSelf(wxssPsiFile: WXSSPsiFile): Collection<WXSSPsiFile> {
        return findImportedFiles(wxssPsiFile).toMutableList().apply {
            this.add(0, wxssPsiFile)
        }
    }

    private fun findImportedFiles(wxssPsiFile: WXSSPsiFile): Collection<WXSSPsiFile> {
        val result = hashSetOf<WXSSPsiFile>()
        addImportedFiles(wxssPsiFile, result)
        return result
    }

    private fun addImportedFiles(wxssPsiFile: WXSSPsiFile, result: MutableCollection<WXSSPsiFile>) {
        val imports = PsiTreeUtil.findChildrenOfType(wxssPsiFile, CssImport::class.java)
        imports.mapNotNull { wxssImport ->
            val references = PsiTreeUtil.findChildOfType(wxssImport, CssString::class.java)?.references
                ?: emptyArray()
            val resolveResult = references.find { it is FileReference }?.let { it as FileReference? }
                ?.lastFileReference?.resolve()
            if (resolveResult is WXSSPsiFile) {
                resolveResult
            } else {
                null
            }
        }.forEach {
            if (!result.contains(it)) {
                // 如果此文件已经在结果集中
                // 则忽略

                // 添加此文件
                result.add(it)
                // 继续解析此文件的imports
                addImportedFiles(it, result)
            }
        }
    }

}