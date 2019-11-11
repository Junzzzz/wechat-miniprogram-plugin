package com.zxy.ijplugin.wechat_miniprogram.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.zxy.ijplugin.wechat_miniprogram.context.isWechatMiniProgramContext

abstract class WechatAction : AnAction() {

    override fun update(anActionEvent: AnActionEvent) {
        anActionEvent.presentation.isEnabledAndVisible = anActionEvent.project?.let { isWechatMiniProgramContext(it) }
                ?: false
    }
}