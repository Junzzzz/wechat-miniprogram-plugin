package com.zxy.ijplugin.wechat_miniprogram.lang.expr

import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator
import com.intellij.lang.javascript.psi.resolve.JSTypeProcessor
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile

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
        val project = expression.project
        if (myQualifier == null ){
            //TODO
            val psiManager = PsiManager.getInstance(project)
            val jsFile = findRelateFile(expression.containingFile.virtualFile,RelateFileType.JS)
            if (jsFile!=null){
                val jsPsiFile = psiManager.findFile(jsFile)
                if (jsPsiFile!=null){
                    // 找到最外层的方法调用
                    PsiTreeUtil.getChildrenOfType(JsT)
                }
            }
        }
        return super.resolve(expression, incompleteCode)
    }
}
