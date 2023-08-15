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
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLModuleUtils
import com.zxy.ijplugin.miniprogram.qq.QMLFileType

class WXMLTemplateDefinitionChooseByNameContributor : ChooseByNameContributor {
    override fun getItemsByName(
        name: String, pattern: String?, project: Project, includeNonProjectItems: Boolean
    ): Array<out NavigationItem> {
        return getTemplateDefinition(project).filter { it.value == name }
            .filterIsInstance<XmlAttributeValueImpl>()
            .toTypedArray()
    }

    override fun getNames(project: Project, p1: Boolean): Array<String> {
        return getTemplateDefinition(project).distinct().map {
            it.value
        }.toTypedArray()
    }

    private fun getTemplateDefinition(project: Project): List<XmlAttributeValue> {
        val virtualFiles = mutableListOf<VirtualFile>()
        val allScope = GlobalSearchScope.allScope(project)
        virtualFiles.addAll(
            FileTypeIndex.getFiles(
                WXMLFileType.INSTANCE, allScope
            )
        )
        if (project.isQQContext()) {
            virtualFiles.addAll(
                FileTypeIndex.getFiles(
                    QMLFileType.INSTANCE, allScope
                )
            )
        }
        return virtualFiles.flatMap { virtualFile ->
            val wxmlPsiFile = PsiManager.getInstance(project).findFile(virtualFile) as WXMLPsiFile?
            wxmlPsiFile?.let {
                WXMLModuleUtils.findTemplateDefinitions(it)
            } ?: emptyList()
        }.distinct()
    }
}