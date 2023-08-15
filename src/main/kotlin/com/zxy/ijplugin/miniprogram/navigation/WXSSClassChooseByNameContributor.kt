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
import com.intellij.psi.css.CssClass
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.qq.QSSFileType

class WXSSClassChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
        name: String, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        return getClassSelectorsByProject(project).filter { it.name == name }.toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return getClassSelectorsByProject(project).mapNotNull { it.name }.toTypedArray()
    }

    private fun getClassSelectorsByProject(project: Project): List<CssClass> {
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
                PsiTreeUtil.findChildrenOfType(wxssFile, CssClass::class.java)
            } ?: emptyList()
        }
    }

}