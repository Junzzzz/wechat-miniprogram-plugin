/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.fasterxml.jackson.annotation.JsonCreator
import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.wechat_miniprogram.utils.*

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