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
import com.sun.javafx.scene.CameraHelper.project
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl.WXSSIdSelectorImpl
import java.util.*


class WXSSIdChooseByNameContributor : ChooseByNameContributor {

    override fun getItemsByName(
            name: String?, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val idSelectors = getIdSelectorsByProject(project).filter { it.lastChild.text==name }
        println("name:$name  ")
        println(idSelectors.toTypedArray().map { it.presentation.presentableText + it.presentation.locationString })
        return idSelectors.toTypedArray()
    }

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {

        println("getNames ${
        Arrays.toString(getIdSelectorsByProject(project).map { wxssIdSelector ->
            wxssIdSelector.node.findChildByType(WXSSTypes.ID)!!.text
        }.toTypedArray())
        }")
        return getIdSelectorsByProject(project).map { wxssIdSelector ->
            wxssIdSelector.node.findChildByType(WXSSTypes.ID)!!.text
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