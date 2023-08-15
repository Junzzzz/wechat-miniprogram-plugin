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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTokenType
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.utils.ComponentWxmlUtils
import com.zxy.ijplugin.miniprogram.utils.toTextRange

class WXMLReferenceContributor : PsiReferenceContributor() {

    companion object {
        // 描述可以被解析为路径的元素的属性
        private val PATH_ATTRIBUTES = arrayOf(
                PathAttribute("wxs", "src"),
                PathAttribute("image", "src", true),
                PathAttribute("import", "src"),
                PathAttribute("include", "src"),
                PathAttribute("cover-image", "src", true)
        )

        private fun findPathAttribute(tagName: String, attributeName: String): PathAttribute? {
            return PATH_ATTRIBUTES.find {
                it.tagName == tagName && it.attributeName == attributeName
            }
        }
    }

    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        // 解析wxml中的id
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLocalName("id").withLanguage(WXMLLanguage.INSTANCE),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        if (psiElement is XmlAttributeValue) {
                            val valueTokens = psiElement.children.filter {
                                it.elementType == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN
                            }

                            if (valueTokens.size == 1) {
                                // 这个字符串内容必须在 id中 并且只有一个ValueToken
                                // 没有双括号
                                return arrayOf(WXMLIdReference(psiElement, valueTokens[0].textRangeInParent))
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析wxml中的class
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLanguage(WXMLLanguage.INSTANCE)
                        .inFile(
                                PlatformPatterns.psiFile(WXMLPsiFile::class.java)
                        ),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement is XmlAttributeValue
                        val attribute = psiElement.parent as? XmlAttribute
                        if (attribute != null && (attribute.name == "class" || ComponentWxmlUtils.isExternalClassesAttribute(
                                        attribute
                                ))) {
                            val findResults = Regex("[_\\-a-zA-Z][_\\-a-zA-Z0-9]+").findAll(psiElement.text)
                            return findResults.map { matchResult ->
                                WXMLClassReference(
                                        psiElement, matchResult.range.toTextRange()
                                )
                            }.toList().toTypedArray()
                        }
                        return PsiReference.EMPTY_ARRAY
                    }

                }
        )

        // 解析wxml中的路径属性
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLanguage(WXMLLanguage.INSTANCE),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, processingContext: ProcessingContext
                    ): Array<out PsiReference> {
                        psiElement is XmlAttributeValue
                        val attribute = psiElement.parent as? XmlAttribute ?: return PsiReference.EMPTY_ARRAY
                        val tag = attribute.parent
                        val pathAttribute = findPathAttribute(
                                tag.name, attribute.name
                        )
                        if (pathAttribute != null) {
                            // 这个属性是可解析为路径的
                            return object : FileReferenceSet(psiElement) {
                                override fun isSoft(): Boolean {
                                    return pathAttribute.isSoftReference
                                }
                            }.allReferences
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析wxml中的template.is属性
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLanguage(WXMLLanguage.INSTANCE).withLocalName("is").withSuperParent(
                        2, XmlPatterns.xmlTag().withLocalName("template")
                ),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as XmlAttributeValue
                        return arrayOf(WXMLTemplateIsAttributeReference(psiElement))
                    }
                }
        )

        // 解析wxml中的template.name属性
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLanguage(WXMLLanguage.INSTANCE).withLocalName("name")
                        .withSuperParent(
                                2, XmlPatterns.xmlTag().withLocalName("template")
                        ),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as XmlAttributeValue
                        return arrayOf(WXMLTemplateNameAttributeReference(psiElement))
                    }
                }
        )

        // 解析wxml元素的slot属性
        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue().withLanguage(WXMLLanguage.INSTANCE).withLocalName("slot"),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as XmlAttributeValue
                        val wxmlAttribute = psiElement.parent as? XmlAttribute
                        if (wxmlAttribute != null) {
                            return arrayOf(WXMLNamedSlotReference(psiElement))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        psiReferenceRegistrar.registerReferenceProvider(
                XmlPatterns.xmlAttribute().withLanguage(WXMLLanguage.INSTANCE),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            element: PsiElement, context: ProcessingContext
                    ): Array<PsiReference> {
                        element as XmlAttribute
                        if (element.name.startsWith("model:")) {
                            return arrayOf(MarkupTwoWayBindingReference(element))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

    }

}