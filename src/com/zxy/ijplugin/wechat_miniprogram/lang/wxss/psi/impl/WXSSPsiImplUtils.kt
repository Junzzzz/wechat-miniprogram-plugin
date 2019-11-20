package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceService
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSItemPresentation
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSIdSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSTypes
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory
import com.zxy.ijplugin.wechat_miniprogram.reference.WXSSClassSelectorSelfReference
import com.zxy.ijplugin.wechat_miniprogram.reference.WXSSIdSelectorSelfReference
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

    @JvmStatic
    fun getReference(wxssIdSelector: WXSSIdSelector): WXSSIdSelectorSelfReference? {
        val textRange = wxssIdSelector.nameIdentifier?.textRangeInParent ?: return null
        return WXSSIdSelectorSelfReference(wxssIdSelector, textRange)
    }

    @JvmStatic
    fun getTextOffset(wxssIdSelector: WXSSIdSelector): Int {
        return wxssIdSelector.textRange.startOffset + 1
    }

    /*WXSSClassSelector*/

    private fun getClassNodeByWXSSClassSelector(
            wxssClassSelector: WXSSClassSelector
    ) = wxssClassSelector.node.findChildByType(WXSSTypes.CLASS)

    @JvmStatic
    fun getClassName(wxssClassSelector: WXSSClassSelector): String? {
        return getClassNodeByWXSSClassSelector(wxssClassSelector)?.text
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
    fun getName(element: WXSSClassSelector): String? {
        return getClassName(element)
    }

    @JvmStatic
    fun setName(element: WXSSClassSelector, newName: String): PsiElement {
        getClassNodeByWXSSClassSelector(element)?.let {
            element.node.replaceChild(it, WXSSElementFactory.createClass(element.project, newName))
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSClassSelector): PsiElement? {
        return getClassNodeByWXSSClassSelector(element)?.psi
    }

    @JvmStatic
    fun getIcon(wxssClassSelector: WXSSClassSelector): Icon {
        return AllIcons.Xml.Css_class
    }

    @JvmStatic
    fun getReference(wxssClassSelector: WXSSClassSelector): PsiReference? {
        val textRange = wxssClassSelector.nameIdentifier?.textRangeInParent ?: return null
        return WXSSClassSelectorSelfReference(wxssClassSelector, textRange)
    }

    @JvmStatic
    fun getTextOffset(wxssClassSelector: WXSSClassSelector): Int {
        return wxssClassSelector.textRange.startOffset + 1
    }

    @JvmStatic
    fun getReferences(wxssClassSelector: WXSSClassSelector): Array<PsiReference> {
        return wxssClassSelector.reference?.let {
            arrayOf(it)
        } ?: emptyArray()
    }

    /*string text*/
    @JvmStatic
    fun createLiteralTextEscaper(element: WXSSStringText): LiteralTextEscaper<WXSSStringText> {
        return LiteralTextEscaper.createSimple(element)
    }

    @JvmStatic
    fun isValidHost(element: WXSSStringText): Boolean {
        return true
    }

    @JvmStatic
    fun updateText(element: WXSSStringText, newText: String): WXSSStringText {
        element.replace(WXSSElementFactory.createStringText(element.project, newText))
        return element
    }

    @JvmStatic
    fun getReferences(element: WXSSStringText): Array<out PsiReference> {
        return PsiReferenceService.getService().getContributedReferences(element)
    }

}