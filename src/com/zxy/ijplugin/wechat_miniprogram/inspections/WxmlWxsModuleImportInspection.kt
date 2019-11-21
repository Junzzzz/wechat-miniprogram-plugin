package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*
import com.zxy.ijplugin.wechat_miniprogram.lang.wxs.WXSFileType
import com.zxy.ijplugin.wechat_miniprogram.utils.contentRange

/**
 * 检查wxml中的wxs标签的导入是否有效
 */
class WxmlWxsModuleImportInspection : WXMLInspectionBase() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : WXMLVisitor() {
            override fun visitElement(wxmlElement: WXMLElement) {
                if (wxmlElement.tagName == "wxs") {
                    val wxsSrcAttribute = PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                        it.name == "src"
                    }
                    val string = PsiTreeUtil.getChildOfType(wxsSrcAttribute, WXMLString::class.java) ?: return
                    val stringText = PsiTreeUtil.getChildOfType(string, WXMLStringText::class.java)
                    if (stringText == null || stringText.text.isBlank()) {
                        holder.registerProblem(
                                string, TextRange.allOf(string.text), this@WxmlWxsModuleImportInspection.displayName
                        )
                        return
                    }

                    val references = stringText.references
                    if (references.isEmpty()) {
                        holder.registerProblem(
                                stringText, stringText.contentRange(), this@WxmlWxsModuleImportInspection.displayName
                        )
                    } else {
                        for (reference in references) {
                            if (reference is FileReference) {
                                val resolveElement = reference.resolve()
                                if (resolveElement is PsiDirectory) {
                                    continue
                                } else if (resolveElement is PsiFile) {
                                    if (resolveElement.fileType == WXSFileType.INSTANCE) {
                                        continue
                                    } else {
                                        holder.registerProblem(stringText, stringText.contentRange(), "仅能导入wxs文件")
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
    }

}