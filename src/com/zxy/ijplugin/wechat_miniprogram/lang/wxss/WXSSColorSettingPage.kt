package com.zxy.ijplugin.wechat_miniprogram.lang.wxss

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class WXSSColorSettingPage : ColorSettingsPage {
    override fun getHighlighter(): SyntaxHighlighter {
        return WXSSSyntaxHighlighter()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }

    override fun getIcon(): Icon? {
        return WXSSIcons.FILE
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
                AttributesDescriptor("Class",WXSSSyntaxHighlighter.WXSS_CLASS),
                AttributesDescriptor("Bad character",WXSSSyntaxHighlighter.WXSS_BAD_CHARACTER),
                AttributesDescriptor("Braces",WXSSSyntaxHighlighter.WXSS_BRACKET)
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return WXSSLanguage.INSTANCE.displayName
    }

    override fun getDemoText(): String {
        return """
            @import "manual.css";

            @font-face {
              font-family: DroidSans;
              src: url(DroidSans.ttf);
              unicode-range: U+000-5FF, U+1e00-1fff, U+2000-2300;
            }
            
            h1.mystyle:lang(en) {
              color:blue; /* TODO: change THIS to yellow for next version! */
              border:rgb(255,0,0);
              background-color: #FAFAFA;
              background:url(hello.jpg) !important;
            }
            
            div > p, p ~ ul, input [type="radio"] {
              color: green;
              width: 80%;
            }
            
            #header:after {
              color: red;
            }
            
            &!
        """.trimIndent()
    }
}