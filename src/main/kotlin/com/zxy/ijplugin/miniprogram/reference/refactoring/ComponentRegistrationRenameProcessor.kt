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

package com.zxy.ijplugin.miniprogram.reference.refactoring

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.xml.TagNameReference
import com.intellij.psi.search.SearchScope
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.reference.JsonRegistrationReference
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType

/**
 * 重命名在WXML组件在json文件中的定义
 */
class ComponentRegistrationRenameProcessor : RenamePsiElementProcessor() {

    override fun findReferences(
        element: PsiElement, searchScope: SearchScope, searchInCommentsAndStrings: Boolean
    ): MutableCollection<PsiReference> {
        element as JsonProperty
        val containingFile = element.containingFile
        if (containingFile !is JsonFile) return mutableListOf()
        val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(containingFile) as? WXMLPsiFile ?: return mutableListOf()
        return wxmlPsiFile.findChildrenOfType<XmlTag>().filter {
            it.name == element.name
        }.flatMap {
            it.references.toList()
        }.filterIsInstance<TagNameReference>()
            .filter {
                it.resolve() == element
            }.toMutableList()
    }

    override fun canProcessElement(element: PsiElement): Boolean {
        return element is JsonProperty && element.references.any {
            it is JsonRegistrationReference
        }
    }
}