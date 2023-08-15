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

package com.zxy.ijplugin.miniprogram.inspections

import com.intellij.codeInspection.InspectionProfileEntry
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.lang.javascript.highlighting.IntentionAndInspectionFilter
import com.intellij.lang.javascript.inspections.JSUnresolvedVariableInspection
import com.intellij.lang.javascript.intentions.JSSplitDeclarationIntention
import com.intellij.lang.javascript.psi.JSEmbeddedContent
import com.intellij.psi.PsiElement
import com.sixrr.inspectjs.confusing.CommaExpressionJSInspection
import com.sixrr.inspectjs.control.UnnecessaryLabelJSInspection
import com.sixrr.inspectjs.validity.BadExpressionStatementJSInspection
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.utils.getParentOfType

class WxmlJsInspectionFilter : IntentionAndInspectionFilter(), InspectionSuppressor {

    override fun isSupportedInspection(inspectionToolId: String?): Boolean = !suppressedToolIds.contains(
        inspectionToolId
    )

    companion object {
        val suppressedToolIds = listOf(
            JSUnresolvedVariableInspection::class,
            BadExpressionStatementJSInspection::class,
            JSSplitDeclarationIntention::class,
            CommaExpressionJSInspection::class,
            UnnecessaryLabelJSInspection::class
        ).map { InspectionProfileEntry.getShortName(it.java.simpleName) }.toSet()
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> {
        return emptyArray()
    }

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        return element.getParentOfType<JSEmbeddedContent>()?.parent?.language == WXMLLanguage.INSTANCE && suppressedToolIds.contains(
            toolId
        )
    }
}