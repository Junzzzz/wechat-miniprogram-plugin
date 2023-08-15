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

package com.zxy.ijplugin.miniprogram.lang.wxss

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.zxy.ijplugin.miniprogram.reference.WXMLClassReference
import com.zxy.ijplugin.miniprogram.reference.WXMLIdReference

class WXMLIdOrClassRenameHandler : PsiElementRenameHandler() {

    companion object {
        fun getIdOrClassReference(dataContext: DataContext): PsiPolyVariantReference? {
            val reference = CommonDataKeys.PSI_FILE.getData(dataContext)
                ?.findReferenceAt(
                    CommonDataKeys.EDITOR.getData(dataContext)?.caretModel?.offset ?: return null
                )
            if (reference is WXMLIdReference) {
                return reference
            }
            if (reference is WXMLClassReference) {
                return reference
            }
            return null
        }
    }

    override fun isAvailableOnDataContext(dataContext: DataContext): Boolean {
        if (getIdOrClassReference(dataContext) != null) {
            return true
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext) {
        val reference = getIdOrClassReference(dataContext)
        val element = reference?.multiResolve(false)?.firstOrNull()?.element
        if (element != null) {
            invoke(element, project, null, editor)
        }
    }
}