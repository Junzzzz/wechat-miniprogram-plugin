package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType

class WechatMiniProgramJSONReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
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
                                    val fileReferences = FileReferenceSet(psiElement).allReferences
                                    if (fileReferences.isNotEmpty()) {
                                        val last = fileReferences.last()!!
                                        if (last.resolve() == null && fileReferences.size >= 2) {
                                            // 没有文件扩展名 最后一个引用无法解析出文件
                                            val filename = last.text
                                            val psiDirectory = fileReferences[fileReferences.size - 2].resolve()
                                            if (psiDirectory is PsiDirectory) {
                                                val references = fileReferences.map { it as PsiReference }
                                                        .toTypedArray()
                                                // 最后一个引用可能解析出多个文件 js|wxss|wxml
                                                val lastFileReference = object :
                                                        PsiPolyVariantReferenceBase<JsonStringLiteral>(
                                                                psiElement, last.rangeInElement
                                                        ) {
                                                    override fun multiResolve(p0: Boolean): Array<ResolveResult> =
                                                            psiDirectory.files.filter {
                                                                it.virtualFile.nameWithoutExtension == filename
                                                                        && (it.virtualFile.extension == JavaScriptFileType.DEFAULT_EXTENSION
                                                                        || it.virtualFile.extension == WXMLFileType.INSTANCE.defaultExtension
                                                                        || it.virtualFile.extension == WXSSFileType.INSTANCE.defaultExtension)
                                                            }.map {
                                                                PsiElementResolveResult(it)
                                                            }.toTypedArray()

                                                    override fun handleElementRename(
                                                            newElementName: String
                                                    ): PsiElement {
                                                        return super.handleElementRename(newElementName)
                                                    }
                                                }
                                                references[fileReferences.size - 1] = lastFileReference
                                                return references
                                            }
                                        } else {
                                            return fileReferences
                                        }
                                    }
                                }
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )
    }

}