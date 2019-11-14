package com.zxy.ijplugin.wechat_miniprogram.lang.wxml;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WXMLLanguage extends HTMLLanguage {

    public static String[] EVENT_ATTRIBUTE_PREFIX_ARRAY = new String[]{"bind", "catch", "bind:", "catch:"};

    public static WXMLLanguage INSTANCE = new WXMLLanguage();

    private WXMLLanguage() {
        super(WXMLLanguage.INSTANCE,"WXML");
    }

    @Nullable
    public LanguageFileType getAssociatedFileType() {
        return WXMLFileType.INSTANCE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "WXML";
    }
}
