// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.zxy.ijplugin.wechat_miniprogram.lang.expr.highlighting

import com.intellij.lang.javascript.highlighting.TypeScriptHighlighter
import com.zxy.ijplugin.wechat_miniprogram.lang.expr.WxmlJsLanguage


class WxmlJsSyntaxHighlighter : TypeScriptHighlighter(WxmlJsLanguage.INSTANCE.optionHolder, false)
