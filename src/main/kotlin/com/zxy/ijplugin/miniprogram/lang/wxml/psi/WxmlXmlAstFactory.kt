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

package com.zxy.ijplugin.miniprogram.lang.wxml.psi

import com.intellij.lang.xml.XmlASTFactory
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlElementType
import com.zxy.ijplugin.miniprogram.lang.wxml.psi.impl.WXMLAttributeValueImpl

class WxmlXmlAstFactory : XmlASTFactory() {

    override fun createComposite(type: IElementType): CompositeElement? {
        if (type == XmlElementType.XML_ATTRIBUTE_VALUE) {
            return WXMLAttributeValueImpl()
        }
        return super.createComposite(type)
    }

}