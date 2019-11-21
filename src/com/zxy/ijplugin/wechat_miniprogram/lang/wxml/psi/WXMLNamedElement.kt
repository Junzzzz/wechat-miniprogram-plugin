package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiNameIdentifierOwner

interface WXMLNamedElement: PsiNameIdentifierOwner

abstract class WXMLNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node),WXMLNamedElement