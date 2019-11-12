package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText

object WXMLElementFactory {

    fun createStringText(project: Project, text: String): WXMLStringText {
        val psiFile = createDummyFile(
                project, """
            <a a="$text"/>
        """.trimIndent()
        )
        return PsiTreeUtil.findChildOfType(psiFile, WXMLStringText::class.java)!!
    }

    private fun createDummyFile(project: Project, fileContent: String): PsiFile {
        val name = "dummy.wxml"
        return PsiFileFactory.getInstance(project).createFileFromText(name, WXMLFileType.INSTANCE, fileContent)
    }

}