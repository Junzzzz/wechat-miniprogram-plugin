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

package com.zxy.ijplugin.miniprogram.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.css.CssClass
import com.intellij.psi.css.CssDeclaration
import com.intellij.psi.css.CssIdSelector
import com.intellij.psi.css.CssTerm
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.CssTermImpl
import com.intellij.psi.css.impl.CssTermTypes
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.miniprogram.reference.WXMLClassReference
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType


class WXSSCompletionContributor : CompletionContributor() {

    init {
        // wxss 类名
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(CssElementTypes.CSS_IDENT)
                        .inWXSSFile()
                        .withParent(CssClass::class.java),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        val wxssPsiFile = (psiElement.containingFile as? WXSSPsiFile) ?: return

                        // 收集wxss文件中的所有可见的类名
                        result.addAllElements(
                                WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile).asSequence().flatMap {
                                    it.findChildrenOfType<CssClass>().asSequence()
                                }.mapNotNull {
                                    it.name
                                }.distinct().map {
                                    LookupElementBuilder.create(it).withPresentableText(it)
                                            .withIcon(AllIcons.Xml.Css_class)
                                }.toMutableList()
                        )

                        // 收集wxml文件中的所有可见的类名
                        wxssPsiFile.originalFile.virtualFile ?: return
                        val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(wxssPsiFile.originalFile)
                                as? WXMLPsiFile ?: return

                        result.addAllElements(
                                wxmlPsiFile.findChildrenOfType<XmlAttributeValue>().asSequence()
                                        .flatMap { it.references.asSequence() }.filterIsInstance<WXMLClassReference>()
                                        .map {
                                            it.rangeInElement.substring(it.element.text)
                                        }.distinct().map {
                                            LookupElementBuilder.create(it).withPresentableText(it)
                                                    .withIcon(AllIcons.Xml.Css_class)
                                        }.toMutableList()
                        )
                    }

                })

        // rpx
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(CssElementTypes.CSS_IDENT).afterLeafSkipping(
                        StandardPatterns.alwaysFalse<Any>(),
                        PlatformPatterns.psiElement(CssElementTypes.CSS_NUMBER)
                ).withSuperParent(2, CssTerm::class.java).inside(
                        CssDeclaration::class.java
                ).andOr(
                        PlatformPatterns.psiElement().inWXSSFile(),
                        PlatformPatterns.psiElement().inFile(PlatformPatterns.psiFile(WXMLPsiFile::class.java))
                ),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val element = parameters.position
                        val termImpl = PsiTreeUtil.getParentOfType(
                                element,
                                CssTermImpl::class.java
                        ) ?: return
                        val termType = termImpl.termType
                        if (termType == CssTermTypes.NUMBER_WITH_UNKNOWN_UNIT) {
                            result.addElement(LookupElementBuilder.create("rpx"))
                        }
                    }
                }
        )

        // id
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(CssElementTypes.CSS_HASH)
                        .inWXSSFile()
                        .withParent(CssIdSelector::class.java),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        val wxssPsiFile = (psiElement.containingFile as? WXSSPsiFile) ?: return

                        // 收集wxml文件中的所有可见的Id
                        wxssPsiFile.originalFile.virtualFile ?: return
                        val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(wxssPsiFile.originalFile)
                                as? WXMLPsiFile ?: return
                        result.addAllElements(wxmlPsiFile.findChildrenOfType<XmlAttributeValue>().asSequence().flatMap {
                            it.references.asSequence()
                        }.map {
                            it.rangeInElement.substring(it.element.text)
                        }.distinct().map {
                            LookupElementBuilder.create(it).withPresentableText("#$it").withIcon(AllIcons.Xml.Html_id)
                        }.toMutableList())
                    }

                }
        )

    }
}

fun PsiElementPattern.Capture<out PsiElement>.inWXSSFile(): PsiElementPattern.Capture<out PsiElement> {
    return this.inFile(PlatformPatterns.psiFile(WXSSPsiFile::class.java))
}