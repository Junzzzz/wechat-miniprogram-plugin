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
                AttributesDescriptor("Class", WXSSSyntaxHighlighter.WXSS_CLASS),
                AttributesDescriptor("Bad character", WXSSSyntaxHighlighter.WXSS_BAD_CHARACTER),
                AttributesDescriptor("Braces", WXSSSyntaxHighlighter.WXSS_BRACKET),
                AttributesDescriptor("Property name", WXSSSyntaxHighlighter.WXSS_ATTRIBUTE_NAME),
                AttributesDescriptor("Class selector", WXSSSyntaxHighlighter.WXSS_CLASS_SELECTOR),
                AttributesDescriptor("Class name", WXSSSyntaxHighlighter.WXSS_CLASS),
                AttributesDescriptor("Id", WXSSSyntaxHighlighter.WXSS_ID),
                AttributesDescriptor("Property value", WXSSSyntaxHighlighter.WXSS_ATTRIBUTE_VALUE_BASIC),
                AttributesDescriptor("Colon", WXSSSyntaxHighlighter.WXSS_COLON),
                AttributesDescriptor("Parenthesis", WXSSSyntaxHighlighter.WXSS_PARENTHESES),
                AttributesDescriptor("Color", WXSSSyntaxHighlighter.WXSS_COLOR),
                AttributesDescriptor("Function", WXSSSyntaxHighlighter.WXSS_FUNCTION),
                AttributesDescriptor("Semicolon", WXSSSyntaxHighlighter.WXSS_SEMICOLON),
                AttributesDescriptor("String", WXSSSyntaxHighlighter.WXSS_STRING),
                AttributesDescriptor("Tag name", WXSSSyntaxHighlighter.WXSS_ELEMENT),
                AttributesDescriptor("Pseudo selector", WXSSSyntaxHighlighter.WXSS_PSEUDO)
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
            @import "abcddsadas";
            @font-face {
                
            }
            
            .container::before{
                margin-top: 20rpx;
                padding: 30rpx;
                box-sizing: border-box;
                background-color: white;
                border-radius: 30;
                border-radius: 30,232,value
            }
            
            .content{
                display: flex;
                flex-direction: row;
            
            }
            
            .cover{
                width: 200rpx;
                height: 200rpx;
            }
            
            .info{
                margin-left: 30rpx;
            }
            
            .info .attrs{
                display: flex;
                flex-direction: row;
                flex-wrap: wrap;
                color: #909399;
                font-size: 28rpx;
            }
            
            .info .attrs .attr{
                margin-right: 12rpx;
            }
            
            .info .name{
                font-size: 32rpx;
                font-weight: 400;
            }
        """.trimIndent()
    }
}