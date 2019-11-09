package com.zxy.ijplugin.wechat_miniprogram.module

import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import javax.swing.Icon

class WechatMiniProgramModuleType : ModuleType<WechatMiniProgramModuleBuilder>(ID) {

    companion object{
        const val ID = "WECHAT_MINI_PROGRAM_MODULE_TYPE"
        fun getInstance(): WechatMiniProgramModuleType {
            return ModuleTypeManager.getInstance().findByID(ID) as WechatMiniProgramModuleType
        }
    }

    override fun createModuleBuilder(): WechatMiniProgramModuleBuilder {
        return WechatMiniProgramModuleBuilder()
    }

    override fun getName(): String {
        return "Wechat Mini Program Module Type"
    }

    override fun getDescription(): String {
        return "Wechat Mini Program Module Type"
    }

    override fun getNodeIcon(p0: Boolean): Icon {
        return AllIcons.General.Information;
    }
}