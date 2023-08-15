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

package com.zxy.ijplugin.miniprogram.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.css.CssElementVisitor
import com.intellij.psi.css.CssImport
import com.intellij.psi.css.CssString
import com.intellij.psi.css.resolve.StylesheetFileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.utils.contentRange
import com.zxy.ijplugin.miniprogram.utils.findChildOfType

/**
 * 检查wxss的import是否有效
 */
class WXSSInvalidImportInspection : LocalInspectionTool() {

    companion object {
        const val QUICK_FIX_FAMILY_NAME = "WXSS导入语句"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : CssElementVisitor() {
            override fun visitCssImport(wxssImport: CssImport) {
                val string = wxssImport.findChildOfType<CssString>() ?: return
                val stringText = string.value
                if (stringText.isBlank()) {
                    holder.registerProblem(
                        string, TextRange.allOf(string.text), this@WXSSInvalidImportInspection.displayName
                    )
                    return
                }
                val references = string.references.filterIsInstance<FileReference>()
                if (references.isEmpty()) {
                    holder.registerProblem(
                        string, string.contentRange(), this@WXSSInvalidImportInspection.displayName
                    )
                } else {
                    val resolveElement = references.lastOrNull {
                        it !is StylesheetFileReference
                    }?.fileReferenceSet?.resolve()
                    if (resolveElement is WXSSPsiFile) {
                        if (resolveElement.virtualFile == RelateFileHolder.STYLE.findAppFile(resolveElement.project)) {
                            holder.registerProblem(
                                string, string.contentRange(), "${resolveElement.name}是全局样式，无需导入",
                                DeleteImportQuickFix(wxssImport)
                            )
                        }
                    } else {
                        holder.registerProblem(string, string.contentRange(), "无效的@import")
                    }
                }
            }
        }
    }

    class DeleteImportQuickFix(wxssImport: CssImport) :
        LocalQuickFixOnPsiElement(wxssImport) {

        override fun getFamilyName(): String {
            return QUICK_FIX_FAMILY_NAME
        }

        override fun getText(): String {
            return "删除导入语句"
        }

        override fun invoke(project: Project, psiFile: PsiFile, wxssImport: PsiElement, p3: PsiElement) {
            wxssImport.delete()
        }
    }

}