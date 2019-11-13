package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSItemPresentation
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory
import javax.swing.Icon


object WXSSPsiImplUtils {

    /*WXSSIdSelector*/

    private fun getIdNodeByWXSSIdSelector(
            element: WXSSIdSelector
    ) = element.node.findChildByType(WXSSTypes.ID)

    @JvmStatic
    fun getPresentation(element: WXSSIdSelector): ItemPresentation {
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
    fun getName(element: WXSSIdSelector): String? {
        return getId(element)
    }

    @JvmStatic
    fun setName(element: WXSSIdSelector, newName: String): PsiElement {
        val idNode = getIdNodeByWXSSIdSelector(element)
        idNode?.let {
            element.node.replaceChild(idNode, WXSSElementFactory.createId(element.project, newName))
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSIdSelector): PsiElement? {
        return getIdNodeByWXSSIdSelector(element)?.psi
    }

    @JvmStatic
    fun getId(wxssIdSelector: WXSSIdSelector): String? {
        return getIdNodeByWXSSIdSelector(wxssIdSelector)?.text
    }

    @JvmStatic
    fun getIcon(wxssIdSelector: WXSSIdSelector): Icon {
        return AllIcons.Xml.Html_id
    }

    /*WXSSClassSelector*/

    private fun getClassNodeByWXSSClassSelector(
            wxssClassSelector: WXSSClassSelector
    ) = wxssClassSelector.node.findChildByType(WXSSTypes.CLASS)!!

    @JvmStatic
    fun getClassName(wxssClassSelector: WXSSClassSelector): String {
        return getClassNodeByWXSSClassSelector(wxssClassSelector).text
    }

    @JvmStatic
    fun getPresentation(element: WXSSClassSelector): ItemPresentation {
        return object : WXSSItemPresentation(element) {

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.Xml.Css_class
            }
        }
    }

    @JvmStatic
    fun getName(element: WXSSClassSelector): String {
        return getClassName(element)
    }

    @JvmStatic
    fun setName(element: WXSSClassSelector, newName: String): PsiElement {
        val node = getClassNodeByWXSSClassSelector(element)
        element.node.replaceChild(node, WXSSElementFactory.createClass(element.project, newName))
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSClassSelector): PsiElement? {
        return getClassNodeByWXSSClassSelector(element).psi
    }

    @JvmStatic
    fun getIcon(wxssClassSelector: WXSSClassSelector): Icon {
        return AllIcons.Xml.Css_class
    }

}