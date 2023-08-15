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

package com.zxy.ijplugin.miniprogram.lang.wxml.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import javax.swing.Icon

class WXMLAttributeValueImpl : XmlAttributeValueImpl() {

    override fun getPresentation(): ItemPresentation? {
        val presentation = super.getPresentation()
        val value = this.value
        return object : ItemPresentation {
            override fun getLocationString(): String? {
                return presentation?.locationString
            }

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.FileTypes.Xml
            }

            override fun getPresentableText(): String? {
                return value
            }

        }
    }

}