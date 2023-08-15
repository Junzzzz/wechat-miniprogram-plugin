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

package com.zxy.ijplugin.miniprogram.utils

import com.intellij.json.psi.JsonProperty
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile

object ComponentWxmlUtils {

    /**
     * 判断一个wxml属性是否是自定义组件配置的externalClasses
     */
    fun isExternalClassesAttribute(attribute: XmlAttribute): Boolean {
        return attribute.parent?.let { this.findCustomComponentDefinitionJsFile(it) }?.let {
            ComponentJsUtils.findComponentExternalClasses(it)?.contains(attribute.name)
        } == true
    }

    /**
     * 获取一个WXMLTag作为自定义组件的wxml文件
     */
    fun findCustomComponentDefinitionWxmlFile(xmlTag: XmlTag): WXMLPsiFile? {
        return this.findCustomComponentDefinitionFiles(xmlTag)?.find { it is WXMLPsiFile } as? WXMLPsiFile
    }

    /**
     * 获取一个WXMLTag作为自定义组件的js文件
     */
    fun findCustomComponentDefinitionJsFile(xmlTag: XmlTag): JSFile? {
        return this.findCustomComponentDefinitionFiles(xmlTag)?.find { it is JSFile } as? JSFile
    }

    fun findCustomComponentDefinitionJsFile(jsonProperty: JsonProperty): JSFile? {
        return this.findCustomComponentDefinitionFiles(jsonProperty)?.find { it is JSFile } as? JSFile
    }

    private fun findCustomComponentDefinitionFiles(xmlTag: XmlTag): List<PsiFile>? {
        // 先找到json文件的定义
        val componentNameJsonProperty = xmlTag.descriptor?.declaration as? JsonProperty
        // 解析其路径可以找到自定义组件的位置
        return componentNameJsonProperty?.let { this.findCustomComponentDefinitionFiles(it) }
    }

    private fun findCustomComponentDefinitionFiles(jsonProperty: JsonProperty): List<PsiFile>? {
        // 解析其路径可以找到自定义组件的位置
        val lastComponentPathReference = jsonProperty.value?.references?.lastOrNull() as? PsiFileReference
        return lastComponentPathReference?.multiResolve(
            false
        )?.mapNotNull { it.element as? PsiFile }
    }

}