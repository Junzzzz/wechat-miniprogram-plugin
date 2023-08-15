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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonFile
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.zxy.ijplugin.miniprogram.context.findMiniProgramNpmRootDir
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.utils.ComponentFilesCreator

class ComponentFileReferenceSet(psiElement: PsiElement) : FileReferenceSet(psiElement) {

    override fun createFileReference(range: TextRange, index: Int, text: String): FileReference {
        return ComponentFileReference(this, range, index, text)
    }

}

class ComponentFileReference(
    componentFileReferenceSet: ComponentFileReferenceSet,
    range: TextRange,
    index: Int,
    text: String
) : FileReference(
    componentFileReferenceSet,
    range, index, text
) {

    override fun innerResolveInContext(
        text: String,
        context: PsiFileSystemItem,
        result: MutableCollection<in ResolveResult>,
        caseSensitive: Boolean
    ) {
        super.innerResolveInContext(text, context, result, caseSensitive)
        val isFirst = index == 0
        val miniProgramNpmRootDir = findMiniProgramNpmRootDir(context.project)
        val tryToSearchInNpm = isFirst && !fileReferenceSet.element.text.startsWith(
            "/"
        ) && !fileReferenceSet.element.text.startsWith(
            ".."
        ) && miniProgramNpmRootDir != null

        val directoryFromNpm = if (tryToSearchInNpm) {
            // is first reference

            // search directory in npm root
            miniProgramNpmRootDir!!.findSubdirectory(text)
        } else {
            null
        }
        directoryFromNpm?.let {
            result.add(PsiElementResolveResult(it))
        }


        if (isLast && context is PsiDirectory) {
            result.addAll(findRelateFilesFromDirectory(context, text))
        }

        if (directoryFromNpm != null && isLast) {
            // resolving index files from npm directory
            result.addAll(findRelateFilesFromDirectory(directoryFromNpm, "index"))
        }

    }

    private fun findRelateFilesFromDirectory(
        directoryFromNpm: PsiDirectory, text: String
    ) = directoryFromNpm.files.filter {
        it.virtualFile.nameWithoutExtension == text
                && (it is JSFile
                || it is WXMLPsiFile
                || it is WXSSPsiFile
                || it is JsonFile)
    }.map {
        PsiElementResolveResult(it)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // 重命名移除文件名后缀
        val dotIndex = newElementName.lastIndexOf(".")
        return if (dotIndex == -1) {
            super.handleElementRename(newElementName)
        } else {
            super.handleElementRename(
                newElementName.substring(0 until dotIndex)
            )
        }
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        ComponentFilesCreator.createComponentPathFromFile(
            element.containingFile
        )?.let {
            return this.element.replace(
                JsonElementGenerator(element.project).createStringLiteral(
                    it
                )
            )
        }
        return this.element
    }

}

