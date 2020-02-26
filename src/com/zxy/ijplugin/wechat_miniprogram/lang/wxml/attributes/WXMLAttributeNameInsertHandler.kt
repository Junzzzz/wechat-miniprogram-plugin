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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.attributes

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
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLElementAttributeDescription
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.isJsTypeAttribute

class WXMLAttributeNameInsertHandler(
        private val attributeDescription: WXMLElementAttributeDescription
) : InsertHandler<LookupElement> {
    companion object {

        fun isOnlyNameForInsert(
                wxmlPresetElementAttributeDescription: WXMLElementAttributeDescription
        ): Boolean {
            return wxmlPresetElementAttributeDescription.types.contains(
                    WXMLElementAttributeDescription.ValueType.BOOLEAN
            ) && wxmlPresetElementAttributeDescription.default == false
        }
    }

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        if (attributeDescription.isJsTypeAttribute()) {
            DoubleBraceInsertHandler().handleInsert(context, item)
        } else if (!isOnlyNameForInsert(attributeDescription)) {
            DoubleQuotaInsertHandler().handleInsert(context, item)
        }

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
            InsertHandler<LookupElement> {
        override fun handleInsert(insertionContext: InsertionContext, p1: LookupElement) {
            // 额外插入 [=""]
            // 额外插入 [="{{}}"]
            val editor = insertionContext.editor
            val offset = editor.caretModel.offset
            insertionContext.document.insertString(offset, "=\"{{}}\"")
            editor.caretModel.moveToOffset(offset + 4)
        }

    }

    /**
     * 在插入属性名称之前
     * 额外插入双引号
     * @param autoPopup 完成之后是否立即唤醒自动完成控制器
     */
    class DoubleQuotaInsertHandler(private val autoPopup: Boolean = false) :
            InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, p1: LookupElement) {
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
                        ) < 0) {
                    document.insertString(caretOffset, "$toInsert ")
                } else {
                    document.insertString(caretOffset, toInsert)
                }
                if ('=' == context.completionChar) {
                    context.setAddCompletionChar(false) // IDEA-19449
                }
            }
            editor.caretModel.moveToOffset(caretOffset + if (insertQuotes || hasQuotes) 2 else 1)
        }

    }
}
