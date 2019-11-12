package com.zxy.ijplugin.wechat_miniprogram.reference.manipulator

import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLElementFactory

class WXMLStringTextManipulator : MyAbstractElementManipulator<WXMLStringText>({
    project, elementText ->
    WXMLElementFactory.createStringText(
            project,elementText
    )
})