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

package com.zxy.ijplugin.miniprogram.qq

import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.icons.WechatMiniProgramIcons
import javax.swing.Icon

class QMLFileType : WXMLFileType() {

    companion object {
        @JvmField
        val INSTANCE = QMLFileType()
    }

    override fun getName(): String {
        return "QML"
    }

    override fun getDisplayName(): String {
        return "QML"
    }

    override fun getDefaultExtension(): String {
        return "qml"
    }

    override fun getDescription(): String {
        return "QQ Markup Language"
    }

    override fun getIcon(): Icon? {
        return WechatMiniProgramIcons.QML
    }

}