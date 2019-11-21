package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.zxy.ijplugin.wechat_miniprogram.lang.wxs.WXSFileType
import com.zxy.ijplugin.wechat_miniprogram.reference.PathAttribute

/**
 * 检查wxml中的wxs标签的导入是否有效
 */
class WxmlWxsModuleImportInspection :
        WXMLElementPathAttributeInspection(arrayOf(PathAttribute("wxs", "src")), WXSFileType.INSTANCE)
