package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.toArray
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector

class WXSSIdChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
            name: String?, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val idSelectors = getIdSelectorsByProject(project)
        idSelectors
        return idSelectors.toArray(Array(idSelectors.size))
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        return getIdSelectorsByProject(project).map { wxssIdSelector ->
            wxssIdSelector.lastChild.text
        }.toTypedArray()
    }



    private fun getIdSelectorsByProject(project: Project): List<WXSSIdSelector> {
        val virtualFiles = FileTypeIndex.getFiles(WXSSFileType.INSTANCE, GlobalSearchScope.allScope(project))
        return virtualFiles.flatMap { virtualFile ->
            val wxssFile = PsiManager.getInstance(project).findFile(virtualFile) as WXSSPsiFile?
            wxssFile?.let {
                PsiTreeUtil.findChildrenOfType(wxssFile, WXSSIdSelector::class.java)

            } ?: emptyList()
        }
    }
}