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

package com.zxy.ijplugin.miniprogram.lang.wxml.tag

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlTagNameProvider
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.miniprogram.utils.ComponentJsonUtils
import com.zxy.ijplugin.miniprogram.utils.getPathRelativeToRootRemoveExt


class WXMLTagNameProvider : XmlTagNameProvider {
    override fun addTagNameVariants(
        elements: MutableList<LookupElement>, tag: XmlTag, prefix: String
    ) {
        if (tag.language == WXMLLanguage.INSTANCE) {
            // 自带组件
            elements.addAll(WXMLMetadata.getElementDescriptions(tag.project).map {
                LookupElementBuilder.create(it.name)
                    .withInsertHandler(WXMLTagInsertHandler.INSTANCE)
            })

            // 自定义组件
            val currentJsonPsiFile = RelateFileHolder.JSON.findFile(tag.containingFile.originalFile)
            if (currentJsonPsiFile !== null) {
                if (currentJsonPsiFile is JsonFile) {

                    // 项目中所有的组件配置文件
                    val jsonFiles = ComponentJsonUtils.getAllComponentConfigurationFile(
                        currentJsonPsiFile.project
                    ).filter {
                        it != currentJsonPsiFile
                    }
                    val usingComponentsObjectValue = ComponentJsonUtils.getUsingComponentPropertyValue(
                        currentJsonPsiFile
                    )
                    val appJsonUsingComponentsObjectValue = AppJsonUtils.findUsingComponentsValue(tag.project)
                    // 从app.json 和 相关的json文件中手机usingComponents
                    val usingComponentItems = mutableListOf<JsonProperty>().apply {
                        usingComponentsObjectValue?.propertyList?.let {
                            this.addAll(it)
                        }
                        appJsonUsingComponentsObjectValue?.propertyList?.let {
                            this.addAll(it)
                        }
                    }
                    val usingComponentMap = usingComponentItems
                        .associateBy({ jsonProperty ->
                            (jsonProperty.value?.references?.lastOrNull() as? PsiPolyVariantReferenceBase<*>)?.multiResolve(
                                false
                            )?.map {
                                it.element
                            }?.find {
                                it is JsonFile
                            }?.let {
                                it as JsonFile
                            }
                        }, { it.name })
                        .filter { it.key != null }
                    elements.addAll(jsonFiles.mapNotNull { jsonFile ->
                        val configComponentName = usingComponentMap[jsonFile]
                        val componentPath = (jsonFile.virtualFile.getPathRelativeToRootRemoveExt(
                            jsonFile.project
                        ) ?: return@mapNotNull null)
                        if (configComponentName == null) {
                            // 没有注册的组件
                            LookupElementBuilder.create(jsonFile.virtualFile.nameWithoutExtension)
                                .withTypeText(componentPath)
                                .withInsertHandler { _, _ ->
                                    // 在配置文件中注册组件
                                    usingComponentsObjectValue?.let {
                                        ComponentJsonUtils.registerComponent(usingComponentsObjectValue, jsonFile)
                                    }
                                }
                        } else {
                            LookupElementBuilder.create(configComponentName)
                                .withTypeText(componentPath)
                        }
                    })
                }
            }
        }
    }

}
