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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.naming.AutomaticRenamer
import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory
import com.intellij.usageView.UsageInfo
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.utils.isComponentFile

class ComponentFilesAutomaticRenamerFactory : AutomaticRenamerFactory {

    companion object {
        fun isValidElement(element: PsiElement): Boolean {
            return element is PsiFile && isWechatMiniProgramContext(element) && isComponentFile(element)
        }
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun setEnabled(enabled: Boolean) {

    }

    override fun isApplicable(element: PsiElement): Boolean {
        return isValidElement(element)
    }

    override fun getOptionName(): String? {
        return "Rename component files (with the same name)"
    }

    override fun createRenamer(
        element: PsiElement, newName: String, usages: MutableCollection<UsageInfo>?
    ): AutomaticRenamer {
        return ComponentFilesAutomaticRenamer(element as PsiFile, newName)
    }

}

class ComponentFilesAutomaticRenamer(private val psiFile: PsiFile, newName: String) : AutomaticRenamer() {

    init {
        val virtualFile = psiFile.originalFile.virtualFile
        val extension = virtualFile.extension
        // 如果改变了扩展名，则忽略
        if (extension != null && newName.endsWith(extension)) {
            val newNameWithoutExtension = newName.removeSuffix(extension).removeSuffix(".")
            RelateFileHolder.INSTANCES.mapNotNull {
                it.findFile(psiFile)
            }.filter {
                it != psiFile
            }.forEach {
                val relateFileOldName = it.name
                val relateFileNewName = newNameWithoutExtension + relateFileOldName.removePrefix(
                    virtualFile.nameWithoutExtension
                )
                myElements.add(it)
                suggestAllNames(relateFileOldName, relateFileNewName)
            }
        }
    }

    override fun getDialogDescription(): String {
        return "Rename component files to:"
    }

    override fun getDialogTitle(): String {
        return "Rename"
    }

    override fun entityName(): String {
        return "Component files"
    }

    override fun isSelectedByDefault(): Boolean {
        return true
    }

}