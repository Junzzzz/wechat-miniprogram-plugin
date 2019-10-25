package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSValue

object WXSSElementFactory {

    fun createWXSSValue(project:Project,value:String): WXSSValue {
        val file = createDummyFile(project,"""
            .a{
                key: $value
            }
        """.trimIndent())
        return PsiTreeUtil.findChildOfType(file,WXSSValue::class.java)!!
    }

    private fun createDummyFile(project: Project, fileContent:String): PsiFile {
        val name = "dummy.wxss"
        return PsiFileFactory.getInstance(project).createFileFromText(name,WXSSFileType.INSTANCE,fileContent)
    }

}