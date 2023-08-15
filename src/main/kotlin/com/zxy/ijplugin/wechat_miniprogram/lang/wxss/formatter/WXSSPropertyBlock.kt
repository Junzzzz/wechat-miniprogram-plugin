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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.css.CssTermType
import com.intellij.psi.css.impl.CssElementTypes
import com.intellij.psi.css.impl.util.completion.TimeUserLookup
import com.intellij.psi.css.impl.util.editor.CssFormattingModelBuilder
import com.intellij.psi.xml.XmlTokenType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl.WXSSTermImpl

open class WXSSPropertyBlock(
        _node: ASTNode?, indent: Indent?, extension: CssFormattingModelBuilder.CssFormattingExtension?,
        alignment: Alignment?, childAlignment: Alignment?
) : CssFormattingModelBuilder.CssPropertyBlock(
        _node, indent,
        extension, alignment, childAlignment
) {
    override fun getSpacing(formatterBlock: Block?, formatterBlock2: Block): Spacing? {
        return if (formatterBlock is CssFormattingModelBuilder.CssFormatterBlock && formatterBlock2 is CssFormattingModelBuilder.CssFormatterBlock) {
            val codeStyleSettings = this.customSettings
            if (codeStyleSettings.VALUE_ALIGNMENT == 1 && formatterBlock2.myType === CssElementTypes.CSS_COLON) {
                Spacing.createSpacing(1, 1, 0, false, 0)
            } else if (myExtension.isLineComment(formatterBlock.myType)) {
                Spacing.createSpacing(0, 0, 1, true, this.keepBlankLines)
            } else if (formatterBlock.myType !== CssElementTypes.CSS_LPAREN && formatterBlock2.myType !== CssElementTypes.CSS_RPAREN) {
                if (formatterBlock2.myType === CssElementTypes.CSS_COMMA) {
                    Spacing.createSpacing(0, 0, 0, false, 0)
                } else if (formatterBlock.myType === CssElementTypes.CSS_COMMA) {
                    Spacing.createSpacing(1, 1, 0, true, 0)
                } else if (formatterBlock2.myType === CssElementTypes.CSS_LPAREN && this.node
                                .elementType === CssElementTypes.CSS_IMPORT) {
                    Spacing.createSpacing(0, 1, 0, false, 0)
                } else {
                    val textOfFormatterBlock2 = StringUtil.toLowerCase(
                            formatterBlock2.node.text
                    )
                    if (formatterBlock.myType !== CssElementTypes.CSS_MINUS && formatterBlock.myType !== CssElementTypes.CSS_ASTERISK && (formatterBlock.myType !== CssElementTypes.CSS_PLUS || formatterBlock2.myType !== CssElementTypes.CSS_NUMBER) && (formatterBlock.myType !== CssElementTypes.CSS_IDENT || formatterBlock2.myType !== CssElementTypes.CSS_PLUS) && (formatterBlock.myType !== CssElementTypes.CSS_PLUS || formatterBlock2.myType !== CssElementTypes.CSS_IDENT) && formatterBlock2.myType !== CssElementTypes.CSS_COLON && formatterBlock2.myType !== CssElementTypes.CSS_SEMICOLON && formatterBlock2.myType !== CssElementTypes.CSS_PERCENT && (formatterBlock.myType !== CssElementTypes.CSS_NUMBER && formatterBlock.myType !== XmlTokenType.XML_COMMENT_START || formatterBlock2.myType !== CssElementTypes.CSS_IDENT || WXSSTermImpl.getTypeBySuffix(
                                    textOfFormatterBlock2
                            ) === CssTermType.UNKNOWN && "n" != textOfFormatterBlock2 && "x" != textOfFormatterBlock2 && !TimeUserLookup.isTimeSuffix(
                                    textOfFormatterBlock2
                            ))) {
                        if (formatterBlock2.myType === CssElementTypes.CSS_LPAREN) {
                            Spacing.createSpacing(0, 1, 0, false, 0)
                        } else if (formatterBlock.myType === CssElementTypes.CSS_COLON) {
                            if (codeStyleSettings.SPACE_AFTER_COLON) Spacing.createSpacing(
                                    1, 1, 0, false, 0
                            ) else Spacing.createSpacing(0, 0, 0, false, 0)
                        } else {
                            Spacing.createSpacing(1, 1, 0, false, 0)
                        }
                    } else {
                        Spacing.createSpacing(0, 0, 0, false, 0)
                    }
                }
            } else {
                Spacing.createSpacing(0, 0, 0, true, 0)
            }
        } else {
            null
        }
    }
}