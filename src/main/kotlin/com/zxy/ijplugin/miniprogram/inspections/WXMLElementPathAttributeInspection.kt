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

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.valueTextRangeInSelf
import com.zxy.ijplugin.miniprogram.reference.PathAttribute

/**
 * 检查wxml中可以被解析为路径的标签的路径的正确性
 * @see pathAttributes 目标标签和属性
 */
abstract class WXMLElementPathAttributeInspection(
    private val pathAttributes: Array<PathAttribute>
) : WXMLInspectionBase() {

    /**
     * 显示在意图中的文件类型描述
     */
    abstract fun getFileTypeText(project: Project): String

    /**
     * 判断是否是正确的文件
     */
    abstract fun match(psiFile: PsiFile): Boolean

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {
            override fun visitXmlTag(xmlTag: XmlTag) {
                if (xmlTag.language != WXMLLanguage.INSTANCE) {
                    return
                }

                val target = pathAttributes.find {
                    it.tagName == xmlTag.name
                }
                if (target != null) {
                    val pathAttribute = PsiTreeUtil.findChildrenOfType(xmlTag, XmlAttribute::class.java).find {
                        it.name == target.attributeName
                    }
                    val xmlAttributeValue = PsiTreeUtil.getChildOfType(pathAttribute, XmlAttributeValue::class.java)
                        ?: return
                    if (xmlAttributeValue.value.isBlank()) {
                        holder.registerProblem(
                            xmlAttributeValue, TextRange.allOf(xmlAttributeValue.text),
                            this@WXMLElementPathAttributeInspection.displayName
                        )
                        return
                    }

                    val references = xmlAttributeValue.references
                    if (references.isEmpty()) {
                        holder.registerProblem(
                            xmlAttributeValue, xmlAttributeValue.valueTextRangeInSelf(),
                            this@WXMLElementPathAttributeInspection.displayName
                        )
                    } else {
                        for (reference in references) {
                            if (reference is FileReference) {
                                val resolveElement = reference.resolve()
                                if (resolveElement is PsiDirectory) {
                                    continue
                                } else if (resolveElement is PsiFile) {
                                    if (match(resolveElement)) {
                                        continue
                                    } else {
                                        holder.registerProblem(
                                            xmlAttributeValue, xmlAttributeValue.valueTextRangeInSelf(),
                                            "仅能导入${getFileTypeText(resolveElement.project)}文件"
                                        )
                                    }
                                } else {
                                    holder.registerProblem(
                                        xmlAttributeValue, reference.rangeInElement, "路径无效", *reference.quickFixes
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