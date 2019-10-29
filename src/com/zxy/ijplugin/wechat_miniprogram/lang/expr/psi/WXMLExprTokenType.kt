package com.zxy.ijplugin.wechat_miniprogram.lang.expr.psi

import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage


class WXMLExprTokenType(debugName: String) :
        IElementType(debugName, WXMLLanguage.INSTANCE)
