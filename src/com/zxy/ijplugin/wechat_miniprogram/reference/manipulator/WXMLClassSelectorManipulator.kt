package com.zxy.ijplugin.wechat_miniprogram.reference.manipulator

import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSClassSelector
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory

class WXMLClassSelectorManipulator :
        MyAbstractElementManipulator<WXSSClassSelector>({ project, elementText ->
            WXSSElementFactory.createClassSelector(
                    project, elementText
            )
        })