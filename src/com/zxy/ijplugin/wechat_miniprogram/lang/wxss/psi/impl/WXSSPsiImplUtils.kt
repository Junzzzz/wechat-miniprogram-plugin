package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector

import javax.swing.*
import com.intellij.psi.PsiElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory


object WXSSPsiImplUtils {

    private fun getIdNodeByWXSSIdSelector(
            element: WXSSIdSelector
    ) = element.node.findChildByType(WXSSTypes.ID)!!

    @JvmStatic
    fun getPresentation(element: WXSSIdSelector): ItemPresentation {
        print("getPresentation")
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return element.text
            }

            override fun getLocationString(): String? {
                return element.containingFile.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.Xml.Html_id
            }
        }
    }

    @JvmStatic
    fun getName(element: WXSSIdSelector): String {
        return getId(element)
    }

    @JvmStatic
    fun setName(element: WXSSIdSelector, newName: String): PsiElement {
        val idNode = getIdNodeByWXSSIdSelector(element)
        element.node.replaceChild(idNode,WXSSElementFactory.createId(element.project,newName))
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSIdSelector): PsiElement? {
        return getIdNodeByWXSSIdSelector(element).psi
    }

    @JvmStatic
    fun getId(wxssIdSelector: WXSSIdSelector): String {
        return getIdNodeByWXSSIdSelector(wxssIdSelector).text
    }

}