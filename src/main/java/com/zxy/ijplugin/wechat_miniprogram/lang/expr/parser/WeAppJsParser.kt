package com.zxy.ijplugin.wechat_miniprogram.lang.expr.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.ecmascript6.parsing.ES6ExpressionParser
import com.intellij.lang.ecmascript6.parsing.ES6FunctionParser
import com.intellij.lang.ecmascript6.parsing.ES6Parser
import com.intellij.lang.ecmascript6.parsing.ES6StatementParser
import com.intellij.lang.javascript.JSTokenTypes
import com.intellij.lang.javascript.parsing.JSPsiTypeParser
import com.intellij.lang.javascript.parsing.JavaScriptParser
import org.intellij.plugins.relaxNG.compact.RncTokenTypes.KEYWORDS

class WeAppJsParser(builder: PsiBuilder) :
        ES6Parser<ES6ExpressionParser<WeAppJsParser>, WeAppJsParser.WeAppJsStatementParser, ES6FunctionParser<WeAppJsParser>,
                JSPsiTypeParser<JavaScriptParser<*, *, *, *>>>(builder) {

    inner class WeAppJsStatementParser(parser: WeAppJsParser) : ES6StatementParser<WeAppJsParser>(parser){

    }

    inner class WeAppJsExpressionParser(parser: WeAppJsParser) : ES6ExpressionParser<WeAppJsParser>(parser) {

        private var expressionNestingLevel: Int = 0

        override fun parseScriptExpression(isTypeContext: Boolean) {
            throw UnsupportedOperationException()
        }

        override fun parseAssignmentExpression(allowIn: Boolean): Boolean {
            expressionNestingLevel++
            try {
                return super.parseAssignmentExpression(allowIn)
            }
            finally {
                expressionNestingLevel--
            }
        }

        override fun getCurrentBinarySignPriority(allowIn: Boolean, advance: Boolean): Int {
            return if (builder.tokenType === JSTokenTypes.OR && expressionNestingLevel <= 1) {
                -1
            }
            else super.getCurrentBinarySignPriority(allowIn, advance)
        }
    }
}