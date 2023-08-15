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
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.intellij.refactoring.rename.RenameHandler
import com.zxy.ijplugin.miniprogram.reference.ComponentFileReference


class ComponentPathLastReferenceRenameHandler : RenameHandler {
    override fun isAvailableOnDataContext(dataContext: DataContext): Boolean {
        return findComponentReference(dataContext) != null
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile, dataContext: DataContext) {
        val componentReference = findComponentReference(file, dataContext) ?: return
        componentReference.multiResolve(false).asSequence().mapNotNull { it.element }.filterIsInstance<JsonFile>()
            .firstOrNull()?.let {
                CommandProcessor.getInstance().runUndoTransparentAction {
                    PsiElementRenameHandler.invoke(it, project, it, editor)
                }
            }
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext) {

    }

}

private fun findComponentReference(dataContext: DataContext): ComponentFileReference? {
    val file = dataContext.getData(CommonDataKeys.PSI_FILE) ?: return null
    return findComponentReference(file, dataContext)
}

private fun findComponentReference(file: PsiFile, dataContext: DataContext): ComponentFileReference? {
    val offset = dataContext.getData(CommonDataKeys.CARET)?.offset ?: return null
    return file.findReferenceAt(offset) as? ComponentFileReference
}