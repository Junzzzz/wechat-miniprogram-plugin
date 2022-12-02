
package com.zxy.ijplugin.wechat_miniprogram.lang.wxml;

import com.intellij.lang.xml.XMLLanguage;

public class WXMLLanguage extends XMLLanguage {

    public static String[] EVENT_ATTRIBUTE_PREFIX_ARRAY = new String[]{"bind", "catch", "bind:", "catch:", "mut-bind", "mut-bind:"};

    public static WXMLLanguage INSTANCE = new WXMLLanguage();

    private WXMLLanguage() {
        super(XMLLanguage.INSTANCE, "WXML");
    }

}
