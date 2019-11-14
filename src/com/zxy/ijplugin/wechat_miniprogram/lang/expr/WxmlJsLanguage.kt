package com.zxy.ijplugin.wechat_miniprogram.lang.expr

import com.intellij.lang.DependentLanguage
import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSLanguageDialect
import com.intellij.lang.javascript.JavaScriptSupportLoader

class WxmlJsLanguage : JSLanguageDialect("WxmlJs", DialectOptionHolder.ECMA_6, JavaScriptSupportLoader.ECMA_SCRIPT_6),
        DependentLanguage {

    companion object {
        val INSTANCE: WxmlJsLanguage = WxmlJsLanguage()
    }

    override fun getFileExtension(): String {
        return "js"
    }
}