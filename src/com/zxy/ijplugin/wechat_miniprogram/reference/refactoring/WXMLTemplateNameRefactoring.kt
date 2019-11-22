package com.zxy.ijplugin.wechat_miniprogram.reference.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSStringText

class WXMLTemplateNameRefactoring : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is WXSSStringText
    }

}