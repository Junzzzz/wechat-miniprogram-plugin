package com.zxy.ijplugin.wechat_miniprogram.reference.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText

class WXMLTemplateNameRefactoring : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is WXMLStringText && PsiTreeUtil.getParentOfType(element, WXMLAttribute::class.java)?.let {
            it.name == "name" && PsiTreeUtil.getParentOfType(it, WXMLElement::class.java)?.tagName == "template"
        } == true
    }

}