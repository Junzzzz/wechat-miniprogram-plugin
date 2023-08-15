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

import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlElementDescriptor
import com.intellij.xml.XmlElementsGroup
import com.intellij.xml.XmlNSDescriptor
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.WXMLUtils

abstract class WXMLBasicElementDescriptor : XmlElementDescriptor {
    override fun getContentType(): Int {
        return XmlElementDescriptor.CONTENT_TYPE_ANY
    }

    override fun getTopGroup(): XmlElementsGroup? {
        return null
    }

    override fun getNSDescriptor(): XmlNSDescriptor? {
        return null
    }

    override fun getElementDescriptor(child: XmlTag, context: XmlTag): XmlElementDescriptor? {
        return XmlElementDescriptorProvider.EP_NAME.findExtension(WXMLElementDescriptorProvider::class.java)
            ?.getDescriptor(child)
    }

    override fun getAttributeDescriptor(attributeName: String, context: XmlTag?): XmlAttributeDescriptor? {
        if (WXMLUtils.isCommonAttributes(attributeName) || WXMLUtils.likeEventAttribute(attributeName)) {
            // 公共属性或公共事件
            return AnyXmlAttributeDescriptor(attributeName)
        }
        return null
    }

    override fun getAttributeDescriptor(attribute: XmlAttribute?): XmlAttributeDescriptor? {
        return this.getAttributeDescriptor(attribute?.name ?: return null, attribute.parent)
    }

    override fun getAttributesDescriptors(p0: XmlTag?): Array<XmlAttributeDescriptor> {
        return emptyArray()
    }
}