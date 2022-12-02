
package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi;

import com.intellij.psi.tree.IElementType;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class WXSSElementType extends IElementType {
    public WXSSElementType(@NotNull @NonNls String debugName) {
        super(debugName, WXSSLanguage.INSTANCE);
    }

}