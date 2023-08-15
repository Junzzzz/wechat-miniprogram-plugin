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

package com.zxy.ijplugin.miniprogram.lang.wxml.attributes

import com.intellij.application.options.editor.WebEditorOptions
import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.editorActions.TabOutScopesTracker
import com.intellij.codeInsight.editorActions.XmlEditUtil
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.text.CharArrayUtil
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLElementAttributeDescription
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.isJsTypeAttribute

abstract class WXMLAttributeNameInsertHandler : InsertHandler<LookupElement> {
    companion object {

        private fun isOnlyNameForInsert(
            wxmlPresetElementAttributeDescription: WXMLElementAttributeDescription
        ): Boolean {
            return wxmlPresetElementAttributeDescription.types.contains(
                WXMLElementAttributeDescription.ValueType.BOOLEAN
            ) && wxmlPresetElementAttributeDescription.default == false
        }

        fun createFromAttributeDescription(
            attributeDescription: WXMLElementAttributeDescription
        ): WXMLAttributeNameInsertHandler? {
            return if (attributeDescription.isJsTypeAttribute()) {
                DoubleBraceInsertHandler()
            } else if (!isOnlyNameForInsert(attributeDescription)) {
                DoubleQuotaInsertHandler()
            } else {
                null
            }
        }

    }

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        TabOutScopesTracker.getInstance().registerEmptyScopeAtCaret(context.editor)
        editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
        editor.selectionModel.removeSelection()
        AutoPopupController.getInstance(editor.project!!).scheduleAutoPopup(editor)
    }

    /**
     * 在插入属性名称时
     * 额外插入双括号
     */
    class DoubleBraceInsertHandler :
        WXMLAttributeNameInsertHandler() {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            // 额外插入 [=""]
            // 额外插入 [="{{}}"]
            val editor = context.editor
            val offset = editor.caretModel.offset
            context.document.insertString(offset, "=\"{{}}\"")
            editor.caretModel.moveToOffset(offset + 4)
            super.handleInsert(context, item)
        }

    }

    /**
     * 在插入属性名称之前
     * 额外插入双引号
     * @param autoPopup 完成之后是否立即唤醒自动完成控制器
     */
    class DoubleQuotaInsertHandler(private val autoPopup: Boolean = false) :
        WXMLAttributeNameInsertHandler() {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = editor.caretModel.offset
            val file = context.file
            val chars = document.charsSequence
            val quote = XmlEditUtil.getAttributeQuote(file)
            val insertQuotes = WebEditorOptions.getInstance().isInsertQuotesForAttributeValue && StringUtil.isNotEmpty(
                quote
            )
            val hasQuotes = CharArrayUtil.regionMatches(chars, caretOffset, "=\"") ||
                    CharArrayUtil.regionMatches(chars, caretOffset, "='")
            if (!hasQuotes) {
                if (CharArrayUtil.regionMatches(chars, caretOffset, "=")) {
                    document.deleteString(caretOffset, caretOffset + 1)
                }
                val fileContext = file.context
                var toInsert: String? = null
                if (fileContext != null) {
                    if (fileContext.text.startsWith("\"")) toInsert = "=''"
                    if (fileContext.text.startsWith("\'")) toInsert = "=\"\""
                }
                if (toInsert == null) {
                    toInsert = "=$quote$quote"
                }
                if (!insertQuotes) toInsert = "="
                if (caretOffset < document.textLength && "/> \n\t\r".indexOf(
                        document.charsSequence[caretOffset]
                    ) < 0
                ) {
                    document.insertString(caretOffset, "$toInsert ")
                } else {
                    document.insertString(caretOffset, toInsert)
                }
                if ('=' == context.completionChar) {
                    context.setAddCompletionChar(false) // IDEA-19449
                }
            }
            editor.caretModel.moveToOffset(caretOffset + if (insertQuotes || hasQuotes) 2 else 1)
            super.handleInsert(context, item)
        }

    }
}
