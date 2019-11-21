package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findAppFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSImport
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSVisitor
import com.zxy.ijplugin.wechat_miniprogram.utils.contentRange

/**
 * 检查wxss的import是否有效
 */
class WXSSInvalidImportInspection : LocalInspectionTool() {

    companion object {
        const val QUICK_FIX_FAMILY_NAME = "WXSS导入语句"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : WXSSVisitor() {
            override fun visitImport(wxssImport: WXSSImport) {
                val string = wxssImport.string ?: return
                val stringText = string.stringText
                if (stringText == null || stringText.text.isBlank()) {
                    holder.registerProblem(
                            string, TextRange.allOf(string.text), this@WXSSInvalidImportInspection.displayName
                    )
                    return
                }
                val references = stringText.references
                if (references.isEmpty()) {
                    holder.registerProblem(
                            stringText, stringText.contentRange(), this@WXSSInvalidImportInspection.displayName
                    )
                } else {
                    for (reference in references) {
                        if (reference is FileReference) {
                            val resolveElement = reference.resolve()
                            if (resolveElement is PsiDirectory) {
                                continue
                            } else if (resolveElement is PsiFile) {
                                if (resolveElement is WXSSPsiFile) {
                                    if (resolveElement.virtualFile == findAppFile(
                                                    resolveElement.project,
                                                    RelateFileType.WXSS
                                            )) {
                                        holder.registerProblem(
                                                stringText, stringText.contentRange(), "app.wxss是全局样式，无需导入",
                                                DeleteImportQuickFix(wxssImport)
                                        )
                                    } else {
                                        continue
                                    }
                                } else {
                                    holder.registerProblem(stringText, stringText.contentRange(), "仅能导入wxss文件")
                                }
                            } else {
                                holder.registerProblem(
                                        stringText, reference.rangeInElement, "路径无效", *reference.quickFixes
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    class DeleteImportQuickFix(wxssImport: WXSSImport) :
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