package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*
import com.zxy.ijplugin.wechat_miniprogram.reference.PathAttribute
import com.zxy.ijplugin.wechat_miniprogram.utils.contentRange

/**
 * 检查wxml中可以被解析为路径的标签的路径的正确性
 * @see pathAttributes 目标标签和属性
 * @see fileType 路径指向的文件类型
 */
abstract class WXMLElementPathAttributeInspection(
        private val pathAttributes: Array<PathAttribute>, private val fileType: FileType
) : WXMLInspectionBase() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : WXMLVisitor() {
            override fun visitElement(wxmlElement: WXMLElement) {

                val target = pathAttributes.find {
                    it.tagName == wxmlElement.tagName
                }
                if (target != null) {
                    val pathAttribute = PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                        it.name == target.attributeName
                    }
                    val string = PsiTreeUtil.getChildOfType(pathAttribute, WXMLString::class.java) ?: return
                    val stringText = PsiTreeUtil.getChildOfType(string, WXMLStringText::class.java)
                    if (stringText == null || stringText.text.isBlank()) {
                        holder.registerProblem(
                                string, TextRange.allOf(string.text),
                                this@WXMLElementPathAttributeInspection.displayName
                        )
                        return
                    }

                    val references = stringText.references
                    if (references.isEmpty()) {
                        holder.registerProblem(
                                stringText, stringText.contentRange(),
                                this@WXMLElementPathAttributeInspection.displayName
                        )
                    } else {
                        for (reference in references) {
                            if (reference is FileReference) {
                                val resolveElement = reference.resolve()
                                if (resolveElement is PsiDirectory) {
                                    continue
                                } else if (resolveElement is PsiFile) {
                                    if (resolveElement.fileType == fileType) {
                                        continue
                                    } else {
                                        val suffix = fileType.defaultExtension
                                        holder.registerProblem(stringText, stringText.contentRange(), "仅能导入${suffix}文件")
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