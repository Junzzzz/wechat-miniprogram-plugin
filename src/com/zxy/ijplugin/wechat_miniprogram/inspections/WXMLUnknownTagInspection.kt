package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.json.psi.JsonFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiManager
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLClosedElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLVisitor
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.contentRange
import com.zxy.ijplugin.wechat_miniprogram.utils.getPathRelativeToRootRemoveExt

class WXMLUnknownTagInspection : WXMLInspectionBase() {

    companion object {
        const val QUICK_FIX_FAMILY_NAME = "WXML标签"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : WXMLVisitor() {
            override fun visitStartTag(wxmlStartTag: WXMLStartTag) {
                if (wxmlStartTag is WXMLTag) {
                    visitTag(wxmlStartTag)
                }
            }

            override fun visitClosedElement(wxmlClosedElement: WXMLClosedElement) {
                if (wxmlClosedElement is WXMLTag) {
                    visitTag(wxmlClosedElement)
                }
            }

            private fun visitTag(wxmlTag: WXMLTag) {
                val tagNameNode = wxmlTag.getTagNameNode() ?: return
                val tagName = tagNameNode.text
                if (WXMLMetadata.ELEMENT_DESCRIPTORS.any {
                            it.name == tagName
                        }) {
                    // 是自带组件
                    return
                }

                val project = wxmlTag.project

                val currentJsonFile = findRelateFile(wxmlTag.containingFile.virtualFile, RelateFileType.JSON)
                val psiManager = PsiManager.getInstance(wxmlTag.project)
                val currentJsonPsiFile = currentJsonFile?.let {
                    psiManager.findFile(it)
                } as? JsonFile
                val usingComponentItems = currentJsonPsiFile?.let {
                    ComponentJsonUtils.getUsingComponentItems(it)
                }
                if (usingComponentItems?.any {
                            it.name == tagName
                        } != true) {
                    // 没有注册的标签
                    val jsonConfigurationFiles = ComponentJsonUtils.getAllComponentConfigurationFile(project).filter {
                        it != currentJsonPsiFile
                    }
                    val quickFixList = if (currentJsonPsiFile == null) {
                        emptyArray()
                    } else {
                        jsonConfigurationFiles.filter {
                            it.virtualFile.nameWithoutExtension == tagName
                        }.let { list ->
                            list.map {
                                object : LocalQuickFix {

                                    override fun getFamilyName(): String {
                                        return QUICK_FIX_FAMILY_NAME
                                    }

                                    override fun getName(): String {
                                        // 如果有多个组件匹配
                                        // 则显示他们相对于根目录的路径
                                        val componentName = if (list.size > 1) it.virtualFile.getPathRelativeToRootRemoveExt(
                                                it.project
                                        ) else {
                                            it.virtualFile.nameWithoutExtension
                                        }
                                        return "在${currentJsonPsiFile.name}中注册$componentName"
                                    }

                                    override fun applyFix(p0: Project, problemDescriptor: ProblemDescriptor) {
                                        ComponentJsonUtils.registerComponent(currentJsonPsiFile, it)
                                    }
                                }

                            }.toTypedArray<LocalQuickFix>()
                        }
                    }
                    holder.registerProblem(
                            tagNameNode.psi, tagNameNode.psi.contentRange(), "未知的标签：$tagName",
                            *quickFixList
                    )

                }
            }
        }
    }

}

