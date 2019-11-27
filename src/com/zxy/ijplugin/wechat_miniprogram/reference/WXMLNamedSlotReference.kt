package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentWxmlUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.contentRange

/**
 * 使用自定义组件时，子元素的slot属性引用该组件中定义的slot标签的name属性
 */
class WXMLNamedSlotReference(element: WXMLStringText) :
        PsiReferenceBase<WXMLStringText>(element, element.contentRange()) {

    override fun resolve(): PsiElement? {
        val slotName = element.text
        return PsiTreeUtil.getParentOfType(element, WXMLElement::class.java)?.let {
            // 获取父元素
            PsiTreeUtil.findChildOfType(PsiTreeUtil.getParentOfType(it, WXMLElement::class.java), WXMLTag::class.java)
        }?.let {
            ComponentWxmlUtils.findCustomComponentDefinitionWxmlFile(it)
        }?.let {
            PsiTreeUtil.findChildrenOfType(it, WXMLTag::class.java)
        }?.asSequence()?.filter { wxmlTag ->
            wxmlTag.name == "slot"
        }?.mapNotNull { wxmlTag ->
            PsiTreeUtil.findChildrenOfType(wxmlTag, WXMLAttribute::class.java).find {
                it.name == "name"
            }
        }?.mapNotNull {
            it.string?.stringText
        }?.find {
            it.text == slotName
        }
    }

}