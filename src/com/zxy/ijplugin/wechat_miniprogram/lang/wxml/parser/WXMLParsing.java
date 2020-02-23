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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.parser;

import com.intellij.codeInsight.daemon.XmlErrorBundle;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.ICustomParsingType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.containers.Stack;
import com.intellij.xml.psi.XmlPsiBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.xml.XmlElementType.*;

/*
 * copy from XmlParsing
 * 支持多个根标签
 */
public class WXMLParsing {
    private static final int BALANCING_DEPTH_THRESHOLD = 1000;

    protected final PsiBuilder myBuilder;
    private final Stack<String> myTagNamesStack = new Stack<>();

    public WXMLParsing(final PsiBuilder builder) {
        myBuilder = builder;
    }

    @Nullable
    private static PsiBuilder.Marker flushError(PsiBuilder.Marker error) {
        if (error != null) {
            error.error(XmlPsiBundle.message("xml.parsing.unexpected.tokens"));
        }
        return null;
    }

    @Nullable
    private static PsiBuilder.Marker terminateText(@Nullable PsiBuilder.Marker xmlText) {
        if (xmlText != null) {
            xmlText.done(XML_TEXT);
        }
        return null;
    }

    public void parseDocument() {
        final PsiBuilder.Marker document = mark();

        while (isCommentToken(token())) {
            parseComment();
        }

        PsiBuilder.Marker error = null;
        while (eof()) {
            final IElementType tt = token();
            if (tt == XML_START_TAG_START) {
                error = flushError(error);
                parseTag();
            } else if (isCommentToken(tt)) {
                error = flushError(error);
                parseComment();
            } else if (tt == XML_PI_START) {
                error = flushError(error);
                parseProcessingInstruction();
            } else if (tt == XML_REAL_WHITE_SPACE) {
                error = flushError(error);
                advance();
            } else {
                if (error == null) error = mark();
                advance();
            }
        }

        if (error != null) {
            error.error(XmlPsiBundle.message("top.level.element.is.not.completed"));
        }

        document.done(XML_DOCUMENT);
    }

    protected void parseTag() {
        assert token() == XML_START_TAG_START : "Tag start expected";
        final PsiBuilder.Marker tag = mark();

        final String tagName = parseTagHeader(tag);
        if (tagName == null) return;

        final PsiBuilder.Marker content = mark();
        parseTagContent();

        if (token() == XML_END_TAG_START) {
            final PsiBuilder.Marker footer = mark();
            advance();

            if (token() == XML_NAME) {
                String endName = myBuilder.getTokenText();
                if (!tagName.equals(endName) && myTagNamesStack.contains(endName)) {
                    footer.rollbackTo();
                    myTagNamesStack.pop();
                    tag.doneBefore(XML_TAG, content, XmlErrorBundle.message("named.element.is.not.closed", tagName));
                    content.drop();
                    return;
                }

                advance();
            }
            footer.drop();

            while (token() != XmlTokenType.XML_TAG_END && token() != XmlTokenType.XML_START_TAG_START && token() != XmlTokenType.XML_END_TAG_START && eof()) {
                error(XmlPsiBundle.message("xml.parsing.unexpected.token"));
                advance();
            }

            if (token() == XML_TAG_END) {
                advance();
            } else {
                error(XmlPsiBundle.message("xml.parsing.closing.tag.is.not.done"));
            }
        } else {
            error(XmlPsiBundle.message("xml.parsing.unexpected.end.of.file"));
        }

        content.drop();
        myTagNamesStack.pop();
        tag.done(XML_TAG);
    }

    @Nullable
    private String parseTagHeader(final PsiBuilder.Marker tag) {
        advance();

        final String tagName;
        if (token() != XML_NAME || myBuilder.rawLookup(-1) == TokenType.WHITE_SPACE) {
            error(XmlPsiBundle.message("xml.parsing.tag.name.expected"));
            tagName = "";
        } else {
            tagName = myBuilder.getTokenText();
            assert tagName != null;
            advance();
        }
        myTagNamesStack.push(tagName);

        do {
            final IElementType tt = token();
            if (tt == XML_NAME) {
                parseAttribute();
            } else if (tt == XML_CHAR_ENTITY_REF || tt == XML_ENTITY_REF_TOKEN) {
                parseReference();
            } else {
                break;
            }
        }
        while (true);

        if (token() == XML_EMPTY_ELEMENT_END) {
            advance();
            myTagNamesStack.pop();
            tag.done(XML_TAG);
            return null;
        }

        if (token() == XML_TAG_END) {
            advance();
        } else {
            error(XmlPsiBundle.message("tag.start.is.not.closed"));
            myTagNamesStack.pop();
            tag.done(XML_TAG);
            return null;
        }

        if (myTagNamesStack.size() > BALANCING_DEPTH_THRESHOLD) {
            error(XmlPsiBundle.message("way.too.unbalanced"));
            tag.done(XML_TAG);
            return null;
        }

        return tagName;
    }

    public void parseTagContent() {
        PsiBuilder.Marker xmlText = null;
        while (true) {
            final IElementType tt = token();
            if (tt == null || tt == XML_END_TAG_START) {
                break;
            }

            if (tt == XML_START_TAG_START) {
                xmlText = terminateText(xmlText);
                parseTag();
            } else if (tt == XML_PI_START) {
                xmlText = terminateText(xmlText);
                parseProcessingInstruction();
            } else if (tt == XML_ENTITY_REF_TOKEN) {
                xmlText = terminateText(xmlText);
                parseReference();
            } else if (tt == XML_CHAR_ENTITY_REF) {
                xmlText = startText(xmlText);
                parseReference();
            } else if (tt == XML_CDATA_START) {
                xmlText = startText(xmlText);
                parseCData();
            } else if (isCommentToken(tt)) {
                xmlText = terminateText(xmlText);
                parseComment();
            } else if (tt == XML_BAD_CHARACTER) {
                xmlText = startText(xmlText);
                final PsiBuilder.Marker error = mark();
                advance();
                error.error(XmlPsiBundle.message("unescaped.ampersand.or.nonterminated.character.entity.reference"));
            } else if (tt instanceof ICustomParsingType || tt instanceof ILazyParseableElementType) {
                xmlText = terminateText(xmlText);
                advance();
            } else {
                xmlText = startText(xmlText);
                advance();
            }
        }

        terminateText(xmlText);
    }

    protected boolean isCommentToken(final IElementType tt) {
        return tt == XML_COMMENT_START;
    }

    @NotNull
    private PsiBuilder.Marker startText(@Nullable PsiBuilder.Marker xmlText) {
        if (xmlText == null) {
            xmlText = mark();
        }
        return xmlText;
    }

    protected final PsiBuilder.Marker mark() {
        return myBuilder.mark();
    }

    private void parseCData() {
        assert token() == XML_CDATA_START;
        final PsiBuilder.Marker cdata = mark();
        while (token() != XML_CDATA_END && eof()) {
            advance();
        }

        if (eof()) {
            advance();
        }

        cdata.done(XML_CDATA);
    }

    protected void parseComment() {
        final PsiBuilder.Marker comment = mark();
        advance();
        while (true) {
            final IElementType tt = token();
            if (tt == XML_COMMENT_CHARACTERS || tt == XML_CONDITIONAL_COMMENT_START
                    || tt == XML_CONDITIONAL_COMMENT_START_END || tt == XML_CONDITIONAL_COMMENT_END_START
                    || tt == XML_CONDITIONAL_COMMENT_END) {
                advance();
                continue;
            } else if (tt == XML_BAD_CHARACTER) {
                final PsiBuilder.Marker error = mark();
                advance();
                error.error(XmlPsiBundle.message("xml.parsing.bad.character"));
                continue;
            }
            if (tt == XML_COMMENT_END) {
                advance();
            }
            break;
        }
        comment.done(XML_COMMENT);
    }

    private void parseReference() {
        if (token() == XML_CHAR_ENTITY_REF) {
            advance();
        } else if (token() == XML_ENTITY_REF_TOKEN) {
            final PsiBuilder.Marker ref = mark();
            advance();
            ref.done(XML_ENTITY_REF);
        } else {
            assert false : "Unexpected token";
        }
    }

    private void parseAttribute() {
        assert token() == XML_NAME;
        final PsiBuilder.Marker att = mark();
        advance();
        if (token() == XML_EQ) {
            advance();
            parseAttributeValue();
        }
        att.done(XML_ATTRIBUTE);
    }

    private void parseAttributeValue() {
        final PsiBuilder.Marker attValue = mark();
        if (token() == XML_ATTRIBUTE_VALUE_START_DELIMITER) {
            while (true) {
                final IElementType tt = token();
                if (tt == null || tt == XML_ATTRIBUTE_VALUE_END_DELIMITER || tt == XML_END_TAG_START || tt == XML_EMPTY_ELEMENT_END ||
                        tt == XML_START_TAG_START) {
                    break;
                }

                if (tt == XML_BAD_CHARACTER) {
                    final PsiBuilder.Marker error = mark();
                    advance();
                    error.error(
                            XmlPsiBundle.message("unescaped.ampersand.or.nonterminated.character.entity.reference"));
                } else if (tt == XML_ENTITY_REF_TOKEN) {
                    parseReference();
                } else {
                    advance();
                }
            }

            if (token() == XML_ATTRIBUTE_VALUE_END_DELIMITER) {
                advance();
            } else {
                error(XmlPsiBundle.message("xml.parsing.unclosed.attribute.value"));
            }
        } else {
            error(XmlPsiBundle.message("xml.parsing.attribute.value.expected"));
        }

        attValue.done(XML_ATTRIBUTE_VALUE);
    }

    private void parseProcessingInstruction() {
        assert token() == XML_PI_START;
        final PsiBuilder.Marker pi = mark();
        advance();
        if (token() != XML_NAME) {
            error(XmlPsiBundle.message("xml.parsing.processing.instruction.name.expected"));
        } else {
            advance();
        }

        final IElementType tokenType = token();
        if (tokenType == XML_TAG_CHARACTERS) {
            while (token() == XML_TAG_CHARACTERS) {
                advance();
            }
        } else {
            while (token() == XML_NAME) {
                advance();
                if (token() == XML_EQ) {
                    advance();
                }
            }
        }

        if (token() == XML_PI_END) {
            advance();
        } else {
            error(XmlPsiBundle.message("xml.parsing.unterminated.processing.instruction"));
        }

        pi.done(XML_PROCESSING_INSTRUCTION);
    }

    @Nullable
    protected final IElementType token() {
        return myBuilder.getTokenType();
    }

    protected final boolean eof() {
        return !myBuilder.eof();
    }

    protected final void advance() {
        myBuilder.advanceLexer();
    }

    private void error(@NotNull String message) {
        myBuilder.error(message);
    }
}
