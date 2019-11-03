package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.css.impl.util.table.CssDescriptorsUtil
import com.intellij.psi.css.impl.util.table.CssElementDescriptorFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSStyleStatement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes


class WXSSPropertyValueCompletionContributor : CompletionContributor() {

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
                        val position = parameters.position
                        val styleStatement = PsiTreeUtil.getParentOfType(position, WXSSStyleStatement::class.java)
                        val propertyName = styleStatement?.firstChild?.text ?: return
                        CssElementDescriptorFactory.getDescriptor(propertyName)?.let {
                            it.allVariants.filter { variant -> variant !== null && (variant !is String || variant.isNotEmpty()) }
                                    .forEach { variant ->
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