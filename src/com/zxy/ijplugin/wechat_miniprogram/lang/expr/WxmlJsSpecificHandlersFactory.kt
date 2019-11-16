package com.zxy.ijplugin.wechat_miniprogram.lang.expr

import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator
import com.intellij.lang.javascript.psi.resolve.JSTypeProcessor
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile

// TODO  参考Vue插件
class WxmlJsSpecificHandlersFactory : JavaScriptSpecificHandlersFactory() {

    override fun createReferenceExpressionResolver(
            referenceExpression: JSReferenceExpressionImpl, ignorePerformanceLimits: Boolean
    ): ResolveCache.PolyVariantResolver<JSReferenceExpressionImpl> {
        return WxmlJsReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits)
    }

    override fun newTypeEvaluator(context: JSEvaluateContext, processor: JSTypeProcessor): JSTypeEvaluator {
        return super.newTypeEvaluator(context, processor)
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
            val psiManager = PsiManager.getInstance(project)
            val jsFile = findRelateFile(
                    InjectedLanguageManager.getInstance(project).getInjectionHost(
                            expression
                    )!!.containingFile.virtualFile, RelateFileType.JS
            )
            if (jsFile != null) {
                val jsPsiFile = psiManager.findFile(jsFile)
                if (jsPsiFile != null) {
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
                            resolveComponent(jsCallExpression).let {
                                if (it.isNotEmpty()) return it
                            }
                        } else if (jsReferenceExpression.text == "Page") {
                            // PageApi
                            resolvePage(jsCallExpression).let {
                                if (it.isNotEmpty()) return it
                            }
                        }
                    }
                }
            }
        }
        return super.resolve(expression, incompleteCode)
    }

    private fun resolveComponent(
            jsCallExpression: JSCallExpression
    ): Array<ResolveResult> {
        val results = arrayListOf<ResolveResult>()

        val properties = getCallExpressionProperties(jsCallExpression)

        // 解析componentApi里的properties属性
        val propertiesProperty = properties.find {
            it.name == "properties"
        }
        val propertiesPropertyObjectLiteral = PsiTreeUtil.getChildOfType(
                propertiesProperty, JSObjectLiteralExpression::class.java
        )
        val componentProperties = PsiTreeUtil.getChildrenOfType(propertiesPropertyObjectLiteral, JSProperty::class.java)
        componentProperties?.find {
            it.name == myReferencedName
        }?.let {
            results.add(PsiElementResolveResult(it))
        }

        results.addAll(resolvePage(properties))
        return results.toTypedArray()
    }

    private fun resolvePage(jsCallExpression: JSCallExpression): Array<ResolveResult> {
        val properties = getCallExpressionProperties(jsCallExpression)
        return resolvePage(properties)
    }

    private fun getCallExpressionProperties(
            jsCallExpression: JSCallExpression
    ): Array<out JSProperty> {
        val args = PsiTreeUtil.getChildOfType(jsCallExpression, JSArgumentList::class.java)
        val objectLiteral = PsiTreeUtil.getChildOfType(args, JSObjectLiteralExpression::class.java)
        return PsiTreeUtil.getChildrenOfType(objectLiteral, JSProperty::class.java) ?: emptyArray()
    }

    private fun resolvePage(callExpressionProperties: Array<out JSProperty>): Array<ResolveResult> {
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
