package com.zxy.ijplugin.wechat_miniprogram.reference.manipulator

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.PsiElement
import com.zxy.ijplugin.wechat_miniprogram.utils.replace

@Suppress("UNCHECKED_CAST")
abstract class MyAbstractElementManipulator<T:PsiElement>(private val createNewElement:(project:Project, elementText:String)->T):
        AbstractElementManipulator<T>() {
    override fun handleContentChange(element: T, textRange: TextRange, newContent: String): T? {
        return element.replace(
                createNewElement(
                        element.project, element.text.replace(textRange, newContent)
                )
        ) as T
    }
}