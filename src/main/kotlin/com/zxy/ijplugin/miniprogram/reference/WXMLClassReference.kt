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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.css.CssClass
import com.intellij.psi.css.CssSelector
import com.intellij.psi.css.CssSimpleSelector
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.miniprogram.utils.findChildOfType
import com.zxy.ijplugin.miniprogram.utils.substring

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
        val wxmlFile = this.element.containingFile
        val wxssFile = RelateFileHolder.STYLE.findFile(wxmlFile) as? WXSSPsiFile
        // 相关的wxss文件中找class的定义
        results.addAll(findClassSelectorResult(wxssFile, cssClass))

        val appWXSSFile = RelateFileHolder.STYLE.findAppFile(project)
        // 在app.wxss文件中找class的定义
        results.addAll(findClassSelectorResult(appWXSSFile as WXSSPsiFile?, cssClass))
        return results.toTypedArray()
    }

    private fun findClassSelectorResult(
        wxssFile: WXSSPsiFile?, cssClass: String
    ): List<PsiElementResolveResult> {
        val selectors = getSelectors(wxssFile)
        return selectors.asSequence().mapNotNull {
            // 伪元素的选择器的第一个子元素也是基本选择器
            val lastChild = it.lastChild as? CssSimpleSelector ?: return@mapNotNull null
            lastChild.findChildOfType<CssClass>()
        }.filter {
            it.name == cssClass
        }.map {
            PsiElementResolveResult(it)
        }.toMutableList()
    }

    private fun getSelectors(
        wxssFile: WXSSPsiFile?
    ): List<CssSelector> {
        val wxssPsiFile = wxssFile ?: return emptyList()
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile)

        return wxssPsiFiles.flatMap {
            PsiTreeUtil.findChildrenOfType(it, CssSelector::class.java)
        }
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        val cssClass = this.element.text.substring(this.rangeInElement)
        if (element is CssClass && element.name == cssClass) {
            val project = this.element.project
            val wxmlFile = this.element.containingFile
            val wxssFile = RelateFileHolder.STYLE.findFile(wxmlFile)
            if (this.containsSelector(element, wxssFile)) {
                return true
            }
            val appWXSSFile = RelateFileHolder.STYLE.findAppFile(project)
            if (this.containsSelector(element, appWXSSFile)) {
                return true
            }
        }
        return false
    }

    private fun containsSelector(selector: CssClass, wxssFile: PsiFile?): Boolean {
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssFile as? WXSSPsiFile ?: return false)
        return wxssPsiFiles.any {
            PsiTreeUtil.findChildrenOfType(it, CssClass::class.java).contains(selector)
        }
    }

    override fun getVariants(): Array<Any> {
        val result = arrayListOf<CssClass>()
        val wxssPsiFile = RelateFileHolder.STYLE.findFile(this.element.containingFile.originalFile)
        if (wxssPsiFile is WXSSPsiFile) {
            result.addAll(findClassSelectorFromFileAndImports(wxssPsiFile))
        }
        RelateFileHolder.STYLE.findAppFile(this.element.project)?.let {
            if (it is WXSSPsiFile) {
                result.addAll(findClassSelectorFromFileAndImports(it))
            }
        }
        // 获取已经存在的class
        val existedClassNames = this.element.references.filterIsInstance<WXMLClassReference>().map { it.value }
        return result.filter { !existedClassNames.contains(it.name) }.distinctBy { it.name }.toTypedArray()
    }

    private fun findClassSelectorFromFileAndImports(wxssPsiFile: WXSSPsiFile): List<CssClass> {
        return WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile).flatMap {
            findClassSelectorFromFile(it)
        }.distinct()
    }

    private fun findClassSelectorFromFile(wxssPsiFile: WXSSPsiFile): MutableCollection<CssClass> {
        return PsiTreeUtil.findChildrenOfType(wxssPsiFile, CssClass::class.java)
    }

    override fun isSoft(): Boolean {
        return true
    }

}