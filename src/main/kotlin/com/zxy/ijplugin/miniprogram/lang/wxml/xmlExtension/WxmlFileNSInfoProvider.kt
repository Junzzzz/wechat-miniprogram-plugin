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

package com.zxy.ijplugin.miniprogram.lang.wxml.xmlExtension

import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlFileNSInfoProvider
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile

class WxmlFileNSInfoProvider : XmlFileNSInfoProvider {
    override fun getDefaultNamespaces(p0: XmlFile): Array<Array<String>>? {
        return null
    }

    override fun overrideNamespaceFromDocType(p0: XmlFile): Boolean {
        return p0 is WXMLPsiFile
    }
}