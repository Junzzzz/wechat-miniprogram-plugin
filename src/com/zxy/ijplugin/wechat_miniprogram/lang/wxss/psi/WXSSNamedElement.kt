package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiNameIdentifierOwner

interface WXSSNamedElement: PsiNameIdentifierOwner

abstract class WXSSNamedElementImpl(node:ASTNode ) : ASTWrapperPsiElement(node),WXSSNamedElement {

}