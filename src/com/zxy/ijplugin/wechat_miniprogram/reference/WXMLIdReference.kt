package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector

class WXMLIdReference(wxmlStringText: WXMLStringText) : PsiPolyVariantReferenceBase<WXMLStringText>(wxmlStringText) {

    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        val id = this.element.text
        val project = this.element.project
        val wxmlFile = this.element.containingFile.virtualFile
        // 在wxml文件附近找同名的wxss文件
        val wxssFile = findRelateFile(wxmlFile, RelateFileType.WXSS)
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
        val results = this.multiResolve(false)
        return results.getOrNull(0)?.element
    }

}