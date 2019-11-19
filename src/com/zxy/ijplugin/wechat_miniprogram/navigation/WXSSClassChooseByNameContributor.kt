package com.zxy.ijplugin.wechat_miniprogram.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl.WXSSClassSelectorImpl

class WXSSClassChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
            name: String, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        return getClassSelectorsByProject(project).filter { it.className == name }.toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return getClassSelectorsByProject(project).mapNotNull { it.className }.toTypedArray()
    }

    private fun getClassSelectorsByProject(project: Project): List<WXSSClassSelectorImpl> {
        val virtualFiles = FileTypeIndex.getFiles(
                WXSSFileType.INSTANCE, GlobalSearchScope.allScope(project)
        )
        return virtualFiles.flatMap { virtualFile ->
            val wxssFile = PsiManager.getInstance(project).findFile(virtualFile) as WXSSPsiFile?
            wxssFile?.let {
                PsiTreeUtil.findChildrenOfType(wxssFile, WXSSClassSelectorImpl::class.java)
            } ?: emptyList()
        }
    }

}