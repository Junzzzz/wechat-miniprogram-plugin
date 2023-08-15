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

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.css.CssImport
import com.intellij.psi.css.CssString
import com.intellij.psi.css.CssTerm
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.util.CssUtil
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class WXSSReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        // 解析@import字符串中的路径
        psiReferenceRegistrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CssString::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    psiElement: PsiElement, p1: ProcessingContext
                ): Array<out PsiReference> {
                    if (PsiTreeUtil.getParentOfType(psiElement, CssImport::class.java) != null) {
                        return FileReferenceSet(psiElement).allReferences
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )

        // 解析wxss animation-name的值
        psiReferenceRegistrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CssElementTypes.CSS_IDENT),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement, context: ProcessingContext
                ): Array<PsiReference> {
                    val parent = element.parent
                    if (parent is CssTerm) {
                        val declaration = CssUtil.getDeclaration(parent)
                        if (declaration != null) {
                            val propertyName = declaration.propertyName
                            if ("animation-name".equals(propertyName, ignoreCase = true) || "animation".equals(
                                    propertyName, ignoreCase = true
                                )
                            ) {
                                return arrayOf(WXSSKeyframesReference(element))
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }

            })
    }

}