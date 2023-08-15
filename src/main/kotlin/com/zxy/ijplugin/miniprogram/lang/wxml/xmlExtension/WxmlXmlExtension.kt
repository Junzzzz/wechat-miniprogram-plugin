/*
 *     Copyright (c) [2019] [zxy]
 *     [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *     You can use this software according to the terms and conditions of the Mulan PSL v1.
 *     You may obtain a copy of Mulan PSL v1 at:
 *         http://license.coscl.org.cn/MulanPSL
 *     THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *     PURPOSE.
 *     See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.miniprogram.lang.wxml.xmlExtension

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.SchemaPrefix
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.DefaultXmlExtension
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.isJsTypeAttribute
import com.zxy.ijplugin.miniprogram.qq.QMLFileType

class WxmlXmlExtension : DefaultXmlExtension() {

    companion object {
        val selfClosingTagNames = arrayOf("input", "textarea", "image", "icon", "import", "include")
    }

    override fun isAvailable(file: PsiFile): Boolean {
        return file.fileType == WXMLFileType.INSTANCE || (file.project.isQQContext() && file.fileType == QMLFileType.INSTANCE)
    }

    override fun getAttributeValuePresentation(
        tag: XmlTag?,
        attributeName: String,
        defaultAttributeQuote: String
    ): AttributeValuePresentation {
        if (tag !== null) {
            val attributeDescription = WXMLMetadata.findElementAttributeDescription(tag, attributeName)
            if (attributeDescription != null) {
                // 如果此属性是 number object 或 array
                if (attributeDescription.isJsTypeAttribute()) {
                    return object : AttributeValuePresentation {
                        override fun showAutoPopup(): Boolean {
                            return attributeDescription.enums.isNotEmpty()
                        }

                        override fun getPostfix(): String {
                            return "}}$defaultAttributeQuote"
                        }

                        override fun getPrefix(): String {
                            return "$defaultAttributeQuote{{"
                        }
                    }
                }
            }
        }
        return super.getAttributeValuePresentation(tag, attributeName, defaultAttributeQuote)
    }

    /**
     * 不使用xml默认的InsertHandler
     */
    override fun useXmlTagInsertHandler(): Boolean {
        return false
    }

    /**
     *  部分标签可以自动关闭
     */
    override fun isSelfClosingTagAllowed(tag: XmlTag): Boolean {
        return selfClosingTagNames.contains(tag.name)
    }

    override fun getPrefixDeclaration(context: XmlTag, namespacePrefix: String?): SchemaPrefix? {
        if (namespacePrefix != null && (namespacePrefix == "wx"
                    || namespacePrefix == "bind"
                    || namespacePrefix == "catch"
                    || namespacePrefix == "model"
                    || namespacePrefix == "mark"
                    || namespacePrefix == "mut-bind"
                    || (context.containingFile.fileType == QMLFileType.INSTANCE && namespacePrefix == "qq"))
        ) {
            findAttributeSchema(context, namespacePrefix)
                ?.let { return it }
        }
        return super.getPrefixDeclaration(context, namespacePrefix)
    }

    private fun findAttributeSchema(context: XmlTag, namespacePrefix: String): SchemaPrefix? {
        return context.attributes
            .find { it.name.startsWith("$namespacePrefix:") }
            ?.let { SchemaPrefix(it, TextRange.create(0, namespacePrefix.length), namespacePrefix) }
    }


}