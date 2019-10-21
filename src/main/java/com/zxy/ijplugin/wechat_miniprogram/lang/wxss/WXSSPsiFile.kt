package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class WXSSPsiFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, WXSSLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return WXSSFileType.INSTANCE
    }
}