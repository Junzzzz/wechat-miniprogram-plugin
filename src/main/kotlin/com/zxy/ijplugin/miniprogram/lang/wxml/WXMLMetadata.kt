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

package com.zxy.ijplugin.miniprogram.lang.wxml

import com.fasterxml.jackson.annotation.JsonCreator
import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.miniprogram.utils.*

data class WXMLElementDescription constructor(
    val name: String,
    val attributeDescriptorPresetElementAttributeDescriptors: Array<WXMLPresetElementAttributeDescription> = emptyArray(),
    val events: Array<String> = emptyArray(),
    val canOpen: Boolean = true,
    val canClose: Boolean = false,
    val description: String? = null,
    val url: String? = null,
    val definedElement: JsonStringLiteral
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLElementDescription

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class WXMLPresetElementAttributeDescription @JsonCreator constructor(
    key: String, types: Array<ValueType>, default: Any?, required: Boolean,
    enums: Array<String>, requiredInEnums: Boolean, description: String?,
    val definedElement: JsonStringLiteral
) : WXMLElementAttributeDescription(key, types, default, required, enums, requiredInEnums, description)

typealias A = WXMLPresetElementAttributeDescription
typealias T = WXMLElementAttributeDescription.ValueType

@Service
class WXMLMetadata(private val project: Project) {

    private val elementDescriptors by lazy {
        ResourceUtils.findWXMLMetaDataFile()?.let {
            PsiManager.getInstance(this.project).findFile(it) as? JsonFile
        }?.let { jsonFile ->
            val root = jsonFile.topLevelValue as? JsonObject
            // 将psi文件读取为Object
            root?.propertyList?.mapNotNull { elementJsonProperty ->
                val elementJsonPropertyValue = elementJsonProperty.value as? JsonObject ?: return@mapNotNull null
                val description = (elementJsonPropertyValue.findProperty(
                    "description"
                )?.value as? JsonStringLiteral)?.value
                val events = elementJsonPropertyValue.findStringArrayPropertyValue("events") ?: emptyArray()
                val canOpen = elementJsonPropertyValue.findBooleanPropertyValue("canOpen") ?: true
                val canClose = elementJsonPropertyValue.findBooleanPropertyValue("canClose") ?: false
                val url = elementJsonPropertyValue.findStringPropertyValue("url")
                WXMLElementDescription(
                    elementJsonProperty.name,
                    elementJsonPropertyValue.findProperty(
                        "attributeDescriptors"
                    )?.let { attributeJsonProperty ->
                        (attributeJsonProperty.value as? JsonArray)?.valueList?.mapNotNull { attributeJsonObject ->
                            if (attributeJsonObject is JsonObject) {
                                WXMLPresetElementAttributeDescription(
                                    attributeJsonObject.findStringPropertyValue("key")!!,
                                    attributeJsonObject.findStringArrayPropertyValue("library")?.map {
                                        WXMLElementAttributeDescription.ValueType.valueOf(it)
                                    }?.toTypedArray() ?: emptyArray(),
                                    attributeJsonObject.findPropertyValue("default"),
                                    attributeJsonObject.findBooleanPropertyValue("required") ?: false,
                                    attributeJsonObject.findStringArrayPropertyValue("enums") ?: emptyArray(),
                                    attributeJsonObject.findBooleanPropertyValue("requiredInEnums") ?: true,
                                    attributeJsonObject.findStringPropertyValue("description"),
                                    attributeJsonObject.findProperty("key")!!.value as JsonStringLiteral
                                )
                            } else {
                                null
                            }
                        }?.toTypedArray()
                    } ?: emptyArray(),
                    events,
                    canOpen,
                    canClose,
                    description,
                    url,
                    elementJsonProperty.nameElement as JsonStringLiteral
                )
            }
        } ?: emptyList()
    }

    companion object {

        fun getElementDescriptions(project: Project): List<WXMLElementDescription> {
            return project.getService(WXMLMetadata::class.java).elementDescriptors
        }

        private fun findElementDescription(xmlTag: XmlTag): WXMLElementDescription? {
            val tagName = xmlTag.name
            return this.getElementDescriptions(xmlTag.project).find {
                it.name == tagName
            }
        }

        fun findElementAttributeDescription(xmlTag: XmlTag, attributeName: String): WXMLElementAttributeDescription? {
            return this.findElementDescription(xmlTag)?.let { wxmlElementDescription ->
                wxmlElementDescription.attributeDescriptorPresetElementAttributeDescriptors.find {
                    it.key == attributeName
                }
            }
        }

        val COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS = arrayOf(
            WXMLElementAttributeDescription("id", arrayOf(T.STRING)),
            WXMLElementAttributeDescription("class", arrayOf(T.STRING)),
            WXMLElementAttributeDescription("style", arrayOf(T.STRING)),
            WXMLElementAttributeDescription("hidden", arrayOf(T.BOOLEAN), false),
            WXMLElementAttributeDescription("slot", arrayOf(T.STRING))
        )

        val COMMON_ELEMENT_EVENTS = arrayOf(
            "touchstart", "touchmove", "touchcancel", "touchend", "tap", "longpress", "longtap", "transitionend",
            "animationstart", "animationiteration", "animationend", "touchforcechange"
        )

        @Suppress("unused")
        val TEXT_ELEMENT_NAMES = arrayOf("text")

        @Suppress("unused")
        val ARIA_ATTRIBUTE = arrayOf(
            "aria-hidden", "aria-role", "aria-label", "aria-checked", "aria-disabled",
            "aria-describedby", "aria-expanded", "aria-haspopup", "aria-selected", "aria-required",
            "aria-orientation", "aria-valuemin", "aria-valuemax", "aria-valuenow", "aria-readonly",
            "aria-multiselectable", "aria-controls", "tabindex", "aria-labelledby", "aria-orientation",
            "aria-multiselectable", "aria-labelledby"
        )

        @Suppress("unused")
        val NATIVE_COMPONENTS = arrayOf(
            "camera", "canvas", "input", "live-player", "live-pusher", "map", "textarea", "video"
        )

    }
}

open class WXMLElementAttributeDescription(
    val key: String,
    val types: Array<ValueType> = emptyArray(),
    val default: Any? = null,
    val required: Boolean = false,
    val enums: Array<String> = emptyArray(),
    @Suppress("unused")
    val requiredInEnums: Boolean = true,
    val description: String? = null
) {
    enum class ValueType {
        STRING, NUMBER, BOOLEAN,
        COLOR, ARRAY, OBJECT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLPresetElementAttributeDescription

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}