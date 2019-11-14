package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator
import com.intellij.lang.javascript.psi.resolve.JSTypeProcessor
import com.intellij.psi.impl.source.resolve.ResolveCache

class WxmlJSSpecificHandlersFactory : JavaScriptSpecificHandlersFactory() {

    override fun createReferenceExpressionResolver(
            referenceExpression: JSReferenceExpressionImpl?, ignorePerformanceLimits: Boolean
    ): ResolveCache.PolyVariantResolver<JSReferenceExpressionImpl> {
        println("resolver")
        return super.createReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits)
    }

    override fun newTypeEvaluator(context: JSEvaluateContext, processor: JSTypeProcessor): JSTypeEvaluator {
        return super.newTypeEvaluator(context, processor)
    }

}