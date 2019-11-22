package com.zxy.ijplugin.wechat_miniprogram.navigation

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl.WXMLStringTextImpl
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType

class WXMLTemplateDefinitionChooseByNameContributor : ChooseByNameContributor {
    override fun getItemsByName(
            name: String, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<out NavigationItem> {
        return getTemplateDefinition(project).filter { it.name == name }.toTypedArray()
    }

    override fun getNames(project: Project, p1: Boolean): Array<String> {
        return getTemplateDefinition(project).map {
            it.text
        }.toTypedArray()
    }

    private fun getTemplateDefinition(project: Project): List<WXMLStringTextImpl> {
        val virtualFiles = FileTypeIndex.getFiles(
                WXSSFileType.INSTANCE, GlobalSearchScope.allScope(project)
        )
        return virtualFiles.flatMap { virtualFile ->
            val wxmlPsiFile = PsiManager.getInstance(project).findFile(virtualFile) as WXMLPsiFile?
            wxmlPsiFile?.let {
                WXMLModuleUtils.findTemplateDefinitions(it)
            } ?: emptyList()
        }.map {
            it as WXMLStringTextImpl
        }
    }
}