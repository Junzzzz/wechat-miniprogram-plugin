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

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.localization.notifyMessage

class UpdateInfoActivity : StartupActivity.DumbAware, ProjectActivity {

    companion object {
        private const val LAST_VERSION_KEY = "$pluginId.lastVersion"

    }

    override suspend fun execute(project: Project) {
        runActivity(project)
    }

    override fun runActivity(project: Project) {
        if (!isWechatMiniProgramContext(project)) return
        val pluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId(pluginId)) ?: return
        val propertiesComponent = PropertiesComponent.getInstance()
        val lastVersion = propertiesComponent.getValue(LAST_VERSION_KEY)
        val version = pluginDescriptor.version
        if (version == lastVersion) {
            return
        }

        showUpdateOrInstallNotification(pluginDescriptor, version, project, lastVersion)

        propertiesComponent.setValue(LAST_VERSION_KEY, version)
    }

    private fun showUpdateOrInstallNotification(
        pluginDescriptor: IdeaPluginDescriptor, version: String,
        project: Project,
        lastVersion: String?
    ) {
        // 显示更新通知
        val isInstall = lastVersion == null
        val title = if (isInstall) {
            notifyMessage("install.title", pluginDescriptor.name, version)
        } else {
            notifyMessage("update.title", pluginDescriptor.name, version)
        }
        val changeNotesSection = if (isInstall) {
            ""
        } else {
            """更新说明：<br/>
                ${pluginDescriptor.changeNotes?.split("<br/>")?.firstOrNull().orEmpty()}
                <br/>"""
        }
        val content = """
                $changeNotesSection
                如果此插件对您有帮助，请支持一下
                <br/>
                <a href="https://gitee.com/zxy_c/wechat-miniprogram-plugin/releases">发行记录/更新日志</a>
                <br/>
                <a href="https://gitee.com/zxy_c/wechat-miniprogram-plugin/wikis">使用文档</a>
                <br/>
            """.trimIndent()

        val notification = NotificationGroupManager.getInstance().getNotificationGroup(this.javaClass.name)
            .createNotification(
                title, content, NotificationType.INFORMATION
            ).apply {
                this.addAction(object : NotificationAction(notifyMessage("support.action.text")) {
                    private val urlOpeningBehavior = NotificationListener.UrlOpeningListener(false)

                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        SupportDialog(project).show()
                    }
                })
            }
        notification
            .addAction(SupportAction(project, notification))
            .setImportant(true)
            .let {
                Notifications.Bus.notify(it, project)
            }
    }

    class SupportAction(
        private val project: Project,
        private val notification: Notification
    ) : DumbAwareAction("支持一下") {
        override fun actionPerformed(e: AnActionEvent) {
            notification.hideBalloon()
            SupportDialog(project).show()
        }
    }

}