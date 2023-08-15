/*
 *     Copyright (c) [2019] [zxy]
 *     [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *     You can use this software according to the terms and conditions of the Mulan PSL v1.
 *     You may obtain a copy of Mulan PSL v1 at:
 *         http://license.coscl.org.cn/MulanPSL
 *     THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *     PURPOSE.
 *     See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.miniprogram.lang.wxml.tag

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlElementDescriptor
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.miniprogram.utils.ComponentJsonUtils

/**
 * 提供Wxml小程序自带组件以及自定义组件的标签描述
 */
class WXMLElementDescriptorProvider : XmlElementDescriptorProvider {
    override fun getDescriptor(xmlTag: XmlTag): XmlElementDescriptor? {
        if (xmlTag.language !is WXMLLanguage) {
            return null
        }
        val tagName = xmlTag.name.ifBlank { return null }
        val jsonProperty = findCustomComponentJsonProperty(xmlTag)
        if (jsonProperty != null) {
            return WxmlCustomComponentDescriptor(jsonProperty)
        }
        return WXMLMetadata.getElementDescriptions(xmlTag.project).find {
            it.name == tagName
        }?.let {
            WXMLElementDescriptor(
                it
            )
        }

    }

    private fun findCustomComponentJsonProperty(xmlTag: XmlTag): JsonProperty? {
        val tagName = xmlTag.name
        val wxmlPsiFile = xmlTag.containingFile
        val jsonFile = RelateFileHolder.JSON.findFile(wxmlPsiFile.originalFile) as? JsonFile ?: return null
        // 找到usingComponents的配置
        val usingComponentItems = mutableListOf<JsonProperty>().apply {
            ComponentJsonUtils.getUsingComponentItems(jsonFile)?.let {
                this.addAll(it)
            }
            AppJsonUtils.findUsingComponentItems(xmlTag.project)?.let {
                this.addAll(it)
            }
        }
        return usingComponentItems.find {
            it.name == tagName
        }
    }
}