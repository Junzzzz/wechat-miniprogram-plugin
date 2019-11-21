package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.LocalInspectionTool

abstract class WechatMiniProgramInspectionBase : LocalInspectionTool() {

    override fun getGroupDisplayName(): String {
        return "Wechat mini program"
    }

}

abstract class WXSSInspectionBase : WechatMiniProgramInspectionBase()