package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class WXMLPsiFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, WXMLLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return WXMLFileType.INSTANCE
    }
}