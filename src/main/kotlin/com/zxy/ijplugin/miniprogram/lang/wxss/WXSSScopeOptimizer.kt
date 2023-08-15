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

package com.zxy.ijplugin.miniprogram.lang.wxss

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ScopeOptimizer
import com.intellij.psi.search.SearchScope
import com.zxy.ijplugin.miniprogram.completion.isAppWxssFile
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext

class WXSSScopeOptimizer : ScopeOptimizer {
    override fun getRestrictedUseScope(element: PsiElement): SearchScope? {
        if (isWechatMiniProgramContext(element) && element.containingFile is WXSSPsiFile) {
            val wxssPsiFile = element.containingFile as WXSSPsiFile
            return if (isAppWxssFile(wxssPsiFile.project, wxssPsiFile.virtualFile)) {
                GlobalSearchScope.fileScope(wxssPsiFile)
            } else {
                GlobalSearchScope.filesScope(element.project, mutableListOf(
                    wxssPsiFile, RelateFileHolder.MARKUP.findFile(wxssPsiFile),
                    RelateFileHolder.STYLE.findAppFile(element.project)
                ).filterNotNull().map { it.virtualFile })
            }
        }
        return null
    }
}