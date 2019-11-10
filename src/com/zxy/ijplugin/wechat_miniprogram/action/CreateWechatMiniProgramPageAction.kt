package com.zxy.ijplugin.wechat_miniprogram.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent



class CreateWechatMiniProgramPageAction : AnAction() {

    override fun actionPerformed(anActionEvent: AnActionEvent) {

    }

    override fun update(anActionEvent: AnActionEvent) {
        val project = anActionEvent.project
        anActionEvent.presentation.isEnabledAndVisible = project != null
    }

}