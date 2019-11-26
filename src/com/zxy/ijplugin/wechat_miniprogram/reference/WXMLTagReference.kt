package com.zxy.ijplugin.wechat_miniprogram.reference

import a.h.it
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsonUtils

class WXMLTagReference(element: WXMLTag) :
        PsiReferenceBase<WXMLTag>(element,element.getTagNameNode()?.psi?.textRangeInParent) {

    override fun resolve(): PsiElement? {
        val tagName = element.getTagName()?:return null
        val wxmlPsiFile = this.element.containingFile
        val jsonFile = findRelateFile(wxmlPsiFile.originalFile.virtualFile, RelateFileType.JSON)?:return null
        val psiManager = PsiManager.getInstance(this.element.project)
        val jsonPsiFile = psiManager.findFile(jsonFile) as? JsonFile?:return null
        // 找到usingComponents的配置
        val usingComponentItems = mutableListOf<JsonProperty>().apply {
            ComponentJsonUtils.getUsingComponentItems(jsonPsiFile)?.let {
                this.addAll(it)
            }
            AppJsonUtils.findUsingComponentItems(element.project)?.let {
                this.addAll(it)
            }
        }
        return usingComponentItems.find {
            it.name == tagName
        }?.nameElement
    }

    override fun isSoft(): Boolean {
        return false
    }

}