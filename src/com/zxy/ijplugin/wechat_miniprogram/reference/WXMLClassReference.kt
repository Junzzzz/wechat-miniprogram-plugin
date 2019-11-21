package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findAppFile
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSSelectors
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.substring

/**
 * WXML class 属性值的引用
 * @param textRange 此class在字符串中的位置
 */
class WXMLClassReference(psiElement: PsiElement, textRange: TextRange) :
        PsiPolyVariantReferenceBase<PsiElement>(psiElement, textRange) {
    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        val results = arrayListOf<ResolveResult>()
        val cssClass = this.element.text.substring(this.rangeInElement)
        val project = this.element.project
        val wxmlFile = this.element.containingFile.virtualFile
        val wxssFile = findRelateFile(wxmlFile, RelateFileType.WXSS)
        // 相关的wxss文件中找class的定义
        results.addAll(findClassSelectorResult(wxssFile, cssClass, project))

        val appWXSSFile = findAppFile(project, RelateFileType.WXSS)
        // 在app.wxss文件中找class的定义
        results.addAll(findClassSelectorResult(appWXSSFile, cssClass, project))
        return results.toTypedArray()
    }

    private fun findClassSelectorResult(
            wxssFile: VirtualFile?, cssClass: String, project: Project
    ): List<PsiElementResolveResult> {
        val selectors = getSelectors(project, wxssFile)
        return selectors.asSequence().mapNotNull {
            // 伪元素的选择器的第一个子元素也是基本选择器
            it.lastChild?.firstChild
        }.filter {
            it is WXSSClassSelector && it.className == cssClass
        }.map {
            PsiElementResolveResult(it)
        }.toMutableList()
    }

    private fun getSelectors(
            project: Project, wxssFile: VirtualFile?
    ): List<WXSSSelectors> {
        val psiManager = PsiManager.getInstance(project)
        val wxssPsiFile = wxssFile?.let { psiManager.findFile(wxssFile) }
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile as WXSSPsiFile)

        return wxssPsiFiles.flatMap {
            PsiTreeUtil.findChildrenOfType(it, WXSSSelectors::class.java)
        }
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        val cssClass = this.element.text.substring(this.rangeInElement)
        if (element is WXSSClassSelector && element.className == cssClass) {
            val project = this.element.project
            val wxmlFile = this.element.containingFile.virtualFile
            val wxssFile = findRelateFile(wxmlFile, RelateFileType.WXSS)
            if (this.containsSelector(element, wxssFile)) {
                return true
            }
            val appWXSSFile = findAppFile(project, RelateFileType.WXSS)
            if (this.containsSelector(element, appWXSSFile)) {
                return true
            }
        }
        return false
    }

    private fun containsSelector(selector: WXSSClassSelector, wxssFile: VirtualFile?): Boolean {
        val psiManager = PsiManager.getInstance(selector.project)
        val wxssPsiFile = wxssFile?.let { psiManager.findFile(wxssFile) }
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile as WXSSPsiFile)
        return wxssPsiFiles.any {
            PsiTreeUtil.findChildrenOfType(it, WXSSClassSelector::class.java).contains(selector)
        }
    }

}