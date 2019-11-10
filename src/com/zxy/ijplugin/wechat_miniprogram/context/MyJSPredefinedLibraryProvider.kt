package com.zxy.ijplugin.wechat_miniprogram.context

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.webcore.libraries.ScriptingLibraryModel

class MyJSPredefinedLibraryProvider : JSPredefinedLibraryProvider() {

    override fun getPredefinedLibraries(project: Project): Array<out ScriptingLibraryModel> {
        if (isWechatMiniProgramContext(project)){
            val fileUrl = this.javaClass.getResource("/wx.ts")
            return arrayOf(
                    ScriptingLibraryModel.createPredefinedLibrary(
                            "wechat-mini-program-api", arrayOf(VfsUtil.findFileByURL(fileUrl)), true
                    )
            )
        }
        return ScriptingLibraryModel.EMPTY_ARRAY
    }

}