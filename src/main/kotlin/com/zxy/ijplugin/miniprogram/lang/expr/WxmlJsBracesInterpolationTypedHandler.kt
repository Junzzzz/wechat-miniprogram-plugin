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

package com.zxy.ijplugin.miniprogram.lang.expr

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.lang.javascript.JSInjectionBracesUtil.InterpolationBracesCompleter
import com.intellij.lang.javascript.formatter.JSCodeStyleSettings
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage


class WxmlJsBracesInterpolationTypedHandler : TypedHandlerDelegate() {

    companion object {
        private const val start = "{{"
        private const val end = "}}"
    }

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (c == '{' && file.language == WXMLLanguage.INSTANCE) {
            if (InterpolationBracesCompleter.startMatches(start, c, editor)) {
                return if (alreadyHasEnding(editor)) {
                    Result.CONTINUE
                } else {
                    insertWithEndCompletion(c, editor, file)
                    Result.STOP
                }
            }
        }
        return Result.CONTINUE
    }

    private fun insertWithEndCompletion(
        c: Char, editor: Editor?, file: PsiFile?
    ) {
        editor ?: return
        var caretBackOffset = end.length
        val interpolation: String
        when {
            JSCodeStyleSettings.getSettings(file!!).SPACES_WITHIN_INTERPOLATION_EXPRESSIONS -> {
                interpolation = "$c  $end"
                ++caretBackOffset
            }

            start.length == 1 -> {
                interpolation = end
            }

            else -> {
                interpolation = c.toString() + end
            }
        }
        EditorModificationUtil.insertStringAtCaret(editor, interpolation, true, interpolation.length - caretBackOffset)
    }

    private fun alreadyHasEnding(
        editor: Editor
    ): Boolean {
        val stopChars: MutableSet<Char> = HashSet(
            start.length + end.length + 1
        )
        stopChars.add('\n')
        addChars(start, stopChars)
        addChars(end, stopChars)
        val offset = editor.caretModel.offset
        var i = offset
        val sequence: CharSequence = editor.document.charsSequence
        while (i < sequence.length && i < offset + 100 && !stopChars.contains(sequence[i])) {
            ++i
        }
        return if (i + end.length > sequence.length) false else end.contentEquals(
            sequence.subSequence(i, i + end.length)
        )
    }

    private fun addChars(start: String, stopChars: MutableSet<Char>) {
        for (element in start) {
            stopChars.add(element)
        }
    }

}

