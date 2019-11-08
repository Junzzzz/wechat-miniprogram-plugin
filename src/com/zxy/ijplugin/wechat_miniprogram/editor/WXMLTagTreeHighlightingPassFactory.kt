package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeHighlighting.TextEditorHighlightingPass
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactory
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactoryRegistrar
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * @see com.intellij.codeInsight.daemon.impl.tagTreeHighlighting.XmlTagTreeHighlightingPassFactory
 */
class WXMLTagTreeHighlightingPassFactory : TextEditorHighlightingPassFactory,
        TextEditorHighlightingPassFactoryRegistrar {

    override fun createHighlightingPass(p0: PsiFile, p1: Editor): TextEditorHighlightingPass? {
        return null
    }

    /**
     * @see com.intellij.codeInsight.daemon.impl.tagTreeHighlighting.XmlTagTreeHighlightingPassFactory.registerHighlightingPassFactory
     */
    override fun registerHighlightingPassFactory(registrar: TextEditorHighlightingPassRegistrar, p1: Project) {
        registrar.registerTextEditorHighlightingPass(this, intArrayOf(4), null as IntArray?, false, -1)
    }
}