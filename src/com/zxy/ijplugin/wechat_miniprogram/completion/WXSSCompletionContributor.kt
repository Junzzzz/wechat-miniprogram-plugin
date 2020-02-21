/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiManager
import com.intellij.psi.css.impl.util.table.CssDescriptorsUtil
import com.intellij.psi.css.impl.util.table.CssElementDescriptorFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import com.intellij.xml.util.ColorSampleLookupValue
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage.UNITS
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.*
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.isAnimationNameStyleStatement
import com.zxy.ijplugin.wechat_miniprogram.reference.WXMLClassReference
import com.zxy.ijplugin.wechat_miniprogram.utils.findChildrenOfType
import icons.WechatMiniProgramIcons


class WXSSCompletionContributor : CompletionContributor() {

    companion object {
        private val WXSS_PROPERTY_NAMES by lazy {
            CssDescriptorsUtil.getAllPropertyDescriptors(null).asSequence().map { it.id }.distinct()
                    .filter { !it.startsWith("-") }
                    .filter { !it.startsWith("mso") }
                    .filter { !it.contains("_") }
                    .toList()
        }
    }

    init {
        // wxss的属性名
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER)
                        .withLanguage(WXSSLanguage.INSTANCE),
                object : CompletionProvider<CompletionParameters>() {
                    public override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        if (parameters.position.parent is WXSSStyleStatement) {
                            resultSet.addAllElements(
                                    WXSS_PROPERTY_NAMES.map {
                                        LookupElementBuilder.create(it).withInsertHandler { insertionContext, _ ->
                                            val editor = insertionContext.editor
                                            val offset = editor.caretModel.offset
                                            insertionContext.document.insertString(offset, ": ;")
                                            editor.caretModel.moveToOffset(offset + 2)
                                            AutoPopupController.getInstance(insertionContext.project)
                                                    .autoPopupMemberLookup(insertionContext.editor, null)
                                        }
                                    }
                            )
                        }
                    }
                }
        )

        // 对css的属性值进行自动完成
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER)
                        .withLanguage(WXSSLanguage.INSTANCE),
                object : CompletionProvider<CompletionParameters>() {
                    public override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        val position = parameters.position
                        if (position.parent is WXSSValue) {
                            val styleStatement = PsiTreeUtil.getParentOfType(position, WXSSStyleStatement::class.java)
                            val propertyName = styleStatement?.firstChild?.text ?: return
                            CssElementDescriptorFactory.getDescriptor(propertyName)?.let {
                                it.allVariants.filter { variant -> variant !== null && (variant !is String || variant.isNotEmpty()) }
                                        .filter { variant ->
                                            // 过滤掉带浏览器前缀的
                                            variant !is String || !variant.startsWith("-")
                                        }
                                        .forEach { variant ->
                                            if (variant is LookupElement) {
                                                resultSet.addElement(variant)
                                            } else if (variant is ColorSampleLookupValue) {
                                                if (variant.value.startsWith("#")) {
                                                    resultSet.addElement(LookupElementBuilder.create(variant.name))
                                                }
                                            } else {
                                                resultSet.addElement(
                                                        LookupElementBuilder.create(
                                                                variant
                                                        )
                                                )
                                            }
                                        }
                            }
                        }
                    }
                }
        )

        // 自动完成数字单位
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER), object :
                CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                    completionParameters: CompletionParameters, processingContext: ProcessingContext,
                    completionResultSet: CompletionResultSet
            ) {
                val preElement = completionParameters.position.prevSibling
                if (preElement?.node?.elementType == WXSSTypes.NUMBER) {
                    completionResultSet.addAllElements(UNITS.map {
                        LookupElementBuilder.create(it)
                    })
                }
            }
        })

        // 自动完成At关键字
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(WXSSAttachElementType.AT_KEYWORD), object :
                CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                    completionParameters: CompletionParameters, processingContext: ProcessingContext,
                    completionResultSet: CompletionResultSet
            ) {
                completionResultSet.addAllElements(
                        listOf("import", "keyframes", "font-face").map(LookupElementBuilder::create)
                )
            }
        })

        // wxss 动画名称属性值
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        val wxssStyleStatement = psiElement.parentOfType<WXSSStyleStatement>() ?: return
                        if (wxssStyleStatement.isAnimationNameStyleStatement()) {
                            val wxssFiles = WXSSModuleUtils.findImportedFilesWithSelf(
                                    psiElement.containingFile as? WXSSPsiFile ?: return
                            )
                            // 寻找可用的keyframes定义
                            result.addAllElements(wxssFiles.flatMap { wxssPsiFile ->
                                wxssPsiFile.findChildrenOfType<WXSSKeyframesDefinition>().mapNotNull {
                                    it.name
                                }
                            }.map { LookupElementBuilder.create(it) })
                        }
                    }
                }
        )

        // wxss 类名
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER),
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        if (psiElement.parent !is WXSSClassSelector) return
                        val wxssPsiFile = (psiElement.containingFile as? WXSSPsiFile) ?: return

                        // 收集wxss文件中的所有可见的类名
                        result.addAllElements(
                                WXSSModuleUtils.findImportedFilesWithSelf(wxssPsiFile).asSequence().flatMap {
                                    it.findChildrenOfType<WXSSClassSelector>().asSequence()
                                }.mapNotNull {
                                    it.className
                                }.distinct().map {
                                    LookupElementBuilder.create(it).withPresentableText(".$it").withIcon(AllIcons.Xml.Css_class)
                                }.toMutableList()
                        )

                        // 收集wxml文件中的所有可见的类名
                        val wxmlVirtualFile = findRelateFile(wxssPsiFile.originalFile.virtualFile ?: return, RelateFileType.WXML)
                                ?: return
                        val wxmlPsiFile = PsiManager.getInstance(psiElement.project).findFile(
                                wxmlVirtualFile
                        ) as? WXMLPsiFile ?: return

                        result.addAllElements(
                                wxmlPsiFile.findChildrenOfType<WXMLStringText>().asSequence().flatMap { it.references.asSequence() }.filterIsInstance<WXMLClassReference>().map {
                                    it.rangeInElement.substring(it.element.text)
                                }.distinct().map {
                                    LookupElementBuilder.create(it).withPresentableText(".$it").withIcon(AllIcons.Xml.Css_class)
                                }.toMutableList()
                        )
                    }

                })

        // id
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER),
                object :CompletionProvider<CompletionParameters>(){
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        if (psiElement.parent !is WXSSIdSelector) return

                        val wxssPsiFile = (psiElement.containingFile as? WXSSPsiFile) ?: return

                        // 收集wxml文件中的所有可见的Id
                        val wxmlVirtualFile = findRelateFile(wxssPsiFile.originalFile.virtualFile ?: return, RelateFileType.WXML)
                                ?: return
                        val wxmlPsiFile = PsiManager.getInstance(psiElement.project).findFile(
                                wxmlVirtualFile
                        ) as? WXMLPsiFile ?: return
                        result.addAllElements(wxmlPsiFile.findChildrenOfType<WXMLStringText>().asSequence().flatMap {
                            it.references.asSequence()
                        }.map {
                            it.rangeInElement.substring(it.element.text)
                        }.distinct().map {
                            LookupElementBuilder.create(it).withPresentableText("#$it").withIcon(AllIcons.Xml.Html_id)
                        }.toMutableList())
                    }

                }
        )

        // wxml tag
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.IDENTIFIER),
                object :CompletionProvider<CompletionParameters>(){
                    override fun addCompletions(
                            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                    ) {
                        val psiElement = parameters.position
                        if (psiElement.parent !is WXSSSelector) return

                        // page 为隐藏的顶层元素
                        result.addElement(LookupElementBuilder.create("page").withIcon(WechatMiniProgramIcons.PAGE))

                        val wxssPsiFile = (psiElement.containingFile as? WXSSPsiFile) ?: return
                        // 收集wxml文件中的所有可见的Id
                        val wxmlVirtualFile = findRelateFile(wxssPsiFile.originalFile.virtualFile ?: return, RelateFileType.WXML)
                                ?: return
                        val wxmlPsiFile = PsiManager.getInstance(psiElement.project).findFile(
                                wxmlVirtualFile
                        ) as? WXMLPsiFile ?: return

                        result.addAllElements(wxmlPsiFile.findChildrenOfType<WXMLElement>().mapNotNull {
                            it.tagName
                        }.distinct().map {
                            LookupElementBuilder.create(it).withIcon(AllIcons.Xml.Html5)
                        })
                    }
                }
        )

    }
}