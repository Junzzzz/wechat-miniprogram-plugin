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

import com.intellij.codeInsight.completion.CompletionUtilCore;
import com.intellij.lang.HtmlScriptContentProvider;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageHtmlScriptContentProvider;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.EmbeddedTokenTypesProvider;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.text.CharArrayUtil;
import com.intellij.xml.util.documentation.HtmlDescriptorsTable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Maxim.Mossienko
 */
public abstract class BaseHtmlLexer extends DelegateLexer {
    protected static final int BASE_STATE_MASK = 0x3F;
    private static final int SEEN_TAG = 0x40;
    private static final int SEEN_ATTRIBUTE = 0x80;
    private static final int SEEN_CONTENT_TYPE = 0x100;
    private static final int SEEN_STYLESHEET_TYPE = 0x200;
    private static final int SEEN_STYLE_SCRIPT_SHIFT = 10;
    private static final int SEEN_STYLE_SCRIPT_MASK = 0x7 << SEEN_STYLE_SCRIPT_SHIFT;
    protected static final int BASE_STATE_SHIFT = 13;
    @Nullable
    protected static final Language ourDefaultLanguage = Language.findLanguageByID("JavaScript");
    @Nullable
    protected static final Language ourDefaultStyleLanguage = Language.findLanguageByID("CSS");

    protected boolean seenTag;
    protected boolean seenAttribute;
    protected boolean seenStyle;
    protected boolean seenScript;
    static final TokenSet TOKENS_TO_MERGE = TokenSet.create(XmlTokenType.XML_COMMENT_CHARACTERS, XmlTokenType.XML_WHITE_SPACE, XmlTokenType.XML_REAL_WHITE_SPACE,
            XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN, XmlTokenType.XML_DATA_CHARACTERS,
            XmlTokenType.XML_TAG_CHARACTERS);
    private static final char SCRIPT = 1;
    private static final char STYLE = 2;

    @Nullable
    protected String scriptType = null;
    @Nullable
    protected String styleType = null;
    private final int[] scriptStyleStack = new int[]{0, 0};
    protected boolean seenContentType;
    protected boolean seenStylesheetType;
    private CharSequence cachedBufferSequence;
    private Lexer lexerOfCacheBufferSequence;
    private final boolean caseInsensitive;
    private final HashMap<IElementType, TokenHandler> tokenHandlers = new HashMap<>();

    protected BaseHtmlLexer(Lexer _baseLexer, boolean _caseInsensitive) {
        super(_baseLexer);
        caseInsensitive = _caseInsensitive;

        XmlNameHandler value = new XmlNameHandler();
        tokenHandlers.put(XmlTokenType.XML_NAME, value);
        tokenHandlers.put(XmlTokenType.XML_TAG_NAME, value);
        tokenHandlers.put(XmlTokenType.XML_TAG_END, new XmlTagClosedHandler());
        tokenHandlers.put(XmlTokenType.XML_END_TAG_START, new XmlTagEndHandler());
        tokenHandlers.put(XmlTokenType.XML_EMPTY_ELEMENT_END, new XmlTagEndHandler());
        tokenHandlers.put(XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER, new XmlAttributeValueEndHandler());
        tokenHandlers.put(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN, new XmlAttributeValueHandler());
    }

    @Nullable
    protected IElementType getCurrentStylesheetElementType() {
        Language language = getStyleLanguage();
        if (language != null) {
            for (EmbeddedTokenTypesProvider provider : EmbeddedTokenTypesProvider.EXTENSION_POINT_NAME.getPoint(null).getExtensions()) {
                IElementType elementType = provider.getElementType();
                if (language.is(elementType.getLanguage())) {
                    return elementType;
                }
            }
        }
        return null;
    }

    protected void pushScriptStyle(boolean script, boolean style) {
        int position = scriptStyleStack[0] == 0 ? 0 : 1;
        scriptStyleStack[position] = script ? SCRIPT :
                style ? STYLE :
                        0;
        seenStyle = style;
        seenScript = script;
    }

    protected void popScriptStyle() {
        int position = scriptStyleStack[1] == 0 ? 0 : 1;
        scriptStyleStack[position] = 0;
        seenStyle = scriptStyleStack[0] == STYLE;
        seenScript = scriptStyleStack[0] == SCRIPT;
    }

    @Nullable
    protected HtmlScriptContentProvider findScriptContentProvider(@Nullable String mimeType) {
        if (StringUtil.isEmpty(mimeType)) {
            return ourDefaultLanguage != null ? LanguageHtmlScriptContentProvider.getScriptContentProvider(ourDefaultLanguage) : null;
        }
        Collection<Language> instancesByMimeType = Language.findInstancesByMimeType(mimeType.trim());
        if (instancesByMimeType.isEmpty() && mimeType.contains("template")) {
            instancesByMimeType = Collections.singletonList(HTMLLanguage.INSTANCE);
        }
        for (Language language : instancesByMimeType) {
            HtmlScriptContentProvider scriptContentProvider = LanguageHtmlScriptContentProvider.getScriptContentProvider(language);
            if (scriptContentProvider != null) {
                return scriptContentProvider;
            }
        }
        return null;
    }

    @Nullable
    protected Language getScriptLanguage() {
        Collection<Language> instancesByMimeType = Language.findInstancesByMimeType(scriptType != null ? scriptType.trim() : null);
        return instancesByMimeType.isEmpty() ? null : instancesByMimeType.iterator().next();
    }

    @Nullable
    protected Language getStyleLanguage() {
        if (ourDefaultStyleLanguage != null && styleType != null && !"text/css".equals(styleType)) {
            for (Language language : ourDefaultStyleLanguage.getDialects()) {
                for (String mimeType : language.getMimeTypes()) {
                    if (styleType.equals(mimeType)) {
                        return language;
                    }
                }
            }
        }
        return ourDefaultStyleLanguage;
    }

    @Nullable
    protected IElementType getCurrentScriptElementType() {
        HtmlScriptContentProvider scriptContentProvider = findScriptContentProvider(scriptType);
        return scriptContentProvider == null ? null : scriptContentProvider.getScriptElementType();
    }

    protected void registerHandler(IElementType elementType, TokenHandler value) {
        final TokenHandler tokenHandler = tokenHandlers.get(elementType);

        if (tokenHandler != null) {
            final TokenHandler newHandler = value;
            value = new TokenHandler() {
                @Override
                public void handleElement(final Lexer lexer) {
                    tokenHandler.handleElement(lexer);
                    newHandler.handleElement(lexer);
                }
            };
        }

        tokenHandlers.put(elementType, value);
    }

    public interface TokenHandler {
        void handleElement(Lexer lexer);
    }

    public class XmlNameHandler implements TokenHandler {
        @NonNls
        private static final String TOKEN_SCRIPT = "wxs";
        @NonNls
        private static final String TOKEN_STYLE = "style";
        @NonNls
        private static final String TOKEN_ON = "on";

        @Override
        public void handleElement(Lexer lexer) {
            final CharSequence buffer;
            if (lexerOfCacheBufferSequence == lexer) {
                buffer = cachedBufferSequence;
            } else {
                cachedBufferSequence = lexer.getBufferSequence();
                buffer = cachedBufferSequence;
                lexerOfCacheBufferSequence = lexer;
            }
            final char firstCh = buffer.charAt(lexer.getTokenStart());

            if (seenScript && !seenTag) {
                seenContentType = false;
                if (((firstCh == 'l' || firstCh == 't') || (caseInsensitive && (firstCh == 'L' || firstCh == 'T')))) {
                    @NonNls String name = TreeUtil.getTokenText(lexer);
                    seenContentType = Comparing.strEqual("language", name, !caseInsensitive) || Comparing.strEqual("type", name, !caseInsensitive);
                    return;
                }
            }
            if (seenStyle && !seenTag) {
                seenStylesheetType = false;
                if (firstCh == 't' || caseInsensitive && firstCh == 'T') {
                    seenStylesheetType = Comparing.strEqual(TreeUtil.getTokenText(lexer), "type", !caseInsensitive);
                    return;
                }
            }

            if (firstCh != 's' && firstCh != 'w' && (!caseInsensitive || (firstCh != 'S' && firstCh != 'W'))) {
                return; // optimization
            }

            String name = TreeUtil.getTokenText(lexer);
            if (caseInsensitive) name = StringUtil.toLowerCase(name);

            final boolean style = name.equals(TOKEN_STYLE);
            final int state = getState() & BASE_STATE_MASK;
            final boolean script = name.equals(TOKEN_SCRIPT) ||
                    ((name.startsWith(TOKEN_ON) && name.indexOf(':') == -1 && !isHtmlTagState(state) &&
                            HtmlDescriptorsTable.getAttributeDescriptor(name) != null));

            if (style || script) {
                // encountered tag name in end of tag
                if (seenTag) {
                    if (isHtmlTagState(state)) {
                        seenTag = false;
                    }
                    return;
                }

                // If we have seenAttribute it means that we need to pop state
                if (seenAttribute) {
                    popScriptStyle();
                }
                pushScriptStyle(script, style);

                if (!isHtmlTagState(state)) {
                    seenAttribute = true;
                }
            }
        }
    }

    class XmlAttributeValueEndHandler implements TokenHandler {
        @Override
        public void handleElement(Lexer lexer) {
            if (seenAttribute) {
                popScriptStyle();
                seenAttribute = false;
            }
            seenContentType = false;
            seenStylesheetType = false;
        }
    }

    class XmlAttributeValueHandler implements TokenHandler {
        @Override
        public void handleElement(Lexer lexer) {
            if (seenContentType && seenScript && !seenAttribute) {
                @NonNls String mimeType = TreeUtil.getTokenText(lexer);
                scriptType = caseInsensitive ? StringUtil.toLowerCase(mimeType) : mimeType;
            }
            if (seenStylesheetType && seenStyle && !seenAttribute) {
                @NonNls String type = TreeUtil.getTokenText(lexer).trim();
                styleType = caseInsensitive ? StringUtil.toLowerCase(type) : type;
            }
        }
    }

    class XmlTagClosedHandler implements TokenHandler {
        @Override
        public void handleElement(Lexer lexer) {
            if (seenAttribute) {
                popScriptStyle();
                seenAttribute = false;
            } else {
                if (seenStyle || seenScript) {
                    seenTag = true;
                }
            }
        }
    }

    class XmlTagEndHandler implements TokenHandler {
        @Override
        public void handleElement(Lexer lexer) {
            popScriptStyle();
            seenAttribute = false;
            seenContentType = false;
            seenStylesheetType = false;
            scriptType = null;
            styleType = null;
        }
    }

    @Override
    public void start(@NotNull final CharSequence buffer, final int startOffset, final int endOffset, final int initialState) {
        initState(initialState);
        super.start(buffer, startOffset, endOffset, initialState & BASE_STATE_MASK);
    }

    private void initState(final int initialState) {
        seenTag = (initialState & SEEN_TAG) != 0;
        seenAttribute = (initialState & SEEN_ATTRIBUTE) != 0;
        seenContentType = (initialState & SEEN_CONTENT_TYPE) != 0;
        seenStylesheetType = (initialState & SEEN_STYLESHEET_TYPE) != 0;
        decodeScriptStack(((initialState & SEEN_STYLE_SCRIPT_MASK) >> SEEN_STYLE_SCRIPT_SHIFT));
        int position = scriptStyleStack[1] == 0 ? 0 : 1;
        seenStyle = scriptStyleStack[position] == STYLE;
        seenScript = scriptStyleStack[position] == SCRIPT;
        lexerOfCacheBufferSequence = null;
        cachedBufferSequence = null;
    }

    protected int skipToTheEndOfTheEmbeddment() {
        Lexer base = getDelegate();
        int tokenEnd = base.getTokenEnd();
        int lastState = 0;
        int lastStart = 0;

        final CharSequence buf = base.getBufferSequence();
        final char[] bufArray = CharArrayUtil.fromSequenceWithoutCopying(buf);

        if (seenTag) {
            FoundEnd:
            while (true) {
                FoundEndOfTag:
                while (base.getTokenType() != XmlTokenType.XML_END_TAG_START) {
                    if (base.getTokenType() == XmlTokenType.XML_COMMENT_CHARACTERS) {
                        // we should terminate on first occurence of </
                        final int end = base.getTokenEnd();

                        for (int i = base.getTokenStart(); i < end; ++i) {
                            if ((bufArray != null ? bufArray[i] : buf.charAt(i)) == '<' &&
                                    i + 1 < end &&
                                    (bufArray != null ? bufArray[i + 1] : buf.charAt(i + 1)) == '/') {
                                tokenEnd = i;
                                lastStart = i - 1;
                                lastState = 0;

                                break FoundEndOfTag;
                            }
                        }
                    }

                    lastState = base.getState();
                    tokenEnd = base.getTokenEnd();
                    lastStart = base.getTokenStart();
                    if (tokenEnd == getBufferEnd()) break FoundEnd;
                    base.advance();
                }

                // check if next is script
                if (base.getTokenType() != XmlTokenType.XML_END_TAG_START) { // we are inside comment
                    base.start(buf, lastStart + 1, getBufferEnd(), lastState);
                    base.getTokenType();
                }
                base.advance();

                while (XmlTokenType.WHITESPACES.contains(base.getTokenType())) {
                    base.advance();
                }

                if (base.getTokenType() == XmlTokenType.XML_NAME) {
                    String name = TreeUtil.getTokenText(base);
                    if (caseInsensitive) name = StringUtil.toLowerCase(name);

                    if (endOfTheEmbeddment(name)) {
                        break; // really found end
                    }
                }
            }

            base.start(buf, lastStart, getBufferEnd(), lastState);
            base.getTokenType();
        } else if (seenAttribute) {
            while (true) {
                if (!isValidAttributeValueTokenType(base.getTokenType())) break;

                tokenEnd = base.getTokenEnd();
                lastState = base.getState();
                lastStart = base.getTokenStart();

                if (tokenEnd == getBufferEnd()) break;
                base.advance();
            }

            base.start(buf, lastStart, getBufferEnd(), lastState);
            base.getTokenType();
        }
        return tokenEnd;
    }

    protected boolean endOfTheEmbeddment(String name) {
        return (hasSeenScript() && XmlNameHandler.TOKEN_SCRIPT.equals(name)) ||
                (hasSeenStyle() && XmlNameHandler.TOKEN_STYLE.equals(name)) ||
                CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED.equalsIgnoreCase(name);
    }

    protected boolean isValidAttributeValueTokenType(final IElementType tokenType) {
        return tokenType == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN ||
                tokenType == XmlTokenType.XML_ENTITY_REF_TOKEN ||
                tokenType == XmlTokenType.XML_CHAR_ENTITY_REF;
    }

    @Override
    public void advance() {
        super.advance();
        IElementType type = getDelegate().getTokenType();
        TokenHandler tokenHandler = tokenHandlers.get(type);
        if (tokenHandler != null) tokenHandler.handleElement(this);
    }


    @Override
    public int getState() {
        int state = super.getState();

        state |= ((seenTag) ? SEEN_TAG : 0);
        state |= ((seenAttribute) ? SEEN_ATTRIBUTE : 0);
        state |= ((seenContentType) ? SEEN_CONTENT_TYPE : 0);
        state |= ((seenStylesheetType) ? SEEN_STYLESHEET_TYPE : 0);
        state |= encodeScriptStack() << SEEN_STYLE_SCRIPT_SHIFT;

        return state;
    }

    private int encodeScriptStack() {
        if (scriptStyleStack[1] == 0) {
            return scriptStyleStack[0];
        }
        if (scriptStyleStack[0] == 0) {
            throw new IllegalStateException();
        }
        return scriptStyleStack[0] * 2 + scriptStyleStack[1];
    }

    private void decodeScriptStack(int value) {
        if (value <= 2) {
            scriptStyleStack[0] = value;
            scriptStyleStack[1] = 0;
        } else {
            value -= 3;
            scriptStyleStack[0] = (value / 2) + 1;
            scriptStyleStack[1] = (value % 2) + 1;
        }
    }

    protected final boolean hasSeenStyle() {
        return seenStyle;
    }

    protected final boolean hasSeenAttribute() {
        return seenAttribute;
    }

    protected final boolean hasSeenTag() {
        return seenTag;
    }

    protected boolean hasSeenScript() {
        return seenScript;
    }

    protected abstract boolean isHtmlTagState(int state);
}
