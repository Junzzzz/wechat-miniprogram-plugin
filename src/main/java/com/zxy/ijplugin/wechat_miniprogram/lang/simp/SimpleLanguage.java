package com.zxy.ijplugin.wechat_miniprogram.lang.simp;

import com.intellij.lang.Language;

public class SimpleLanguage extends Language {
  public static final SimpleLanguage INSTANCE = new SimpleLanguage();

  private SimpleLanguage() {
    super("Simple");
  }
}