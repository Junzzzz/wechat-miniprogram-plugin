package com.zxy.ijplugin.wechat_miniprogram.module

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import javax.swing.JComponent
import javax.swing.JLabel

class WechatMiniProgramModuleStep : ModuleWizardStep() {
    override fun updateDataModel() {

    }

    override fun getComponent(): JComponent {
        return JLabel("Provide some setting here")
    }
}