package com.zxy.ijplugin.wechat_miniprogram.reference.usage

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.search.PsiSearchHelperImpl
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.SingleTargetRequestResultProcessor
import com.intellij.psi.search.TextOccurenceProcessor
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector
import java.util.*

class WXSSElementSearcher :
        QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {
    override fun processQuery(p: ReferencesSearch.SearchParameters, consumer: Processor<in PsiReference>) {
        val elementToSearch = p.elementToSearch
        val options: EnumSet<*>
        val textToSearch: String?
        if (elementToSearch is WXSSClassSelector || elementToSearch is WXSSIdSelector) {
            elementToSearch as PsiNamedElement
            textToSearch = elementToSearch.name ?: return
            options = EnumSet.of(PsiSearchHelperImpl.Options.PROCESS_INJECTED_PSI)
            this.doProcess(p, consumer, options, textToSearch)
        }
    }

    private fun doProcess(
            p: ReferencesSearch.SearchParameters, consumer: Processor<in PsiReference>,
            options: EnumSet<PsiSearchHelperImpl.Options>, textToSearch: String
    ) {
        val scope = p.effectiveSearchScope
        val singleTargetRequestResultProcessor = SingleTargetRequestResultProcessor(
                p.elementToSearch
        )
        val processor = TextOccurenceProcessor { element: PsiElement?, offsetInElement: Int ->
            singleTargetRequestResultProcessor.processTextOccurrence(element!!, offsetInElement, consumer)
        }
        val helper = PsiSearchHelper.getInstance(p.project)
        if (helper is PsiSearchHelperImpl) {
            helper.processElementsWithWord(
                    processor, scope, textToSearch, 255.toShort(), options, null as String?
            )
        } else {
            helper.processElementsWithWord(processor, scope, textToSearch, 255.toShort(), true)
        }
    }
}