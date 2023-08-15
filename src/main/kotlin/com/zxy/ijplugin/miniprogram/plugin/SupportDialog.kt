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

package com.zxy.ijplugin.miniprogram.plugin

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import javax.swing.Action
import javax.swing.JComponent

/**
 * SupportDialog
 */
class SupportDialog(project: Project) : DialogWrapper(project) {

    init {
        title = "支持"
        setOKButtonText("感谢您的支持")
        init()
    }

    override fun getStyle(): DialogStyle = DialogStyle.COMPACT

    override fun createCenterPanel(): JComponent? = panel {
        row("您可以通过以下方式来支持此项目：") {}
        val links = mapOf(
            "在Gitee上Star此项目" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin",
            "在Gitee上Watch此项目" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin",
            "反馈问题" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin/issues/new",
            "提出想法或建议" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin/issues/new",
            "提交PR" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin",
            "捐赠此项目" to "https://gitee.com/zxy_c/wechat-miniprogram-plugin"
        )
        links.entries.forEachIndexed { index, entry ->
            row("${index + 1}.") {
                link(entry.key) {
                    BrowserUtil.browse(entry.value)
                }
            }
        }
        row("${links.size + 1}.") {
            label("将此插件推荐给您的同事朋友")
        }
    }

    override fun createActions(): Array<Action> = arrayOf(okAction)

}