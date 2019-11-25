package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag

object ComponentWxmlUtils {

    /**
     * 判断一个wxml属性是否是自定义组件配置的externalClasses
     */
    fun isExternalClassesAttribute(attribute: WXMLAttribute): Boolean {
        return PsiTreeUtil.getParentOfType(attribute, WXMLTag::class.java)?.getDefinitionJsFile()?.let {
            ComponentJsUtils.findComponentExternalClasses(it)?.contains(attribute.name)
        } == true
    }

}