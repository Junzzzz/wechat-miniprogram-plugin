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

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "com.zxy.ijplugin.zApiForSpring.settings.module.ProjectSettings",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service(Service.Level.PROJECT)
class MyProjectSettings : PersistentStateComponent<MyProjectSettings.MyState> {

    companion object {
        fun getState(project: Project): MyState {
            return project.getService(MyProjectSettings::class.java).myState
        }

        const val DEFAULT_COMPONENT_ROOT = "/"
    }

    private var myState = MyState()

    data class MyState(
        var miniprogramType: MiniProgramType = MiniProgramType.WEI_XIN,
        var enableSupport: EnableSupportType = EnableSupportType.CONFIG_DETECT,
        var componentRoot: String = DEFAULT_COMPONENT_ROOT
    ) {

    }

    override fun getState(): MyState? {
        return myState
    }

    override fun loadState(state: MyState) {
        this.myState = state
    }
}

enum class MiniProgramType {
    WEI_XIN, QQ
}

enum class EnableSupportType {
    CONFIG_DETECT,
    ENABLE
}