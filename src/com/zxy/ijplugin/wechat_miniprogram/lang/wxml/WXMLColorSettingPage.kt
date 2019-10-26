package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSSyntaxHighlighter
import javax.swing.Icon

class WXMLColorSettingPage : ColorSettingsPage {
    override fun getHighlighter(): SyntaxHighlighter {
        return WXSSSyntaxHighlighter()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? {
        return null
    }

    override fun getIcon(): Icon? {
        return WXMLIcons.FILE
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
                AttributesDescriptor("Tag",WXMLSyntaxHighlighter.WXML_TAG),
                AttributesDescriptor("Tag name",WXMLSyntaxHighlighter.WXML_TAG_NAME),
                AttributesDescriptor("Attribute name",WXMLSyntaxHighlighter.WXML_ATTRIBUTE_NAME),
                AttributesDescriptor("Attribute value",WXMLSyntaxHighlighter.WXML_ATTRIBUTE_VALUE),
                AttributesDescriptor("Comment",WXMLSyntaxHighlighter.WXML_COMMENT)
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return WXMLLanguage.INSTANCE.displayName
    }

    override fun getDemoText(): String {
        return """
            <scroll-view scroll-y class="scroll-view"  bindscrolltolower="onScrollBottom">
                <block wx:for="{{orders}}" wx:key="id">
                    <view wx:if="{{index!==0}}" class="divider"></view>
                    <order-item order="{{item}}" class="{{index===0?'first':''}}  {{index===orders.length-1?'last':''}}" catchtap="onOrderItemTap" data-id="{{item.id}}"></order-item>
                </block>
            </scroll-view>
        """.trimIndent()
    }
}