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

        //regex, curly, square, paren

        fun parseFilterOptional(): Boolean {
            var pipe: PsiBuilder.Marker = builder.mark()
            var firstParam: PsiBuilder.Marker = builder.mark()
            expressionNestingLevel = 0
            if (!parseExpressionOptional()) {
                firstParam.drop()
                pipe.drop()
                return false
            }

            while (builder.tokenType === JSTokenTypes.OR) {
                firstParam.done(FILTER_LEFT_SIDE_ARGUMENT)
                builder.advanceLexer()
                if (builder.tokenType === JSTokenTypes.IDENTIFIER || KEYWORDS.contains(builder.tokenType)) {
                    val pipeName = builder.mark()
                    builder.advanceLexer()
                    pipeName.done(FILTER_REFERENCE_EXPRESSION)
                }
                else {
                    builder.error("Expected identifier or string")
                }
                if (builder.tokenType === JSTokenTypes.LPAR) {
                    val params = builder.mark()
                    expressionNestingLevel = 2
                    parseArgumentListNoMarker()
                    params.done(FILTER_ARGUMENTS_LIST)
                    if (builder.tokenType !== JSTokenTypes.OR && !builder.eof()) {
                        val err = builder.mark()
                        builder.advanceLexer()
                        err.error("Expected | or end of expression")
                        while (builder.tokenType !== JSTokenTypes.OR && !builder.eof()) {
                            builder.advanceLexer()
                        }
                    }
                }
                else if (builder.tokenType !== JSTokenTypes.OR && !builder.eof()) {
                    val err = builder.mark()
                    builder.advanceLexer()
                    err.error("Expected (, | or end of expression")
                    while (builder.tokenType !== JSTokenTypes.OR && !builder.eof()) {
                        builder.advanceLexer()
                    }
                }
                pipe.done(FILTER_EXPRESSION)
                firstParam = pipe.precede()
                pipe = firstParam.precede()
            }
            firstParam.drop()
            pipe.drop()
            return true
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