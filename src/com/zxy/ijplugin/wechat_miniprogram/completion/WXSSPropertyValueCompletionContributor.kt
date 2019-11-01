package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes


class WXSSPropertyValueCompletionContributor : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXSSTypes.ATTRIBUTE_VALUE)
                        .withLanguage(WXSSLanguage.INSTANCE),
                object : CompletionProvider<CompletionParameters>() {
                    public override fun addCompletions(
                            parameters: CompletionParameters,
                            context: ProcessingContext,
                            resultSet: CompletionResultSet
                    ) {
                        resultSet.addElement(LookupElementBuilder.create("Hello"))
                    }
                }
        )
    }
}