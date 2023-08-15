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

package com.zxy.ijplugin.miniprogram.lang.wxss.psi.impl

import com.intellij.codeInsight.completion.CompletionUtilCoreImpl
import com.intellij.css.util.CssConstants
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.css.CssFunction
import com.intellij.psi.css.CssTerm
import com.intellij.psi.css.CssTermType
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.CssTermImpl
import com.intellij.psi.css.impl.CssTermTypes
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.xml.util.ColorMap

/**
 * @see CssTermImpl
 */
class WXSSTermImpl : CssTermImpl() {

    private val cssTermType by lazy {
        calcTermType() ?: CssTermTypes.IDENT
    }

    override fun getTermType(): CssTermType {
        return this.cssTermType
    }

    private fun calcTermType(): CssTermType? {
        val term = CompletionUtilCoreImpl.getOriginalElement(this) as? CssTerm ?: this
        val parent = term.parent
        var child = term.firstChild
        if (child != null && child.node.elementType === CssElementTypes.CSS_PLUS) {
            child = child.nextSibling
        }
        if (child != null) {
            val astNode = child.node
            if (astNode != null) {
                val type = astNode.elementType
                val firstTreeChild = astNode.firstChildNode
                val lastTreeChild = astNode.lastChildNode
                val prevLeaf = PsiTreeUtil.prevLeaf(child)
                val afterMinus = parent is CssTerm && prevLeaf != null && prevLeaf.node
                        .elementType === CssElementTypes.CSS_MINUS
                if (!afterMinus && CssElementTypes.CSS_NUMBER_TERM === type) {
                    if (CssElementTypes.CSS_NUMBER === firstTreeChild.elementType) {
                        if (firstTreeChild === lastTreeChild) {
                            return CssTermTypes.NUMBER
                        }
                        if (CssElementTypes.CSS_IDENT === lastTreeChild.elementType) {
                            val suffixType = getTypeBySuffix(
                                    lastTreeChild.text
                            )
                            return if (suffixType === CssTermType.UNKNOWN) CssTermTypes.NUMBER_WITH_UNKNOWN_UNIT else suffixType
                        }
                        if (CssElementTypes.CSS_PERCENT === lastTreeChild.elementType) {
                            return CssTermTypes.PERCENTAGE
                        }
                    }
                } else if (!afterMinus && CssElementTypes.CSS_MINUS !== type) {
                    if (CssElementTypes.CSS_HASH === type) {
                        return if (matchHashColor()) CssTermTypes.COLOR else CssTermTypes.IDENT
                    }
                    if (CssElementTypes.CSS_UNICODE_RANGE === type) {
                        return CssTermTypes.UNICODE_RANGE
                    }
                    val colorName: String
                    if (child is CssFunction) {
                        if (CssElementTypes.CSS_FUNCTION_TOKEN === firstTreeChild.elementType) {
                            colorName = firstTreeChild.text
                            if (CssConstants.COLOR_FUNCTIONS.contains(
                                            StringUtil.toLowerCase(colorName)
                                    )) {
                                return CssTermTypes.COLOR
                            }
                            if ("rect".equals(colorName, ignoreCase = true)) {
                                return CssTermTypes.RECT
                            }
                            if ("counter".equals(colorName, ignoreCase = true)) {
                                return CssTermTypes.COUNTER
                            }
                            if ("var".equals(colorName, ignoreCase = true)) {
                                return CssTermTypes.VAR
                            }
                        }
                        return child.type
                    }
                    if (parent != null && parent.node
                                    .elementType === CssElementTypes.CSS_URI && type === CssElementTypes.CSS_STRING) {
                        return CssTermTypes.URI
                    }
                    if (CssElementTypes.CSS_STRING === type) {
                        return CssTermTypes.STRING
                    }
                    if (CssElementTypes.CSS_URI === type) {
                        return CssTermTypes.URI
                    }
                    if (CssElementTypes.CSS_IDENT === type) {
                        colorName = StringUtil.toLowerCase(this.text)
                        if (ColorMap.getHexCodeForColorName(colorName) != null || ColorMap.isSystemColorName(
                                        colorName
                                )) {
                            return CssTermTypes.COLOR
                        }
                    }
                } else {
                    var numberTerm = if (afterMinus) this else child.nextSibling
                    if (numberTerm is CssTerm) {
                        numberTerm = (numberTerm as PsiElement).firstChild
                    }
                    if (numberTerm != null) {
                        val node = numberTerm.node
                        if (node != null && CssElementTypes.CSS_NUMBER_TERM === node.elementType) {
                            val lastNumberTermNode = node.lastChildNode
                            if (lastNumberTermNode != null) {
                                if (CssElementTypes.CSS_IDENT === lastNumberTermNode.elementType) {
                                    val lengthSuffixText = StringUtil.toLowerCase(
                                            lastNumberTermNode.text
                                    )
                                    if (getTypeBySuffix(
                                                    lengthSuffixText
                                            ) == CssTermTypes.LENGTH) {
                                        return CssTermTypes.NEGATIVE_LENGTH
                                    }
                                    if (getTypeBySuffix(
                                                    lengthSuffixText
                                            ) == CssTermTypes.ANGLE) {
                                        return CssTermTypes.NEGATIVE_ANGLE
                                    }
                                    val suffixText = StringUtil.toLowerCase(
                                            lastNumberTermNode.text
                                    )
                                    val suffixType = getTypeBySuffix(
                                            suffixText
                                    )
                                    return if (suffixType === CssTermType.UNKNOWN) CssTermTypes.NUMBER_WITH_UNKNOWN_UNIT else suffixType
                                }
                                if (CssElementTypes.CSS_PERCENT === lastNumberTermNode.elementType) {
                                    return CssTermTypes.NEGATIVE_PERCENTAGE
                                }
                                if (CssElementTypes.CSS_NUMBER === lastNumberTermNode.elementType) {
                                    return CssTermTypes.NEGATIVE_NUMBER
                                }
                            }
                        }
                    }
                }
            }
        }
        return CssTermTypes.IDENT
    }

    private fun matchHashColor(): Boolean {
        val s = StringUtil.toLowerCase(this.text)
        return s.matches(Regex("#([0-9a-f]{3}|[0-9a-f]{4}|[0-9a-f]{6}|[0-9a-f]{8})$"))
    }

    companion object {
        /**
         * @see CssTermImpl.getTypeBySuffix
         */
        fun getTypeBySuffix(text: String): CssTermType {
            if (text == "rpx") {
                return CssTermTypes.LENGTH
            }
            return CssTermImpl.getTypeBySuffix(text)
        }
    }

}