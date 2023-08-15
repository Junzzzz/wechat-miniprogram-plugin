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

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.move.MoveCallback
import com.intellij.refactoring.move.MoveHandlerDelegate
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesHandler
import com.zxy.ijplugin.miniprogram.reference.ComponentFileReference

/**
 * 对json文件中配置的组件进行移动
 * 在json文件引用的最后一段调用
 * @see ComponentFileReference
 */
class MoveComponentReferenceHandler : MoveHandlerDelegate() {

    override fun canMove(
        elements: Array<out PsiElement>?, targetContainer: PsiElement?, reference: PsiReference?
    ): Boolean {
        return reference is ComponentFileReference
    }

    override fun doMove(
        project: Project?, elements: Array<out PsiElement>?, targetContainer: PsiElement?, callback: MoveCallback?
    ) {
        val moveFilesOrDirectoriesHandler = MoveFilesOrDirectoriesHandler()
        if (moveFilesOrDirectoriesHandler.canMove(elements, targetContainer, null)) {
            moveFilesOrDirectoriesHandler.doMove(project, elements, targetContainer, callback)
        }
    }

    override fun tryToMove(
        element: PsiElement?, project: Project, dataContext: DataContext, reference: PsiReference?,
        editor: Editor?
    ): Boolean {
        element ?: return false
        val componentReference = element.parentOfType<JsonStringLiteral>()?.references?.asSequence()
            ?.filterIsInstance<ComponentFileReference>()
            ?.lastOrNull()
        // TODO Change component registration when it moved
        if (componentReference != null) {
            this.doMove(
                project, componentReference.multiResolve(false).mapNotNull { it.element }.toTypedArray(),
                LangDataKeys.TARGET_PSI_ELEMENT.getData(dataContext), null
            )
            return true
        }
        return false
    }

}