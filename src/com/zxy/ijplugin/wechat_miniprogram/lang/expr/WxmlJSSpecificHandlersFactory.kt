package com.zxy.ijplugin.wechat_miniprogram.lang.expr

import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator
import com.intellij.lang.javascript.psi.resolve.JSTypeProcessor
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache

// TODO  参考Vue插件
class WxmlJSSpecificHandlersFactory : JavaScriptSpecificHandlersFactory() {

    override fun createReferenceExpressionResolver(
            referenceExpression: JSReferenceExpressionImpl, ignorePerformanceLimits: Boolean
    ): ResolveCache.PolyVariantResolver<JSReferenceExpressionImpl> {
        return WxmlJsReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits)
    }

    override fun newTypeEvaluator(context: JSEvaluateContext, processor: JSTypeProcessor): JSTypeEvaluator {
        return super.newTypeEvaluator(context, processor)
    }

}

class WxmlJsReferenceExpressionResolver(referenceExpression: JSReferenceExpressionImpl,
                                       ignorePerformanceLimits: Boolean) :
        JSReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits){
    override fun resolve(expression: JSReferenceExpressionImpl, incompleteCode: Boolean): Array<ResolveResult> {
        if (myReferencedName == null) return ResolveResult.EMPTY_ARRAY
        if (myQualifier == null ){
            //TODO
        }
        return super.resolve(expression, incompleteCode)
    }
}
