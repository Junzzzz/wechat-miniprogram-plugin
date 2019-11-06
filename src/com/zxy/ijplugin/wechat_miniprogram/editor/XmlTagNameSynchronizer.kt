////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.zxy.ijplugin.wechat_miniprogram.editor
//
//import com.intellij.application.options.editor.WebEditorOptions
//import com.intellij.codeInsight.lookup.LookupManager
//import com.intellij.codeInsight.lookup.impl.LookupImpl
//import com.intellij.codeInspection.htmlInspections.RenameTagBeginOrEndIntentionAction
//import com.intellij.lang.Language
//import com.intellij.lang.html.HTMLLanguage
//import com.intellij.lang.injection.InjectedLanguageManager
//import com.intellij.lang.xhtml.XHTMLLanguage
//import com.intellij.lang.xml.XMLLanguage
//import com.intellij.openapi.Disposable
//import com.intellij.openapi.application.ApplicationManager
//import com.intellij.openapi.command.CommandEvent
//import com.intellij.openapi.command.CommandListener
//import com.intellij.openapi.command.undo.UndoManager
//import com.intellij.openapi.diagnostic.Attachment
//import com.intellij.openapi.diagnostic.Logger
//import com.intellij.openapi.editor.Caret
//import com.intellij.openapi.editor.CaretAction
//import com.intellij.openapi.editor.Document
//import com.intellij.openapi.editor.Editor
//import com.intellij.openapi.editor.EditorFactory
//import com.intellij.openapi.editor.RangeMarker
//import com.intellij.openapi.editor.event.DocumentEvent
//import com.intellij.openapi.editor.event.DocumentListener
//import com.intellij.openapi.editor.event.EditorFactoryEvent
//import com.intellij.openapi.editor.event.EditorFactoryListener
//import com.intellij.openapi.editor.impl.EditorImpl
//import com.intellij.openapi.fileEditor.FileDocumentManager
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.util.Couple
//import com.intellij.openapi.util.Key
//import com.intellij.openapi.util.TextRange
//import com.intellij.openapi.util.text.StringUtil
//import com.intellij.openapi.vfs.VirtualFile
//import com.intellij.pom.core.impl.PomModelImpl
//import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider
//import com.intellij.psi.PsiDocumentManager
//import com.intellij.psi.PsiElement
//import com.intellij.psi.PsiFile
//import com.intellij.psi.PsiManager
//import com.intellij.psi.impl.PsiDocumentManagerBase
//import com.intellij.psi.impl.source.tree.TreeUtil
//import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
//import com.intellij.psi.templateLanguages.TemplateLanguage
//import com.intellij.psi.xml.XmlTokenType
//import com.intellij.util.containers.ContainerUtil
//import com.intellij.xml.util.HtmlUtil
//import com.intellij.xml.util.XmlUtil
//import com.zxy.ijplugin.wechat_miniprogram.editor.XmlTagNameSynchronizer.TagNameSynchronizer.Companion.MARKERS_KEY
//import com.zxy.ijplugin.wechat_miniprogram.editor.XmlTagNameSynchronizer.TagNameSynchronizer.Companion.clearMarkers
//import com.zxy.ijplugin.wechat_miniprogram.editor.XmlTagNameSynchronizer.TagNameSynchronizer.Companion.findSupportElement
//import com.zxy.ijplugin.wechat_miniprogram.editor.XmlTagNameSynchronizer.TagNameSynchronizer.Companion.findSupportForTagList
//import com.zxy.ijplugin.wechat_miniprogram.editor.XmlTagNameSynchronizer.TagNameSynchronizer.Companion.fitsInMarker
//import java.util.Objects
//
//class XmlTagNameSynchronizer private constructor() : CommandListener, EditorFactoryListener {
//
//    init {
//        ApplicationManager.getApplication().messageBus.connect().subscribe(CommandListener.TOPIC, this)
//    }
//
//    override fun editorCreated(event: EditorFactoryEvent) {
//
//        val editor = event.editor
//        val project = editor.project
//        if (project != null && editor is EditorImpl) {
//            val document = editor.document
//            val file = FileDocumentManager.getInstance().getFile(document)
//            val language = findXmlLikeLanguage(project, file)
//            if (language != null) {
//                XmlTagNameSynchronizer.TagNameSynchronizer(editor, project, language).listenForDocumentChanges()
//            }
//
//        }
//    }
//
//    override fun beforeCommandFinished(event: CommandEvent) {
//
//        val synchronizers = findSynchronizers(event.document)
//        val var4 = synchronizers.size
//
//        for (var5 in 0 until var4) {
//            val synchronizer = synchronizers[var5]
//            synchronizer.beforeCommandFinished()
//        }
//
//    }
//
//    private inner class TagNameSynchronizer constructor(
//            private val myEditor: EditorImpl, project: Project, private val myLanguage: Language
//    ) : DocumentListener {
//        private val myDocumentManager: PsiDocumentManagerBase
//        private var myApplying: Boolean = false
//
//        init {
//            this.myDocumentManager = PsiDocumentManager.getInstance(project) as PsiDocumentManagerBase
//        }
//
//        private fun listenForDocumentChanges() {
//            val disposable = this.myEditor.disposable
//            val document = this.myEditor.document
//            document.addDocumentListener(this, disposable)
//            this.myEditor.putUserData(XmlTagNameSynchronizer.SYNCHRONIZER_KEY, this)
//        }
//
//        override fun beforeDocumentChange(event: DocumentEvent) {
//
//            if (WebEditorOptions.getInstance().isSyncTagEditing) {
//                val document = event.document
//                val project = Objects.requireNonNull<Project>(this.myEditor.project) as Project
//                if (!this.myApplying && !project.isDefault && !UndoManager.getInstance(
//                                project
//                        ).isUndoInProgress && PomModelImpl.isAllowPsiModification() && !document.isInBulkUpdate) {
//                    val offset = event.offset
//                    val oldLength = event.oldLength
//                    val fragment = event.newFragment
//                    val newLength = event.newLength
//                    if (document.getUserData(XmlTagNameSynchronizer.SKIP_COMMAND) !== java.lang.Boolean.TRUE) {
//                        val caret = this.myEditor.caretModel.currentCaret
//
//                        for (i in 0 until newLength) {
//                            if (!XmlUtil.isValidTagNameChar(fragment[i])) {
//                                clearMarkers(caret)
//                                return
//                            }
//                        }
//
//                        var markers: Couple<RangeMarker>? = caret.getUserData(MARKERS_KEY)
//                        if (markers != null && !fitsInMarker(markers, offset, oldLength)) {
//                            clearMarkers(caret)
//                            markers = null
//                        }
//
//                        if (markers == null) {
//                            val file = this.myDocumentManager.getPsiFile(document)
//                            if (file == null || this.myDocumentManager.synchronizer.isInSynchronization(document)) {
//                                return
//                            }
//
//                            val leader = this.createTagNameMarker(caret) ?: return
//
//                            leader.isGreedyToLeft = true
//                            leader.isGreedyToRight = true
//                            if (this.myDocumentManager.isUncommited(document)) {
//                                this.myDocumentManager.commitDocument(document)
//                            }
//
//                            val support = this.findSupport(leader, file, document) ?: return
//
//                            support.isGreedyToLeft = true
//                            support.isGreedyToRight = true
//                            markers = Couple.of(leader, support)
//                            if (!fitsInMarker(markers, offset, oldLength)) {
//                                return
//                            }
//
//                            caret.putUserData(MARKERS_KEY, markers)
//                        }
//
//                    }
//                }
//            }
//        }
//
//        private fun createTagNameMarker(caret: Caret): RangeMarker? {
//            val offset = caret.offset
//            val document = this.myEditor.document
//            val sequence = document.charsSequence
//            var start = -1
//            var seenColon = false
//
//            var end: Int
//            end = offset - 1
//            while (end >= Math.max(0, offset - 50)) {
//                try {
//                    val c = sequence[end]
//                    if (c == '<' || c == '/' && end > 0 && sequence[end - 1] == '<') {
//                        start = end + 1
//                        break
//                    }
//
//                    if (!XmlUtil.isValidTagNameChar(c)) {
//                        break
//                    }
//
//                    seenColon = seenColon or (c == ':')
//                } catch (var10: IndexOutOfBoundsException) {
//                    XmlTagNameSynchronizer.LOG.error(
//                            "incorrect offset:$end, initial: $offset",
//                            *arrayOf(Attachment("document.txt", sequence.toString()))
//                    )
//                    return null
//                }
//
//                --end
//            }
//
//            if (start < 0) {
//                return null
//            } else {
//                end = -1
//
//                for (i in offset until Math.min(document.textLength, offset + 50)) {
//                    val c = sequence[i]
//                    if (!XmlUtil.isValidTagNameChar(c) || seenColon && c == ':') {
//                        end = i
//                        break
//                    }
//
//                    seenColon = seenColon or (c == ':')
//                }
//
//                return if (end >= 0 && start <= end) document.createRangeMarker(start, end, true) else null
//            }
//        }
//
//        internal fun beforeCommandFinished() {
//            val action = { caret ->
//                val markers = caret.getUserData(MARKERS_KEY) as Couple<*>
//                if (markers != null && (markers.first as RangeMarker).isValid && (markers.second as RangeMarker).isValid) {
//                    val document = this.myEditor.document
//                    val apply = {
//                        val leader = markers.first as RangeMarker
//                        val support = markers.second as RangeMarker
//                        if (document.textLength >= leader.endOffset) {
//                            val name = document.getText(TextRange(leader.startOffset, leader.endOffset))
//                            if (document.textLength >= support.endOffset && name != document.getText(
//                                            TextRange(support.startOffset, support.endOffset)
//                                    )) {
//                                document.replaceString(support.startOffset, support.endOffset, name)
//                            }
//
//                        }
//                    }
//                    ApplicationManager.getApplication().runWriteAction {
//                        val lookup = LookupManager.getActiveLookup(this.myEditor) as LookupImpl?
//                        lookup?.performGuardedChange(apply) ?: apply.run()
//
//                    }
//                }
//            }
//            this.myApplying = true
//
//            try {
//                if (this.myEditor.caretModel.isIteratingOverCarets) {
//                    action.perform(this.myEditor.caretModel.currentCaret)
//                } else {
//                    this.myEditor.caretModel.runForEachCaret(action)
//                }
//            } finally {
//                this.myApplying = false
//            }
//
//        }
//
//        private fun findSupport(leader: RangeMarker, file: PsiFile, document: Document): RangeMarker? {
//            val offset = leader.startOffset
//            var element: PsiElement? = InjectedLanguageUtil.findElementAtNoCommit(file, offset)
//            var support = findSupportElement(element)
//            if (support == null && file.viewProvider is MultiplePsiFilesPerDocumentFileViewProvider) {
//                element = file.viewProvider.findElementAt(offset, this.myLanguage)
//                support = findSupportElement(element)
//            }
//
//            if (support == null) {
//                return findSupportForTagList(leader, element, document)
//            } else {
//                val range = support.textRange
//                val realRange = InjectedLanguageManager.getInstance(file.project)
//                        .injectedToHost(element!!.containingFile, range)
//                return document.createRangeMarker(realRange.startOffset, realRange.endOffset, true)
//            }
//        }
//
//        companion object {
//            private val MARKERS_KEY = Key.create<Couple<RangeMarker>>("tag.name.synchronizer.markers")
//            private val EMPTY = arrayOfNulls<XmlTagNameSynchronizer.TagNameSynchronizer>(0)
//
//            private fun fitsInMarker(markers: Couple<RangeMarker>, offset: Int, oldLength: Int): Boolean {
//                val leader = markers.first as RangeMarker
//                return leader.isValid && offset >= leader.startOffset && offset + oldLength <= leader.endOffset
//            }
//
//            private fun clearMarkers(caret: Caret) {
//                val markers = caret.getUserData(MARKERS_KEY) as Couple<*>?
//                if (markers != null) {
//                    (markers.first as RangeMarker).dispose()
//                    (markers.second as RangeMarker).dispose()
//                    caret.putUserData(MARKERS_KEY, null as Any?)
//                }
//
//            }
//
//            private fun findSupportForTagList(
//                    leader: RangeMarker, element: PsiElement?, document: Document
//            ): RangeMarker? {
//                if (leader.startOffset == leader.endOffset && element != null) {
//                    var support: PsiElement? = null
//                    var first: PsiElement
//                    if ("<>" == element.text) {
//                        first = element.parent.lastChild
//                        if ("</>" == first.text) {
//                            support = first
//                        }
//                    }
//
//                    if ("</>" == element.text) {
//                        first = element.parent.firstChild
//                        if ("<>" == first.text) {
//                            support = first
//                        }
//                    }
//
//                    if (support != null) {
//                        val range = support.textRange
//                        return document.createRangeMarker(range.endOffset - 1, range.endOffset - 1, true)
//                    } else {
//                        return null
//                    }
//                } else {
//                    return null
//                }
//            }
//
//            private fun findSupportElement(element: PsiElement?): PsiElement? {
//                if (element != null && TreeUtil.findSibling(element.node, XmlTokenType.XML_TAG_END) != null) {
//                    var support = RenameTagBeginOrEndIntentionAction.findOtherSide(element, false)
//                    support = if (support != null && element !== support) support else RenameTagBeginOrEndIntentionAction.findOtherSide(
//                            element, true
//                    )
//                    return if (support != null && StringUtil.equals(element.text, support.text)) support else null
//                } else {
//                    return null
//                }
//            }
//        }
//    }
//
//    companion object {
//        private val SKIP_COMMAND = Key.create<Boolean>("tag.name.synchronizer.skip.command")
//        private val LOG = Logger.getInstance(XmlTagNameSynchronizer::class.java)
//        private val SUPPORTED_LANGUAGES: Set<Language>
//        private val SYNCHRONIZER_KEY: Key<XmlTagNameSynchronizer.TagNameSynchronizer>
//
//        private fun findXmlLikeLanguage(project: Project, file: VirtualFile?): Language? {
//            val psiFile = if (file != null && file.isValid) PsiManager.getInstance(project).findFile(file) else null
//            if (psiFile != null) {
//                val var3 = psiFile.viewProvider.languages.iterator()
//
//                var var10000: Set<*>
//                var language: Language
//                do {
//                    do {
//                        if (!var3.hasNext()) {
//                            return null
//                        }
//
//                        language = var3.next() as Language
//                        var10000 = SUPPORTED_LANGUAGES
//                        language.javaClass
//                    } while (ContainerUtil.find<Any>(
//                                    var10000,
//                                    Condition<Any> { language.isKindOf() }) == null && !HtmlUtil.supportsXmlTypedHandlers(
//                                    psiFile
//                            ))
//                } while (language is TemplateLanguage)
//
//                return language
//            } else {
//                return null
//            }
//        }
//
//        private fun findSynchronizers(document: Document?): Array<XmlTagNameSynchronizer.TagNameSynchronizer> {
//            val var10000: Array<XmlTagNameSynchronizer.TagNameSynchronizer>?
//            if (WebEditorOptions.getInstance().isSyncTagEditing && document != null) {
//                val editors = EditorFactory.getInstance().getEditors(document)
//                var10000 = ContainerUtil.mapNotNull<Editor, TagNameSynchronizer>(
//                        editors, { editor -> editor.getUserData(SYNCHRONIZER_KEY) },
//                        XmlTagNameSynchronizer.TagNameSynchronizer.EMPTY
//                ) as Array<XmlTagNameSynchronizer.TagNameSynchronizer>
//
//                return var10000
//            } else {
//                var10000 = XmlTagNameSynchronizer.TagNameSynchronizer.EMPTY
//
//                return var10000
//            }
//        }
//
//        fun runWithoutCancellingSyncTagsEditing(document: Document, runnable: Runnable) {
//
//            document.putUserData(SKIP_COMMAND, java.lang.Boolean.TRUE)
//
//            try {
//                runnable.run()
//            } finally {
//                document.putUserData(SKIP_COMMAND, null as Any?)
//            }
//
//        }
//
//        init {
//            SUPPORTED_LANGUAGES = ContainerUtil.set(
//                    *arrayOf<Language>(HTMLLanguage.INSTANCE, XMLLanguage.INSTANCE, XHTMLLanguage.INSTANCE)
//            )
//            SYNCHRONIZER_KEY = Key.create("tag_name_synchronizer")
//        }
//    }
//}
