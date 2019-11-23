package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsUtils

class WXMLAttributeReference(element: WXMLAttribute) :
        PsiReferenceBase<WXMLAttribute>(element, element.firstChild.textRangeInParent) {
    override fun resolve(): PsiElement? {
        val attributeName = this.element.name
        val wxmlTag = PsiTreeUtil.getParentOfType(this.element, WXMLTag::class.java)
        return wxmlTag?.getDefinitionJsFile()?.let { jsFile ->
            ComponentJsUtils.findPropertiesItems(jsFile)?.find {
                it.name == attributeName
            }
        }?.nameIdentifier
    }
}