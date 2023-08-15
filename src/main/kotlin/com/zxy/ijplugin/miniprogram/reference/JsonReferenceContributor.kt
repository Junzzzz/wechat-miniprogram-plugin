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

import com.intellij.json.psi.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.TagNameReference
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType

class JsonReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonProperty::class.java), object :
            PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                element as JsonProperty
                val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(element.containingFile)
                    ?: return PsiReference.EMPTY_ARRAY
                if (wxmlPsiFile.findChildrenOfType<XmlTag>().filter {
                        it.name == element.name
                    }.any { xmlTag ->
                        xmlTag.references.any {
                            it is TagNameReference && it.resolve() == element
                        }
                    }) {
                    return arrayOf(JsonRegistrationReference(element))
                }
                return PsiReference.EMPTY_ARRAY
            }
        })

        // 小程序的json配置文件中的usingComponents配置
        // 解析被引入的组件的路径
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    psiElement: PsiElement, processingContext: ProcessingContext
                ): Array<out PsiReference> {
                    psiElement as JsonStringLiteral
                    // 确定此元素是正确的usingComponents配置项的值
                    val parent = psiElement.parent
                    if (parent is JsonProperty && parent.value == psiElement) {
                        val usingComponentsProperty = parent.parent?.parent
                        if (usingComponentsProperty is JsonProperty && usingComponentsProperty.name == "usingComponents") {
                            // 找到usingComponents配置
                            val wrapObject = usingComponentsProperty.parent
                            if (wrapObject is JsonObject && wrapObject.parent is JsonFile) {
                                return ComponentFileReferenceSet(psiElement).allReferences
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )

        // 小程序的app.json配置文件中的pages配置项
        // 解析被注册的page的路径
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    psiElement: PsiElement, processingContext: ProcessingContext
                ): Array<out PsiReference> {
                    psiElement as JsonStringLiteral
                    val parentArray = psiElement.parent
                    if (RelateFileHolder.JSON.findAppFile(
                            psiElement.project
                        ) == psiElement.containingFile.originalFile
                    ) {
                        // 确定是app.json
                        if (parentArray is JsonArray) {
                            val parentProperty = parentArray.parent
                            if (parentProperty is JsonProperty && parentProperty.name == "pages") {
                                val rootObject = parentProperty.parent
                                if (rootObject is JsonObject && rootObject.parent is JsonFile) {
                                    // 确定是app.json下的pages配置项
                                    return ComponentFileReferenceSet(psiElement).allReferences
                                }
                            }
                        }
                    }
                    return PsiReference.EMPTY_ARRAY
                }

            })
    }
}

class JsonRegistrationReference(jsonProperty: JsonProperty) :
    PsiPolyVariantReferenceBase<JsonProperty>(jsonProperty, jsonProperty.nameElement.textRangeInParent) {
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val wxmlPsiFile = RelateFileHolder.MARKUP.findFile(element.containingFile) ?: return ResolveResult.EMPTY_ARRAY
        return wxmlPsiFile.findChildrenOfType<XmlTag>().filter {
            it.name == element.name
        }.filter { xmlTag ->
            xmlTag.references.any {
                it is TagNameReference && it.resolve() == element
            }
        }.map {
            PsiElementResolveResult(it)
        }.toTypedArray()
    }

}

