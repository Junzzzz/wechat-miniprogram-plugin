package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandEvent
import com.intellij.openapi.command.CommandListener
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findNextSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLTagNameSynchronizer(editorFactory: EditorFactory, fileDocumentManager: FileDocumentManager) : CommandListener {

    init {
        editorFactory.addEditorFactoryListener(object : EditorFactoryListener {
            override fun editorCreated(event: EditorFactoryEvent) {
                val editor = event.editor
                val project = editor.project
                if (project != null && editor is EditorImpl) {
                    val document = editor.document
                    val file = fileDocumentManager.getFile(document)
                    val psiFile = if (file != null && file.isValid) PsiManager.getInstance(project).findFile(
                            file
                    ) else null
                    if (psiFile != null && psiFile.language == WXMLLanguage.INSTANCE) {
                        WXMLTagNameSyncDocumentListener(editor,project)
                    }
                }
            }
        }, ApplicationManager.getApplication())
        ApplicationManager.getApplication().messageBus.connect().subscribe(CommandListener.TOPIC, this)
    }

    override fun beforeCommandFinished(event: CommandEvent) {
        super.beforeCommandFinished(event)
    }

}

class WXMLTagNameSyncDocumentListener(private val editor: EditorImpl, private val project: Project) : DocumentListener {

    private val documentManager = PsiDocumentManager.getInstance(project)

    init {
        editor.document.addDocumentListener(this, editor.disposable)
    }

    override fun documentChanged(event: DocumentEvent) {
        val psiFile = documentManager.getPsiFile(editor.document) ?: return
        val element = psiFile.findElementAt(event.offset) ?: return
        if (element.node.elementType === WXMLTypes.TAG_NAME) {
            // 修改了标签名称
            val parent = element.parent
            if (parent is WXMLStartTag) {
                // 修改了开始标签
                val endTag = parent.findNextSibling { it is WXMLEndTag }?:return
                val endTagNameNode = endTag.node.findChildByType(WXMLTypes.TAG_NAME)?:return
                val textRange = endTagNameNode.textRange
                ApplicationManager.getApplication().invokeLater {
                    WriteCommandAction.runWriteCommandAction(project){
//                        editor.document.re(textRange.startOffset,textRange.endOffset,element.text)
                    }
                }
            } else if (parent is WXMLEndTag) {

            }
        }
    }
}