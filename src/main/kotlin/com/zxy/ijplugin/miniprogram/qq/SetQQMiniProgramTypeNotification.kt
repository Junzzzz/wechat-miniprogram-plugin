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

package com.zxy.ijplugin.miniprogram.qq

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.ClickListener
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.ui.EditorNotifications
import com.zxy.ijplugin.miniprogram.localization.message
import com.zxy.ijplugin.miniprogram.localization.settingsMessage
import com.zxy.ijplugin.miniprogram.settings.MiniProgramType
import com.zxy.ijplugin.miniprogram.settings.MyProjectConfigurable
import com.zxy.ijplugin.miniprogram.settings.MyProjectSettings
import com.zxy.ijplugin.miniprogram.icons.WechatMiniProgramIcons
import java.awt.Cursor
import java.awt.event.MouseEvent
import java.util.function.Function
import javax.swing.JComponent

class SetQQMiniProgramTypeNotification :
    EditorNotificationProvider {

    class MyEditorNotificationPanel(
        private val project: Project
    ) : EditorNotificationPanel() {
        init {
            this.icon(WechatMiniProgramIcons.QQ_LOGO)
            this.myLabel.text = settingsMessage("changeTypeToQQQuestion")
            this.createActionLabel(message("yes")) {
                // 设置成QQ小程序类型
                MyProjectSettings.getState(project).miniprogramType = MiniProgramType.QQ
                // 并更新状态
                EditorNotifications.getInstance(project).updateAllNotifications()
            }
            this.myGearLabel.icon = AllIcons.General.Settings
            this.myGearLabel.toolTipText = settingsMessage("openTip")
            myGearLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            object : ClickListener() {
                override fun onClick(e: MouseEvent, clickCount: Int): Boolean {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, MyProjectConfigurable.DISPLAY_NAME)
                    return true
                }
            }.installOn(myGearLabel)
        }
    }

    private fun createNotificationPanel(
        file: VirtualFile, project: Project
    ): MyEditorNotificationPanel? {
        val psiFile = PsiManager.getInstance(project).findFile(file)
        if (psiFile != null && MyProjectSettings.getState(
                project
            ).miniprogramType == MiniProgramType.WEI_XIN && (psiFile.fileType == QMLFileType.INSTANCE || psiFile.fileType == QSFileType.INSTANCE || psiFile.fileType == QSSFileType.INSTANCE)
        ) {
            // 在QQ小程序特有的文件上显示面板
            return MyEditorNotificationPanel(project)
        }
        return null
    }

    override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out JComponent?> {
        return Function { _: FileEditor? ->
            createNotificationPanel(
                file,
                project
            )
        }
    }

}