package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.css.impl.util.table.CssElementDescriptorFactory
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes


class WXSSPropertyValueCompletionContributor : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.ATTRIBUTE_NAME)
                        .withLanguage(WXSSLanguage.INSTANCE),
                object : CompletionProvider<CompletionParameters>() {
                    public override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        resultSet.addElement(
                                LookupElementBuilder.create("Hello").withInsertHandler { insertionContext, lookupElement ->
                                    val editor = insertionContext.editor
                                    val offset = editor.caretModel.offset
                                    insertionContext.document.insertString(offset, ": ;")
                                    editor.caretModel.moveToOffset(offset + 2)
                                    AutoPopupController.getInstance(insertionContext.project)
                                            .autoPopupMemberLookup(insertionContext.editor, null)
                                }
                        )
                    }
                }
        )

        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.ATTRIBUTE_VALUE_LITERAL)
                        .withLanguage(WXSSLanguage.INSTANCE),
                object : CompletionProvider<CompletionParameters>() {
                    public override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        CssElementDescriptorFactory.getDescriptor("text-align")?.let {
                            it.allVariants.forEach { variant ->
                                resultSet.addElement(
                                        LookupElementBuilder.create(
                                                variant
                                        )
                                )
                            }
                        }
                    }
                }
        )
    }
}