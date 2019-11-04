package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.impl

import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLEndTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStartTag

object WXMLPsiImplUtils {

    @JvmStatic
    fun getEndTag(element: WXMLStartTag): WXMLEndTag? {
        do {
            val next = element.nextSibling ?: return null
            if (next is WXMLEndTag) {
                return next
            }
        } while (true)
    }

    @JvmStatic
    fun getStartTag(element: WXMLEndTag): WXMLStartTag? {
        do {
            val prev = element.prevSibling ?: return null
            if (prev is WXMLStartTag) {
                return prev
            }
        } while (true)
    }

}