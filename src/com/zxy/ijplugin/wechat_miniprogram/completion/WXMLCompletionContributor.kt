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
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLElementAttributeDescriptor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTag
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.getPathRelativeToRootRemoveExt

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
                        )!!
                        val wxmlAttribute = PsiTreeUtil.findChildOfType(wxmlElement, WXMLAttribute::class.java)!!
                        val tagName = wxmlElement.tagName
                        val attribute = WXMLMetadata.ELEMENT_DESCRIPTORS.stream().filter { it.name == tagName }
                                .findFirst()
                                .map { it.attributeDescriptors }
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
        attribute: WXMLElementAttributeDescriptor
) = attribute.enums.isNotEmpty() && attribute.types.size == 1 && attribute.types[0] == WXMLElementAttributeDescriptor.ValueType.STRING && attribute.requiredInEnums

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
        cloneCompletionResultSet.addAllElements(WXMLMetadata.ELEMENT_DESCRIPTORS.map { wxmlElementDescriptor ->
            val requiredElements = wxmlElementDescriptor.attributeDescriptors.filter {
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
        val WX_ATTRIBUTES = arrayOf("wx:for", "wx:elseif", "wx:else", "wx:key", "wx:if")
        /**
         * 忽略公共的属性的标签名
         */
        val IGNORE_COMMON_ATTRIBUTE_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include")
        /**
         * 忽略wx属性的标签名
         */
        val IGNORE_WX_ATTRIBUTE_TAG_NAMES = arrayOf("template", "wxs", "import", "include")
        /**
         * 忽略公共事件的标签名
         */
        val IGNORE_COMMON_EVENT_TAG_NAMES = arrayOf("block", "template", "wxs", "import", "include")

        internal fun isDoubleBraceForInsert(
                wxmlElementAttributeDescriptor: WXMLElementAttributeDescriptor
        ): Boolean {
            return wxmlElementAttributeDescriptor.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.NUMBER
            ) && !wxmlElementAttributeDescriptor.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.STRING
            )
        }

        internal fun isOnlyNameForInsert(
                wxmlElementAttributeDescriptor: WXMLElementAttributeDescriptor
        ): Boolean {
            return wxmlElementAttributeDescriptor.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.BOOLEAN
            ) && wxmlElementAttributeDescriptor.default == false
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

        val elementDescriptor = WXMLMetadata.ELEMENT_DESCRIPTORS.find { it.name == tagName }
        if (elementDescriptor != null) {
            // 自带组件
            val attributes = elementDescriptor.attributeDescriptors
                    // 过滤掉元素上已存在的类型
                    .filter { !wxmlAttributeNames.contains(it.key) }
            // 添加组件对应的属性
            completionResultSet.addAllElements(attributes.map {
                createLookupElementFromAttribute(it)
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
            val jsFile = wxmlTag?.getDefinitionJsFile()
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
                        LookupElementBuilder.create(it).withInsertHandler(DoubleBraceInsertHandler())
                    })
        }

        if (!IGNORE_COMMON_ATTRIBUTE_TAG_NAMES.contains(tagName)) {
            // 公共属性
            completionResultSet.addAllElements(WXMLMetadata.COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS.map {
                createLookupElementFromAttribute(it)
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

    private fun createLookupElementFromAttribute(
            wxmlElementAttributeDescriptor: WXMLElementAttributeDescriptor
    ): LookupElementBuilder {
        return when {
            isOnlyNameForInsert(wxmlElementAttributeDescriptor) -> {
                // 属性的默认值为false
                // 只插入属性名称
                return LookupElementBuilder.create(wxmlElementAttributeDescriptor.key)
            }
            isDoubleBraceForInsert(wxmlElementAttributeDescriptor) -> LookupElementBuilder.create(
                    wxmlElementAttributeDescriptor.key
            ).withInsertHandler(DoubleBraceInsertHandler())
            else -> LookupElementBuilder.create(
                    wxmlElementAttributeDescriptor.key
            ).withInsertHandler(DoubleQuotaInsertHandler(wxmlAttributeIsEnumerable(wxmlElementAttributeDescriptor)))
        }
    }

}

