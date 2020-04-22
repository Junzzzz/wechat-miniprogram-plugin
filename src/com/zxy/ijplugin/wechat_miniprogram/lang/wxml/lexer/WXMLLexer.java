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
package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.lexer;

import com.intellij.lang.HtmlInlineScriptTokenTypesProvider;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageHtmlInlineScriptTokenTypesProvider;
import com.intellij.lang.LanguageUtil;
import com.intellij.lexer.*;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Copy from {@see com.intellij.lexer.HtmlLexer}
 */
public class WXMLLexer extends BaseHtmlLexer {
    public static final String INLINE_STYLE_NAME = "css-ruleset-block";
    private static final IElementType ourInlineStyleElementType;

    static {
        List<EmbeddedTokenTypesProvider> extensions = EmbeddedTokenTypesProvider.EXTENSION_POINT_NAME.getExtensionList();
        IElementType inlineStyleElementType = null;
        for (EmbeddedTokenTypesProvider extension : extensions) {
            if (INLINE_STYLE_NAME.equals(extension.getName())) {
                inlineStyleElementType = extension.getElementType();
                break;
            }
        }
        ourInlineStyleElementType = inlineStyleElementType;
    }

    private IElementType myTokenType;
    private int myTokenStart;
    private int myTokenEnd;

    public WXMLLexer(String scriptTagName) {
        this(new MergingLexerAdapter(new FlexAdapter(new __XmlLexer(null)), TOKENS_TO_MERGE), true, scriptTagName);
    }

    protected WXMLLexer(Lexer _baseLexer, boolean _caseInsensitive, String scriptTagName) {
        super(_baseLexer, _caseInsensitive, scriptTagName);
    }

    private static boolean isStartOfEmbeddmentAttributeValue(final IElementType tokenType) {
        return tokenType == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN;
    }

    private static boolean isStartOfEmbeddmentTagContent(final IElementType tokenType) {
        return (tokenType == XmlTokenType.XML_DATA_CHARACTERS ||
                tokenType == XmlTokenType.XML_CDATA_START ||
                tokenType == XmlTokenType.XML_COMMENT_START ||
                tokenType == XmlTokenType.XML_START_TAG_START ||
                tokenType == XmlTokenType.XML_REAL_WHITE_SPACE || tokenType == TokenType.WHITE_SPACE ||
                tokenType == XmlTokenType.XML_ENTITY_REF_TOKEN || tokenType == XmlTokenType.XML_CHAR_ENTITY_REF
        );
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        myTokenType = null;
        super.start(buffer, startOffset, endOffset, initialState);
    }

    @Override
    public void advance() {
        myTokenType = null;
        super.advance();
    }

    @Override
    public IElementType getTokenType() {
        if (myTokenType != null) return myTokenType;
        IElementType tokenType = super.getTokenType();

        myTokenStart = super.getTokenStart();
        myTokenEnd = super.getTokenEnd();

        if (hasSeenStyle()) {
            if (hasSeenTag() && isStartOfEmbeddmentTagContent(tokenType)) {
                Language stylesheetLanguage = getStyleLanguage();
                if (stylesheetLanguage == null || LanguageUtil.isInjectableLanguage(stylesheetLanguage)) {
                    myTokenEnd = skipToTheEndOfTheEmbeddment();
                    IElementType currentStylesheetElementType = getCurrentStylesheetElementType();
                    tokenType = currentStylesheetElementType == null ? XmlTokenType.XML_DATA_CHARACTERS : currentStylesheetElementType;
                }
            } else if (ourInlineStyleElementType != null && isStartOfEmbeddmentAttributeValue(tokenType) && hasSeenAttribute()) {
                tokenType = ourInlineStyleElementType;
            }
        } else if (hasSeenScript()) {
            if (hasSeenTag() && isStartOfEmbeddmentTagContent(tokenType)) {
                Language scriptLanguage = getScriptLanguage();
                if (scriptLanguage == null || LanguageUtil.isInjectableLanguage(scriptLanguage)) {
                    myTokenEnd = skipToTheEndOfTheEmbeddment();
                    IElementType currentScriptElementType = getCurrentScriptElementType();
                    tokenType = currentScriptElementType == null ? XmlTokenType.XML_DATA_CHARACTERS : currentScriptElementType;
                }
            } else if (hasSeenAttribute() && isStartOfEmbeddmentAttributeValue(tokenType)) {
                // At the moment only JS.
                HtmlInlineScriptTokenTypesProvider provider = LanguageHtmlInlineScriptTokenTypesProvider.getInlineScriptProvider(Language.findLanguageByID("JavaScript"));
                IElementType inlineScriptElementType = provider != null ? provider.getElementType() : null;
                if (inlineScriptElementType != null) {
                    myTokenEnd = skipToTheEndOfTheEmbeddment();
                    tokenType = inlineScriptElementType;
                }
            }
        }

        return myTokenType = tokenType;
    }

    @Override
    protected boolean isHtmlTagState(int state) {
        return state == __XmlLexer.TAG || state == __XmlLexer.END_TAG;
    }

    @Override
    public int getTokenStart() {
        if (myTokenType != null) {
            return myTokenStart;
        }
        return super.getTokenStart();
    }

    @Override
    public int getTokenEnd() {
        if (myTokenType != null) {
            return myTokenEnd;
        }
        return super.getTokenEnd();
    }
}
