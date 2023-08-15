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

package com.zxy.ijplugin.miniprogram.lang.expr

import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.zxy.ijplugin.miniprogram.context.MyJSPredefinedLibraryProvider.Companion.PAGE_LIFETIMES
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.lang.wxml.WxmlJSInjector
import com.zxy.ijplugin.miniprogram.lang.wxml.utils.isEventHandler

class WxmlJsSpecificHandlersFactory : JavaScriptSpecificHandlersFactory() {

    override fun createReferenceExpressionResolver(
            referenceExpression: JSReferenceExpressionImpl, ignorePerformanceLimits: Boolean
    ): ResolveCache.PolyVariantResolver<JSReferenceExpressionImpl> {
        return WxmlJsReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits)
    }

}

/**
 * 控制wxml 中的表达式如何引用 js 中的标识符
 */
class WxmlJsReferenceExpressionResolver(
        referenceExpression: JSReferenceExpressionImpl,
        ignorePerformanceLimits: Boolean
) :
        JSReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits) {

    override fun resolve(expression: JSReferenceExpressionImpl, incompleteCode: Boolean): Array<ResolveResult> {
        if (myReferencedName == null) return ResolveResult.EMPTY_ARRAY
        val project = expression.project
        if (myQualifier == null) {
            val injectionHost = InjectedLanguageManager.getInstance(project).getInjectionHost(
                    expression
            ) ?: return ResolveResult.EMPTY_ARRAY
            val originFile = injectionHost.containingFile ?: return ResolveResult.EMPTY_ARRAY
            val jsPsiFile = RelateFileHolder.SCRIPT.findFile(originFile) as? JSFile
            if (jsPsiFile != null) {
                if (injectionHost is XmlAttributeValue && PsiTreeUtil.getParentOfType(
                                injectionHost, XmlAttribute::class.java
                        )?.isEventHandler() == true && !WxmlJSInjector.DOUBLE_BRACE_REGEX.matches(
                                injectionHost.value
                        )) {
                    // 事件
                    // 找到js文件中的methods
                    resolveMethods(jsPsiFile)?.let {
                        return it
                    }
                } else {
                    resolveProperties(jsPsiFile)?.let {
                        return it
                    }
                }
            }
        }
        return super.resolve(expression, incompleteCode)
    }

    private fun findJsCallExpressionAndResolve(
            jsPsiFile: PsiFile,
            componentApiResolve: (jsCallExpression: JSCallExpression) -> Array<ResolveResult>?,
            pageApiResolve: (jsCallExpression: JSCallExpression) -> Array<ResolveResult>?
    ): Array<ResolveResult>? {
        // 找到最外层的方法调用
        val arrayOfJSExpressionStatements = PsiTreeUtil.getChildrenOfType(
                jsPsiFile, JSExpressionStatement::class.java
        ) ?: emptyArray()
        val jsCallExpressions = arrayOfJSExpressionStatements.asSequence().mapNotNull {
            PsiTreeUtil.getChildOfType(
                    it, JSCallExpression::class.java
            )
        }
        for (jsCallExpression in jsCallExpressions) {
            val jsReferenceExpression = PsiTreeUtil.getChildOfType(
                    jsCallExpression, JSReferenceExpression::class.java
            ) ?: continue
            if (jsReferenceExpression.text == "Component") {
                // ComponentApi
                componentApiResolve(jsCallExpression)?.let {
                    if (it.isNotEmpty()) return it
                }
            } else if (jsReferenceExpression.text == "Page") {
                // PageApi
                pageApiResolve(jsCallExpression)?.let {
                    if (it.isNotEmpty()) return it
                }
            }
        }
        return null
    }

    private fun resolveMethods(jsPsiFile: PsiFile): Array<ResolveResult>? {
        return this.findJsCallExpressionAndResolve(
                jsPsiFile, this::resolveMethodsForComponent, this::resolveMethodsForPage
        )
    }

    private fun resolveMethodsForComponent(
            jsCallExpression: JSCallExpression
    ): Array<ResolveResult> {
        val results = arrayListOf<ResolveResult>()

        val properties = getCallExpressionProperties(jsCallExpression)

        // 解析componentApi里的properties属性
        val methodsProperty = properties.find {
            it.name == "methods"
        }
        addObjectKeys(methodsProperty, results)
        return results.toTypedArray()
    }

    private fun resolveMethodsForPage(jsCallExpression: JSCallExpression): Array<ResolveResult>? {
        if (PAGE_LIFETIMES.contains(this.myReferencedName)) {
            return null
        }
        val properties = getCallExpressionProperties(jsCallExpression)
        val result = properties.find {
            it.name == this.myReferencedName
        }
        if (result != null) {
            return arrayOf(PsiElementResolveResult(result))
        }
        return null
    }

    private fun addObjectKeys(
            keyValueProperty: JSProperty?,
            results: ArrayList<ResolveResult>
    ) {
        val methodsPropertyObjectLiteral = PsiTreeUtil.getChildOfType(
                keyValueProperty, JSObjectLiteralExpression::class.java
        )
        val componentItems = PsiTreeUtil.getChildrenOfType(methodsPropertyObjectLiteral, JSProperty::class.java)
        componentItems?.find {
            it.name == myReferencedName
        }?.let {
            results.add(PsiElementResolveResult(it))
        }
    }

    private fun resolveProperties(jsPsiFile: PsiFile): Array<ResolveResult>? {
        return findJsCallExpressionAndResolve(
                jsPsiFile, this::resolvePropertiesForComponent, this::resolvePropertiesForPage
        )
    }

    private fun resolvePropertiesForComponent(
            jsCallExpression: JSCallExpression
    ): Array<ResolveResult> {
        val results = arrayListOf<ResolveResult>()

        val properties = getCallExpressionProperties(jsCallExpression)

        // 解析componentApi里的properties属性
        val propertiesProperty = properties.find {
            it.name == "properties"
        }
        this.addObjectKeys(propertiesProperty, results)

        results.addAll(resolvePropertiesForPage(properties))
        return results.toTypedArray()
    }

    private fun resolvePropertiesForPage(jsCallExpression: JSCallExpression): Array<ResolveResult> {
        val properties = getCallExpressionProperties(jsCallExpression)
        return resolvePropertiesForPage(properties)
    }

    private fun getCallExpressionProperties(
            jsCallExpression: JSCallExpression
    ): Array<out JSProperty> {
        val args = PsiTreeUtil.getChildOfType(jsCallExpression, JSArgumentList::class.java)
        val objectLiteral = PsiTreeUtil.getChildOfType(args, JSObjectLiteralExpression::class.java)
        return PsiTreeUtil.getChildrenOfType(objectLiteral, JSProperty::class.java) ?: emptyArray()
    }

    private fun resolvePropertiesForPage(callExpressionProperties: Array<out JSProperty>): Array<ResolveResult> {
        val results = arrayListOf<ResolveResult>()
        val dataProperty = callExpressionProperties.find {
            it.name == "data"
        }
        val dataPropertyObjectLiteral = PsiTreeUtil.getChildOfType(
                dataProperty, JSObjectLiteralExpression::class.java
        )
        val dataItems = PsiTreeUtil.getChildrenOfType(dataPropertyObjectLiteral, JSProperty::class.java)
        dataItems?.find { it.name == myReferencedName }?.let {
            results.add(PsiElementResolveResult(it))
        }
        return results.toTypedArray()
    }

}
