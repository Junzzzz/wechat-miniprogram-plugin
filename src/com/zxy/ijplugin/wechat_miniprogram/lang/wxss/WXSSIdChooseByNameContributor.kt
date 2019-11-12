package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl.WXSSIdSelectorImpl


class WXSSIdChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
            name: String?, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val idSelectors = getIdSelectorsByProject(project).filter { it.lastChild.text==name }
        return idSelectors.toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return getIdSelectorsByProject(project).mapNotNull { wxssIdSelector ->
            wxssIdSelector.id
        }.toTypedArray()
    }

    private fun getIdSelectorsByProject(project: Project): List<WXSSIdSelectorImpl> {
        val virtualFiles = FileTypeIndex.getFiles(WXSSFileType.INSTANCE, GlobalSearchScope.allScope(project))
        return virtualFiles.flatMap { virtualFile ->
            val wxssFile = PsiManager.getInstance(project).findFile(virtualFile) as WXSSPsiFile?
            wxssFile?.let {
                PsiTreeUtil.findChildrenOfType(wxssFile, WXSSIdSelectorImpl::class.java)
            } ?: emptyList()
        }
    }

}