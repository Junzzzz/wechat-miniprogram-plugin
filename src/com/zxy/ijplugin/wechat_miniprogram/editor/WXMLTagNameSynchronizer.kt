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

package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Couple
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.pom.core.impl.PomModelImpl
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiDocumentManagerBase
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findNextSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findPrevSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLUtils

/**
 * 用于同步WXML标签名称
 * @see XmlTagNameSynchronizer
 */
class WXMLTagNameSynchronizer : CommandListener, EditorFactoryListener {

    init {
        ApplicationManager.getApplication().messageBus.connect().subscribe(CommandListener.TOPIC, this)
    }

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val project = editor.project
        if (project != null && editor is EditorImpl) {
            val document = editor.document
            val file = FileDocumentManager.getInstance().getFile(document)
            val psiFile = if (file != null && file.isValid) PsiManager.getInstance(project).findFile(
                    file
            ) else null
            if (psiFile != null && psiFile.language == WXMLLanguage.INSTANCE) {
                WXMLTagNameSyncDocumentListener(editor, project)
            }
        }
    }

    override fun beforeCommandFinished(event: CommandEvent) {
        val document = event.document ?: return
        val editors = EditorFactory.getInstance().getEditors(document)
        editors.mapNotNull { it.getUserData(WXMLTagNameSyncDocumentListener.KEY) }.forEach {
            it.beforeCommandFinished()
        }
    }

}

class WXMLTagNameSyncDocumentListener(private val editor: EditorImpl, private val project: Project) : DocumentListener {

    companion object {
        val KEY = Key.create<WXMLTagNameSyncDocumentListener>("wxml.key.WXMLTagNameSyncDocumentListener")
        val MARKERS_KEY = Key.create<Couple<RangeMarker>>("wxml.key.WXMLTagNameSyncDocumentListener.markers")
    }

    private val documentManager = PsiDocumentManager.getInstance(project) as PsiDocumentManagerBase

    init {
        editor.document.addDocumentListener(this, editor.disposable)
        editor.putUserData(KEY, this)
    }

    override fun beforeDocumentChange(event: DocumentEvent) {
        val document = event.document
        if (!project.isDefault && !UndoManager.getInstance(
                        project
                ).isUndoInProgress && PomModelImpl.isAllowPsiModification() && !document.isInBulkUpdate) {

            val psiFile = documentManager.getPsiFile(editor.document) ?: return
            if (psiFile.language != WXMLLanguage.INSTANCE) {
                return
            }
            val oldLength = event.oldLength
            val newLength = event.newLength
            // 如果是新增字符则
            // 取前一个字符
            val offset = if (oldLength == 0 && newLength >= 1) event.offset - 1 else event.offset
            val fragment = event.newFragment
            val element = psiFile.findElementAt(offset) ?: return
            val caret = editor.caretModel.currentCaret
            // 被修改的是TAG_NAME
            if (element.node.elementType !== WXMLTypes.TAG_NAME) {
                return
            }
            // 是正确的标签名称
            if (!WXMLUtils.isValidTagName(fragment)) {
                return
            }
            if (documentManager.isUncommited(document)) {
                documentManager.commitDocument(document)
            }
            val leader = document.createRangeMarker(element.textRange)
            leader.isGreedyToLeft = true
            leader.isGreedyToRight = true
            val another = createAnotherRangeMarker(element, document) ?: return
            another.isGreedyToRight = true
            another.isGreedyToLeft = true
            val markers = Couple.of(leader, another)
            caret.putUserData(MARKERS_KEY, markers)
        }

    }

    private fun createAnotherRangeMarker(element: PsiElement, document: Document): RangeMarker? {
        val endTagNameNode = when (val parent = element.parent) {
            is WXMLStartTag -> {
                // 修改了开始标签
                val endTag = parent.findNextSibling { it is WXMLEndTag } ?: return null
                endTag.node.findChildByType(WXMLTypes.TAG_NAME)
            }
            is WXMLEndTag -> {
                // 修改了结束标签
                val startTag = parent.findPrevSibling { it is WXMLStartTag } ?: return null
                startTag.node.findChildByType(WXMLTypes.TAG_NAME)
            }
            else -> null
        }

        if (endTagNameNode == null || endTagNameNode.text !== element.text) {
            // 找不到结束标签
            // 或者结束标签的标签名与开始标签不一致
            return null
        }
        val textRange = endTagNameNode.textRange
        return document.createRangeMarker(textRange)
    }

    internal fun beforeCommandFinished() {
        val caret = this.editor.caretModel.currentCaret
        val markers = caret.getUserData(MARKERS_KEY) ?: return
        val leader = markers.first
        if (!leader.isValid) return
        val another = markers.second
        if (!another.isValid) return
        val document = editor.document
        if (document.textLength >= leader.endOffset) {
            val tagName = document.getText(TextRange(leader.startOffset, leader.endOffset))
            if (document.textLength >= another.endOffset && tagName != document.getText(
                            TextRange(another.startOffset, another.endOffset)
                    )) {
                ApplicationManager.getApplication().runWriteAction {
                    document.replaceString(another.startOffset, another.endOffset, tagName)
                }
            }
        }
        caret.putUserData(MARKERS_KEY, null)
    }
}