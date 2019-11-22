package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText

object WXMLModuleUtils {

    /**
     * 从一个wxml文件以及找到一个template的定义
     * 包含其导入的文件
     */
    fun findTemplateDefinition(wxmlPsiFile: WXMLPsiFile, templateName: String): PsiElement? {
        val elements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java)
        // 在本文件中找
        this.findTemplateDefinitionWithSingleFile(elements, templateName)?.let {
            return it
        }
        // 找到所有的import的文件引用
        val fileReferences = elements.asSequence().filter {
            it.tagName == "import"
        }.map { wxmlElement ->
            PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                it.name == "src"
            }
        }.mapNotNull {
            PsiTreeUtil.findChildOfType(it, WXMLStringText::class.java)
        }.mapNotNull { wxmlStringText ->
            wxmlStringText.references.findLast {
                it is FileReference
            }
        }.filterIsInstance<FileReference>()
        for (fileReference in fileReferences) {
            val resolveFile = fileReference.resolve()
            if (resolveFile is WXMLPsiFile) {
                findTemplateDefinitionWithSingleFile(resolveFile, templateName).let {
                    return it
                }
            }
        }
        return null
    }

    private fun findTemplateDefinitionWithSingleFile(wxmlPsiFile: WXMLPsiFile, templateName: String): WXMLStringText? {
        return this.findTemplateDefinitionWithSingleFile(
                PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java), templateName
        )
    }

    private fun findTemplateDefinitionWithSingleFile(
            wxmlElements: Collection<WXMLElement>, templateName: String
    ): WXMLStringText? {
        val templateElements = wxmlElements.filter {
            it.tagName == "template"
        }
        for (templateElement in templateElements) {
            val attributes = PsiTreeUtil.findChildrenOfType(templateElement, WXMLAttribute::class.java)
            val nameAttribute = attributes.find {
                it.name == "name"
            }
            if (nameAttribute != null) {
                val stringText = PsiTreeUtil.findChildOfType(
                        nameAttribute, WXMLStringText::class.java
                )
                if (stringText != null && stringText.text == templateName) {
                    return stringText
                }
            }
        }
        return null
    }

    fun findTemplateDefinitions(wxmlPsiFile: WXMLPsiFile): List<WXMLStringText> {
        val wxmlElements = PsiTreeUtil.findChildrenOfType(wxmlPsiFile, WXMLElement::class.java)
        return findTemplateDefinitions(wxmlElements)
    }

    fun findTemplateDefinitions(wxmlElements: Collection<WXMLElement>): List<WXMLStringText> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.tagName == "template"
        }.mapNotNull { wxmlElement ->
            PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java).find {
                it.name == "name"
            }
        }.mapNotNull {
            PsiTreeUtil.findChildOfType(
                    it, WXMLStringText::class.java
            )
        }
    }

    fun findValidIncludeTags(wxmlElements: Collection<WXMLElement>): List<WXMLElement> {
        return wxmlElements.filter { wxmlElement ->
            wxmlElement.tagName == "include" && PsiTreeUtil.findChildrenOfType(
                    wxmlElement, WXMLAttribute::class.java
            ).any {
                it.name == "src" && PsiTreeUtil.findChildOfType(
                        it, WXMLStringText::class.java
                )?.references?.lastOrNull()?.resolve() is WXMLPsiFile
            }
        }
    }

}