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