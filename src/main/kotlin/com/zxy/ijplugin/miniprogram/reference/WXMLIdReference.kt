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
import com.intellij.psi.css.CssIdSelector
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.utils.WXSSModuleUtils

class WXMLIdReference(xmlAttributeValue: XmlAttributeValue, range: TextRange) :
    PsiPolyVariantReferenceBase<XmlAttributeValue>(xmlAttributeValue, range) {

    override fun multiResolve(p0: Boolean): Array<ResolveResult> {
        val id = this.value
        return getIdSelectorsFromRelatedWxssFile().filter { id == it.name }.map {
            PsiElementResolveResult(it)
        }.toTypedArray()
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        val cssId = this.value
        if (element is CssIdSelector && element.name == cssId) {
            val wxmlFile = this.element.containingFile
            val wxssFile = RelateFileHolder.STYLE.findFile(wxmlFile)
            if (this.containsSelector(element, wxssFile)) {
                return true
            }
        }
        return false
    }

    private fun containsSelector(selector: CssIdSelector, wxssFile: PsiFile?): Boolean {
        val wxssPsiFiles = WXSSModuleUtils.findImportedFilesWithSelf(wxssFile as? WXSSPsiFile ?: return false)
        return wxssPsiFiles.any {
            PsiTreeUtil.findChildrenOfType(it, CssIdSelector::class.java).contains(selector)
        }
    }

    private fun getIdSelectorsFromRelatedWxssFile(): MutableCollection<CssIdSelector> {
        val wxmlFile = this.element.containingFile.originalFile
        // 在wxml文件附近找同名的wxss文件
        val wxssPsiFile = RelateFileHolder.STYLE.findFile(wxmlFile) ?: return mutableListOf()
        return PsiTreeUtil.findChildrenOfType(wxssPsiFile, CssIdSelector::class.java)
    }

    override fun getVariants(): Array<Any> {
        return getIdSelectorsFromRelatedWxssFile().toTypedArray()
    }

    override fun isSoft(): Boolean {
        return true
    }

}