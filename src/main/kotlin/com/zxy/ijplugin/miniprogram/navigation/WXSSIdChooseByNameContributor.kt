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

package com.zxy.ijplugin.miniprogram.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.css.CssIdSelector
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.qq.QSSFileType


class WXSSIdChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
        name: String?, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val idSelectors = getIdSelectorsByProject(project).filter { it.name == name }
        return idSelectors.toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return getIdSelectorsByProject(project).mapNotNull { wxssIdSelector ->
            wxssIdSelector.name
        }.toTypedArray()
    }

    private fun getIdSelectorsByProject(project: Project): List<CssIdSelector> {
        val virtualFiles = mutableListOf<VirtualFile>()
        val allScope = GlobalSearchScope.allScope(project)
        virtualFiles.addAll(
            FileTypeIndex.getFiles(
                WXSSFileType.INSTANCE, allScope
            )
        )
        if (project.isQQContext()) {
            virtualFiles.addAll(
                FileTypeIndex.getFiles(
                    QSSFileType.INSTANCE, allScope
                )
            )
        }
        return virtualFiles.flatMap { virtualFile ->
            val wxssFile = PsiManager.getInstance(project).findFile(virtualFile) as WXSSPsiFile?
            wxssFile?.let {
                PsiTreeUtil.findChildrenOfType(wxssFile, CssIdSelector::class.java)
            } ?: emptyList()
        }
    }

}