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

package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.stream
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLElementAttributeDescription
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.utils.*

class WXMLCompletionContributor : CompletionContributor() {

    init {
        this.extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(WXMLTypes.ATTRIBUTE_NAME).afterLeafSkipping(
                        PlatformPatterns.alwaysFalse<Any>(), PlatformPatterns.instanceOf(PsiWhiteSpace::class.java)
                ).inside(WXMLElement::class.java),
                WXMLAttributeCompletionProvider()
        )
        this.extend(
                CompletionType.BASIC, PlatformPatterns.psiElement(WXMLTypes.STRING_CONTENT),
                /**
                 * 对WXML的属性值提供完成
                 */
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            completionParameters: CompletionParameters, processingContext: ProcessingContext,
                            completionResultSet: CompletionResultSet
                    ) {
                        val stringContentElement = completionParameters.originalFile.findElementAt(
                                completionParameters.offset
                        )
                        // 对WXML的枚举属性值提供完成
                        val wxmlElement = PsiTreeUtil.getParentOfType(
                                stringContentElement, WXMLElement::class.java
                        ) ?: return
                        val wxmlAttribute = PsiTreeUtil.findChildOfType(wxmlElement, WXMLAttribute::class.java)!!
                        val tagName = wxmlElement.tagName
                        val attribute = WXMLMetadata.getElementDescriptions(
                                wxmlElement.project
                        ).stream().filter { it.name == tagName }
                                .findFirst()
                                .map { it.attributeDescriptorPresetElementAttributeDescriptors }
                                .orElse(emptyArray())
                                .stream()
                                .filter { it.key == wxmlAttribute.name }
                                .findFirst()
                                .orElse(null) ?: return
                        if (wxmlAttributeIsEnumerable(attribute)) {
                            completionResultSet.addAllElements(attribute.enums.map {
                                LookupElementBuilder.create(it)
                            })
                        }

                    }
                }
        )

        // 输入一个元素开始符号后
        // 自动完成标签名以及必填的属性
        this.extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().afterLeafSkipping(
                        PlatformPatterns.alwaysFalse<Any>(),
                        PlatformPatterns.psiElement(WXMLTypes.START_TAG_START)
                ), WXMLTagNameCompletionProvider()
        )

        this.extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().afterLeafSkipping(
                        PlatformPatterns.alwaysFalse<Any>(),
                        PlatformPatterns.psiElement(WXMLTypes.TAG_NAME)
                ),
                object : WXMLTagNameCompletionProvider() {
                    override fun getPrefix(completionParameters: CompletionParameters): String? {
                        return (completionParameters.position.parent.prevSibling as? WXMLTag)?.getTagName()
                    }
                }
        )
    }

}

private fun wxmlAttributeIsEnumerable(
        attributePresetElementAttributeDescription: WXMLElementAttributeDescription
) = attributePresetElementAttributeDescription.enums.isNotEmpty() && attributePresetElementAttributeDescription.types.size == 1 && attributePresetElementAttributeDescription.types[0] == WXMLElementAttributeDescription.ValueType.STRING && attributePresetElementAttributeDescription.requiredInEnums

open class WXMLTagNameCompletionProvider : CompletionProvider<CompletionParameters>() {

    open fun getPrefix(completionParameters: CompletionParameters): String? {
        return null
    }

    override fun addCompletions(
            completionParameters: CompletionParameters, p1: ProcessingContext,
            completionResultSet: CompletionResultSet
    ) {
        val cloneCompletionResultSet = getPrefix(completionParameters)?.let {
            completionResultSet.withPrefixMatcher(it)
        } ?: completionResultSet

        // 获取所有组件名称
        cloneCompletionResultSet.addAllElements(
                WXMLMetadata.getElementDescriptions(
                        completionParameters.position.project
                ).map { wxmlElementDescriptor ->
                    val requiredElements = wxmlElementDescriptor.attributeDescriptorPresetElementAttributeDescriptors.filter {
                        it.required
                    }
                    if (requiredElements.isEmpty()) {
                        LookupElementBuilder.create(wxmlElementDescriptor.name)
                    } else {
                        LookupElementBuilder.create(wxmlElementDescriptor.name)
                                .withInsertHandler { insertionContext, _ ->
                                    val editor = insertionContext.editor
                                    val offset = editor.caretModel.offset
                                    var result = ""
                                    var afterOffset: Int? = null
                                    requiredElements.forEach {
                                        val key = it.key
                                        when {
                                            WXMLAttributeCompletionProvider.isDoubleBraceForInsert(
                                                    it
                                            ) -> {
                                                result += " $key=\"{{}}\""
                                                if (afterOffset == null) {
                                                    afterOffset = offset + result.length - 3
                                                }
                                            }
                                            WXMLAttributeCompletionProvider.isOnlyNameForInsert(it) -> {
                                                result += " $key"
                                            }
                                            else -> {
                                                result += " $key=\"\""
                                                if (afterOffset == null) {
                                                    afterOffset = offset + result.length - 1
                                                }
                                            }
                                        }
                                    }
                                    if (afterOffset == null) {
                                        afterOffset = offset + result.length
                                    }
                                    insertionContext.document.insertString(offset, result)
                                    if (afterOffset != null) {
                                        editor.caretModel.moveToOffset(afterOffset!!)
                                    }
                                }
                    }
                })
        // 自定义组件
        val currentJsonFile = findRelateFile(completionParameters.originalFile.virtualFile, RelateFileType.JSON)
        if (currentJsonFile !== null) {
            val project = completionParameters.position.project
            val psiManager = PsiManager.getInstance(project)
            val currentJsonPsiFile = psiManager.findFile(currentJsonFile)
            if (currentJsonPsiFile != null && currentJsonPsiFile is JsonFile) {

                // 项目中所有的组件配置文件
                val jsonFiles = ComponentJsonUtils.getAllComponentConfigurationFile(
                        project
                ).filter {
                    it != currentJsonPsiFile
                }
                val usingComponentsObjectValue = ComponentJsonUtils.getUsingComponentPropertyValue(
                        currentJsonPsiFile
                )
                val appJsonUsingComponentsObjectValue = AppJsonUtils.findUsingComponentsValue(project)
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
                cloneCompletionResultSet.addAllElements(jsonFiles.mapNotNull { jsonFile ->
                    val configComponentName = usingComponentMap[jsonFile]
                    val componentPath = (jsonFile.virtualFile.getPathRelativeToRootRemoveExt(
                            project
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

/**
 * 对WXML属性名称提供完成
 */
class WXMLAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
    companion object {
        val WX_ATTRIBUTES = arrayOf("wx:for", "wx:elif", "wx:else", "wx:key", "wx:if")

        const val NO_VALUE_ATTRIBUTE = "wx:elif"
        /**
         * 忽略公共的属性的标签名
         */
        val IGNORE_COMMON_ATTRIBUTE_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include", "slot")
        /**
         * 忽略wx属性的标签名
         */
        val IGNORE_WX_ATTRIBUTE_TAG_NAMES = arrayOf("template", "wxs", "import", "include")
        /**
         * 忽略公共事件的标签名
         */
        val IGNORE_COMMON_EVENT_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include", "slot")

        internal fun isDoubleBraceForInsert(
                wxmlPresetElementAttributeDescription: WXMLElementAttributeDescription
        ): Boolean {
            return wxmlPresetElementAttributeDescription.types.contains(
                    WXMLElementAttributeDescription.ValueType.NUMBER
            ) && !wxmlPresetElementAttributeDescription.types.contains(
                    WXMLElementAttributeDescription.ValueType.STRING
            )
        }

        internal fun isOnlyNameForInsert(
                wxmlPresetElementAttributeDescription: WXMLElementAttributeDescription
        ): Boolean {
            return wxmlPresetElementAttributeDescription.types.contains(
                    WXMLElementAttributeDescription.ValueType.BOOLEAN
            ) && wxmlPresetElementAttributeDescription.default == false
        }
    }

    /**
     * 在插入属性名称时
     * 额外插入双括号
     */
    class DoubleBraceInsertHandler : InsertHandler<LookupElement> {
        override fun handleInsert(insertionContext: InsertionContext, p1: LookupElement) {
            // 额外插入 [=""]
            // 额外插入 [="{{}}"]
            val editor = insertionContext.editor
            val offset = editor.caretModel.offset
            insertionContext.document.insertString(offset, "=\"{{}}\"")
            editor.caretModel.moveToOffset(offset + 4)
        }

    }

    /**
     * 在插入属性名称之前
     * 额外插入双引号
     * @param autoPopup 完成之后是否立即唤醒自动完成控制器
     */
    class DoubleQuotaInsertHandler(private val autoPopup: Boolean = false) : InsertHandler<LookupElement> {
        override fun handleInsert(insertionContext: InsertionContext, p1: LookupElement) {
            // 额外插入 [=""]
            val editor = insertionContext.editor
            val offset = editor.caretModel.offset
            insertionContext.document.insertString(offset, "=\"\"")
            editor.caretModel.moveToOffset(offset + 2)
            if (autoPopup) {
                AutoPopupController.getInstance(insertionContext.project)
                        .autoPopupMemberLookup(insertionContext.editor, null)
            }
        }

    }

    override fun addCompletions(
            completionParameters: CompletionParameters, processingContext: ProcessingContext,
            completionResultSet: CompletionResultSet
    ) {
        // 根据组件名称进行完成
        val wxmlElement = PsiTreeUtil.getParentOfType(
                completionParameters.position, WXMLElement::class.java
        )
        val tagName = wxmlElement!!.tagName

        val wxmlAttributeNames = PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java)
                .map(WXMLAttribute::getName)

        val elementDescriptor = WXMLMetadata.getElementDescriptions(completionParameters.position.project)
                .find { it.name == tagName }
        if (elementDescriptor != null) {
            // 自带组件
            val attributes = elementDescriptor.attributeDescriptorPresetElementAttributeDescriptors
                    // 过滤掉元素上已存在的类型
                    .filter { !wxmlAttributeNames.contains(it.key) }
            // 添加组件对应的属性
            completionResultSet.addAllElements(attributes.map {
                createLookupElementFromCommonAttribute(it)
            })

            //添加组件对应的事件
            completionResultSet.addAllElements(createLookupElementsFromEvents(elementDescriptor.events))

            if (!WXMLMetadata.NATIVE_COMPONENTS.contains(tagName)) {
                // 无障碍访问属性
                completionResultSet.addAllElements(WXMLMetadata.ARIA_ATTRIBUTE.map {
                    LookupElementBuilder.create(it).withInsertHandler(DoubleQuotaInsertHandler(false))
                })
            }
        } else {
            // 尝试寻找自定义组件
            val wxmlTag = PsiTreeUtil.findChildOfType(
                    wxmlElement, WXMLTag::class.java
            )
            val jsFile = wxmlTag?.let { ComponentWxmlUtils.findCustomComponentDefinitionJsFile(wxmlTag) }
            // 根据js中的properties配置项提供完成
            jsFile?.let {
                ComponentJsUtils.findPropertiesItems(jsFile)
            }?.mapNotNull {
                LookupElementBuilder.create(it.name ?: return@mapNotNull null)
                        .withInsertHandler(
                                if (ComponentJsUtils.findTypeByPropertyValue(
                                                it
                                        ) != "String") DoubleBraceInsertHandler() else DoubleQuotaInsertHandler(false)
                        ).withTypeText("Component.properties")
            }?.let {
                completionResultSet.addAllElements(it)
            }

            // 根据js中的externalClasses配置项提供完成
            jsFile?.let {
                ComponentJsUtils.findComponentExternalClasses(it)
            }?.map {
                LookupElementBuilder.create(it)
                        .withInsertHandler(DoubleQuotaInsertHandler(true))
                        .withTypeText("Component.externalClasses")
            }?.let {
                completionResultSet.addAllElements(it)
            }
        }

        if (!IGNORE_WX_ATTRIBUTE_TAG_NAMES.contains(tagName)) {
            // 提供固定的wx前缀完成
            completionResultSet.addAllElements(
                    WX_ATTRIBUTES.map {
                        if (it == NO_VALUE_ATTRIBUTE) {
                            LookupElementBuilder.create(it)
                        } else {
                            LookupElementBuilder.create(it).withInsertHandler(DoubleBraceInsertHandler())
                        }
                    })
        }

        if (!IGNORE_COMMON_ATTRIBUTE_TAG_NAMES.contains(tagName)) {
            // 公共属性
            completionResultSet.addAllElements(WXMLMetadata.COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS.map {
                createLookupElementFromCommonAttribute(it)
            })
        }

        if (!IGNORE_COMMON_EVENT_TAG_NAMES.contains(tagName)) {
            // 公共事件
            completionResultSet.addAllElements(createLookupElementsFromEvents(WXMLMetadata.COMMON_ELEMENT_EVENTS))
        }
    }

    private fun createLookupElementsFromEvents(events: Array<String>): List<LookupElementBuilder> {
        return events.asSequence().flatMap {
            WXMLLanguage.EVENT_ATTRIBUTE_PREFIX_ARRAY.map { prefix ->
                prefix + it
            }.asSequence()
        }.map {
            LookupElementBuilder.create(it).withInsertHandler(DoubleQuotaInsertHandler(false))
        }.toList()
    }

    private fun createLookupElementFromCommonAttribute(
            wxmlElementAttributeDescription: WXMLElementAttributeDescription
    ): LookupElementBuilder {
        return when {
            isOnlyNameForInsert(wxmlElementAttributeDescription) -> {
                // 属性的默认值为false
                // 只插入属性名称
                return LookupElementBuilder.create(wxmlElementAttributeDescription.key)
            }
            isDoubleBraceForInsert(wxmlElementAttributeDescription) -> LookupElementBuilder.create(
                    wxmlElementAttributeDescription.key
            ).withInsertHandler(DoubleBraceInsertHandler())
            else -> LookupElementBuilder.create(
                    wxmlElementAttributeDescription.key
            ).withInsertHandler(DoubleQuotaInsertHandler(wxmlAttributeIsEnumerable(wxmlElementAttributeDescription)))
        }
    }

}

