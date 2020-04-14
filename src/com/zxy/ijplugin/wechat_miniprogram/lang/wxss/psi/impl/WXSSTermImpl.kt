/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl

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