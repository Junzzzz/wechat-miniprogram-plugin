package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.reference.PathAttribute

/**
 * 对wxml 中的import和include标签的src属性进行路径检查
 */
class WXMLInvalidImportInspection : WXMLElementPathAttributeInspection(
        arrayOf(
                PathAttribute("import", "src"),
                PathAttribute("include", "src")
        ), WXMLFileType.INSTANCE
)