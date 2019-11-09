package com.zxy.ijplugin.wechat_miniprogram.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType

class WechatMiniProgramModuleBuilder : ModuleBuilder() {

    override fun getModuleType(): ModuleType<*> {
        return WechatMiniProgramModuleType.getInstance()
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep? {
        return WechatMiniProgramModuleStep()
    }

}