package com.zxy.ijplugin.wechat_miniprogram.lang.expr.psi;

import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class WXMLExprElementType extends IElementType {
    public WXMLExprElementType(@NotNull @NonNls String debugName) {
        super(debugName, WXMLLanguage.INSTANCE);
    }

}