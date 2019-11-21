package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.substring

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

    override fun isReferenceTo(element: PsiElement): Boolean {
        val cssId = this.element.text.substring(this.rangeInElement)
        if (element is WXSSIdSelector && element.id == cssId) {
            val wxmlFile = this.element.containingFile.virtualFile
            val wxssFile = findRelateFile(wxmlFile, RelateFileType.WXSS)
            if (this.containsSelector(element, wxssFile)) {
                return true
            }
        }
        return false
    }

    private fun containsSelector(selector: WXSSIdSelector, wxssFile: VirtualFile?): Boolean {
        val psiManager = PsiManager.getInstance(selector.project)
        val wxssPsiFile = wxssFile?.let { psiManager.findFile(wxssFile) }
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile as WXSSPsiFile)
        return wxssPsiFiles.any {
            PsiTreeUtil.findChildrenOfType(it, WXSSIdSelector::class.java).contains(selector)
        }
    }

}