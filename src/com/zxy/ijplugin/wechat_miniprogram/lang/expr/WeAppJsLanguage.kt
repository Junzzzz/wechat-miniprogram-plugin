package com.zxy.ijplugin.wechat_miniprogram.lang.expr

import com.intellij.lang.javascript.DialectOptionHolder
import com.intellij.lang.javascript.JSLanguageDialect
import com.intellij.lang.javascript.JavaScriptSupportLoader

class WeAppJsLanguage :
        JSLanguageDialect("WeAppJs", DialectOptionHolder.ECMA_6, JavaScriptSupportLoader.ECMA_SCRIPT_6) {

    companion object {
        val INSTANCE: WeAppJsLanguage = WeAppJsLanguage()
    }

    override fun getFileExtension(): String {
        return "js"
    }

}