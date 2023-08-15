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

package com.zxy.ijplugin.miniprogram.context

import com.intellij.lang.javascript.library.JSPredefinedLibraryProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.util.ResourceUtil
import com.intellij.webcore.libraries.ScriptingLibraryModel

class MyJSPredefinedLibraryProvider : JSPredefinedLibraryProvider() {

    companion object {
        val PAGE_LIFETIMES = arrayOf(
            "onLoad", "onShow", "onReady", "onHide", "onUnload", "onPullDownRefresh", "onReachBottom",
            "onShareAppMessage", "onPageScroll", "onResize", "onTabItemTap"
        )
    }

    override fun getPredefinedLibraries(project: Project): Array<out ScriptingLibraryModel> {
        val isOn = isWechatMiniProgramContext(project, false)

        val isQQ = project.isQQContext()
        return arrayOf(
            ScriptingLibraryModel.createPredefinedLibrary(
                "Wechat Miniprogram API",
                arrayOf(
                    VfsUtil.findFileByURL(
                        ResourceUtil.getResource(
                            javaClass.classLoader, "/library/", "wx"
                        )
                    )
                ).filterNotNull().toTypedArray(),
                isOn && !isQQ
            ),
            ScriptingLibraryModel.createPredefinedLibrary(
                "QQ Miniprogram API",
                arrayOf(
                    VfsUtil.findFileByURL(
                        ResourceUtil.getResource(
                            javaClass.classLoader, "/library/qq", "typings"
                        )
                    )
                ).filterNotNull().toTypedArray(),
                isOn && isQQ
            )
        )
    }

}