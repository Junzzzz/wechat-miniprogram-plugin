package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils

import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement

object WXMLUtils {
    fun isValidTagName(charSequence: CharSequence): Boolean {
        return charSequence.all { it.isDigit() || (it.isLetter() && it.isLowerCase()) || it == '-' || it == '_' }
    }
}

/**
 * 判断一个元素是否需要换行
 * 这个元素不能是内联元素
 * 这个元素中必须包含且只能包含块级元素
 */
fun WXMLElement.isMultiLine(): Boolean {
    return !this.isInnerElement() && PsiTreeUtil.findChildrenOfType(this, WXMLElement::class.java).let { children ->
        children.isNotEmpty() && children.all { !it.isInnerElement() }
    }
}

fun WXMLElement.isInnerElement(): Boolean {
    return WXMLMetadata.INNER_ELEMENT_NAMES.contains(this.tagName)
}

fun WXMLAttribute.isEventHandler(): Boolean {
    val name = this.name
    return WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.any {
        name.startsWith(it)
    }
}