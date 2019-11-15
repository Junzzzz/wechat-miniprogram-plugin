package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.impl.include.FileIncludeInfo
import com.intellij.psi.impl.include.FileIncludeProvider
import com.intellij.util.Consumer
import com.intellij.util.indexing.FileContent
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage

class WxmlJsModuleFileIncludeProvider : FileIncludeProvider() {
    override fun registerFileTypesUsedForIndexing(consumer: Consumer<FileType>) {
        return consumer.consume(WXMLFileType.INSTANCE)
    }

    override fun getId(): String {
        return WXMLLanguage.INSTANCE.id
    }

    override fun getIncludeInfos(fileContent: FileContent?): Array<FileIncludeInfo> {
        return FileIncludeInfo.EMPTY
    }

    override fun acceptFile(virtualFile: VirtualFile): Boolean {
        return virtualFile.fileType == WXMLFileType.INSTANCE
    }
}