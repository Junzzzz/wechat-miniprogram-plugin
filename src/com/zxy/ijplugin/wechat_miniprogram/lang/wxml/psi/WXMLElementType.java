package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi;

import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class WXMLElementType extends IElementType {
    public WXMLElementType(@NotNull @NonNls String debugName) {
        super(debugName, WXMLLanguage.INSTANCE);
    }

}