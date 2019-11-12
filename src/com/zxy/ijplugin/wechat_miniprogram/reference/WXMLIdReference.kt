package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector

class WXMLIdReference(wxmlStringText: WXMLStringText) : PsiReferenceBase<WXMLStringText>(wxmlStringText),
        PsiPolyVariantReference {

    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        val id = this.element.text
        val project = this.element.project
        val wxmlFile = this.element.containingFile.virtualFile
        // 在wxml文件附近找同名的wxss文件
        val wxssFile = wxmlFile.parent.children.find { it.nameWithoutExtension == wxmlFile.nameWithoutExtension && it.extension == WXSSFileType.INSTANCE.defaultExtension }
        val psiManager = PsiManager.getInstance(project)
        if (wxssFile != null) {
            val wxssPsiFile = psiManager.findFile(wxssFile)
            if (wxssPsiFile != null) {
                val idSelectors = PsiTreeUtil.findChildrenOfType(wxssPsiFile, WXSSIdSelector::class.java)
                return idSelectors.filter { id == it.id }.map {
                    PsiElementResolveResult(it)
                }.toTypedArray()
            }
        }
        return emptyArray()
    }


    override fun resolve(): PsiElement? {
        return multiResolve(false).let {
            if (it.size == 1) {
                it[0].element
            } else {
                null
            }
        }

    }

    override fun getRangeInElement(): TextRange {
        return TextRange(0, this.element.textLength + 1)
    }
}