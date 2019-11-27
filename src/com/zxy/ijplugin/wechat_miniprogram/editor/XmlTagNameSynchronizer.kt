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
