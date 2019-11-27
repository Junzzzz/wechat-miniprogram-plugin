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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

import com.intellij.lang.javascript.highlighting.JSHighlighter
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.css.impl.util.CssHighlighter
import com.intellij.psi.tree.IElementType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.lexer._WXMLLexer
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val WXML_TAG = createTextAttributesKey("WXML_TAG", XmlHighlighterColors.HTML_TAG)
        val WXML_TAG_NAME = createTextAttributesKey("WXML_TAG_NAME", XmlHighlighterColors.HTML_TAG_NAME)
        val WXML_ATTRIBUTE_VALUE = createTextAttributesKey(
                "WXML_ATTRIBUTE_VALUE", XmlHighlighterColors.HTML_ATTRIBUTE_VALUE
        )
        val WXML_ATTRIBUTE_NAME = createTextAttributesKey(
                "WXML_ATTRIBUTE_NAME", XmlHighlighterColors.HTML_ATTRIBUTE_NAME
        )
        val WXML_COMMENT = createTextAttributesKey("WXML_COMMENT", XmlHighlighterColors.HTML_COMMENT)
        val WXML_IDENTIFIER = createTextAttributesKey("WXML_IDENTIFIER", JSHighlighter.JS_LOCAL_VARIABLE)
        val WXML_BRACKET = createTextAttributesKey("WXML_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
        val WXML_COMMA = createTextAttributesKey("WXML_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val WXML_NUMBER = createTextAttributesKey("WXML_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val WXML_NATIVE_VALUE = createTextAttributesKey("WXML_NATIVE_VALUE", JSHighlighter.JS_KEYWORD)
        val WXML_OPERATOR = createTextAttributesKey("WXML_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val WXML_COLON = createTextAttributesKey("WXML_COLON", CssHighlighter.CSS_COLON)
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        val textAttributesKey = when (iElementType) {
            WXMLTypes.START_TAG_START,
            WXMLTypes.START_TAG_END,
            WXMLTypes.END_TAG_START,
            WXMLTypes.EMPTY_ELEMENT_END -> WXML_TAG
            WXMLTypes.ATTRIBUTE_NAME -> WXML_ATTRIBUTE_NAME
            WXMLTypes.EQ,
            WXMLTypes.STRING_START,
            WXMLTypes.STRING_END,
            WXMLTypes.STRING_CONTENT -> WXML_ATTRIBUTE_VALUE
            WXMLTypes.TAG_NAME -> WXML_TAG_NAME
            WXMLTypes.COMMENT_START,
            WXMLTypes.COMMENT_CONTENT,
            WXMLTypes.COMMONT_END -> WXML_COMMENT
            // expr
//            WXMLTypes.IDENTIFIER -> WXML_IDENTIFIER
//            WXMLTypes.LEFT_BRACKET,
//            WXMLTypes.RIGHT_BRACKET -> WXML_BRACKET
//            WXMLTypes.COMMA -> WXML_COMMA
//            WXMLTypes.NUMBER -> WXML_NUMBER
//            WXMLTypes.FALSE,
//            WXMLTypes.TRUE,
//            WXMLTypes.NULL -> WXML_NATIVE_VALUE
//            WXMLTypes.PLUS,
//            WXMLTypes.MINUS,
//            WXMLTypes.MULTIPLY,
//            WXMLTypes.DIVIDE,
//            WXMLTypes.RESIDUAL,
//            WXMLTypes.NOT_EQ,
            WXMLTypes.EQ
//            WXMLTypes.NOT_STRICT_EQ,
//            WXMLTypes.QUESTION_MARK,
//            WXMLTypes.EXPAND_KEYWORD,
//            WXMLTypes.EXCLAMATION_MARK,
//            WXMLTypes.STRICT_EQ
            -> WXML_OPERATOR
//            WXMLTypes.COLON -> WXML_COLON
            else -> null
        }
        return textAttributesKey?.let { arrayOf(it) } ?: emptyArray()
    }

    override fun getHighlightingLexer(): Lexer {
        return FlexAdapter(_WXMLLexer(null))
    }

}