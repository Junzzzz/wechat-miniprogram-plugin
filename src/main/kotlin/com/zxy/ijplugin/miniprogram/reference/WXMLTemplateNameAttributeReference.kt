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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.valueTextRangeInSelf
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType

class WXMLTemplateNameAttributeReference(element: XmlAttributeValue) :
    PsiPolyVariantReferenceBase<XmlAttributeValue>(element, element.valueTextRangeInSelf()) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = element.project
        val psiManager = PsiManager.getInstance(project)
        val wxmlFiles = mutableListOf<VirtualFile>()
        wxmlFiles.addAll(FilenameIndex.getAllFilesByExt(project, WXMLFileType.INSTANCE.defaultExtension))
        if (project.isQQContext()) {
            wxmlFiles.addAll(FilenameIndex.getAllFilesByExt(project, QMLFileType.INSTANCE.defaultExtension))
        }
        return wxmlFiles.flatMap { it ->
            psiManager.findFile(it)?.findChildrenOfType<XmlTag>()?.filter { xmlTag ->
                xmlTag.name == "template"
            }?.mapNotNull {
                it.getAttribute("is")?.valueElement
            }?.filter { xmlAttributeValue ->
                xmlAttributeValue.references.any {
                    it is WXMLTemplateIsAttributeReference && it.resolve() == this.element
                }
            } ?: emptyList()
        }.map {
            PsiElementResolveResult(it)
        }.toTypedArray()
    }

    override fun isSoft(): Boolean {
        return true
    }

}