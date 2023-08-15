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

package com.zxy.ijplugin.miniprogram.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.panel
import com.zxy.ijplugin.miniprogram.localization.message
import com.zxy.ijplugin.miniprogram.localization.settingsMessage

class MyProjectConfigurable(private val project: Project) : BoundConfigurable(DISPLAY_NAME) {

    companion object {
        const val DISPLAY_NAME = "Wechat Mini Program"
    }

    private val settings = MyProjectSettings.getState(this.project)

    override fun createPanel(): DialogPanel {
        return panel {
            buttonsGroup {
                row {
                    radioButton(settingsMessage("detect"), EnableSupportType.CONFIG_DETECT)
                    radioButton(settingsMessage("enable"), EnableSupportType.ENABLE)
                }
            }.bind(settings::enableSupport)
            buttonsGroup {
                row(settingsMessage("miniProgramType")) {
                    radioButton(message("weixin"), MiniProgramType.WEI_XIN)
                    radioButton(message("qq"), MiniProgramType.QQ)
                }
            }.bind(settings::miniprogramType)
        }
    }

}