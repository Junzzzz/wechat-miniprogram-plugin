package com.zxy.ijplugin.wechat_miniprogram.module

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.webcore.libraries.ScriptingLibraryModel

class MyJSPredefinedLibraryProvider : JSPredefinedLibraryProvider() {

    override fun getPredefinedLibraries(project: Project): Array<out ScriptingLibraryModel> {
        val basePath = project.basePath
        if (basePath != null) {
            val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
            if (baseDir != null) {
                if (hasProjectConfigFile(baseDir)) {
                    val fileUrl = this.javaClass.getResource("/wx.ts")
                    return arrayOf(
                            ScriptingLibraryModel.createPredefinedLibrary(
                                    "wechat-mini-program-api", arrayOf(VfsUtil.findFileByURL(fileUrl)), true
                            )
                    )
                }
            }
        }
        return ScriptingLibraryModel.EMPTY_ARRAY
    }

    private fun hasProjectConfigFile(baseDir: VirtualFile): Boolean {
        val projectConfigJsonFile = baseDir.children.find { it.name == "project.config.json" }
        return projectConfigJsonFile != null
    }

}