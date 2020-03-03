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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml.tag;

import com.intellij.application.options.CodeStyle;
import com.intellij.application.options.editor.WebEditorOptions;
import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.CompletionUtil;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.daemon.impl.quickfix.EmptyExpression;
import com.intellij.codeInsight.editorActions.XmlEditUtil;
import com.intellij.codeInsight.editorActions.XmlTagNameSynchronizer;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateEditingAdapter;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.MacroCallNode;
import com.intellij.codeInsight.template.macro.CompleteMacro;
import com.intellij.codeInsight.template.macro.CompleteSmartMacro;
import com.intellij.codeInspection.InspectionProfile;
import com.intellij.codeInspection.htmlInspections.XmlEntitiesInspection;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.HtmlCodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlCodeStyleSettings;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.xml.*;
import com.intellij.xml.XmlExtension.AttributeValuePresentation;
import com.intellij.xml.actions.GenerateXmlTagAction;
import com.intellij.xml.impl.schema.XmlElementDescriptorImpl;
import com.intellij.xml.util.HtmlUtil;
import com.intellij.xml.util.XmlUtil;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLAttributeInsertUtils;
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.WXMLUtils;
import kotlin.collections.ArraysKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * copy from {@see com.intellij.codeInsight.completion.XmlTagInsertHandler}
 */
public class WXMLTagInsertHandler implements InsertHandler<LookupElement> {
    public static final WXMLTagInsertHandler INSTANCE = new WXMLTagInsertHandler();

    public static void insertIncompleteTag(char completionChar,
                                           final Editor editor,
                                           XmlTag tag) {
        XmlTag originalElement = CompletionUtil.getOriginalElement(tag);
        XmlElementDescriptor descriptor = originalElement != null ? originalElement.getDescriptor() : tag.getDescriptor();
        if (descriptor == null) return;
        final Project project = editor.getProject();
        assert project != null;
        TemplateManager templateManager = TemplateManager.getInstance(project);
        Template template = templateManager.createTemplate("", "");

        template.setToIndent(true);

        // temp code
        PsiFile containingFile = tag.getContainingFile();
        boolean htmlCode = HtmlUtil.hasHtml(containingFile) || HtmlUtil.supportsXmlTypedHandlers(containingFile);
        template.setToReformat(!htmlCode);

        StringBuilder indirectRequiredAttrs = addRequiredAttributes(tag, template, containingFile);
        final boolean chooseAttributeName = addTail(completionChar, descriptor, htmlCode, tag, template,
                indirectRequiredAttrs);

        templateManager.startTemplate(editor, template, new TemplateEditingAdapter() {
            private RangeMarker myAttrValueMarker;

            @Override
            public void waitingForInput(Template template) {
                int offset = editor.getCaretModel().getOffset();
                myAttrValueMarker = editor.getDocument().createRangeMarker(offset + 1, offset + 4);
            }

            @Override
            public void templateFinished(@NotNull final Template template, boolean brokenOff) {
                final int offset = editor.getCaretModel().getOffset();

                if (chooseAttributeName && offset > 0) {
                    char c = editor.getDocument().getCharsSequence().charAt(offset - 1);
                    if (c == '/' || (c == ' ' && brokenOff)) {
                        WriteCommandAction.writeCommandAction(project).run(
                                () -> editor.getDocument().replaceString(offset, offset + 3, ">"));
                    }
                }
            }

            @Override
            public void templateCancelled(final Template template) {
                if (myAttrValueMarker == null) {
                    return;
                }

                if (UndoManager.getInstance(project).isUndoOrRedoInProgress()) {
                    return;
                }

                if (chooseAttributeName && myAttrValueMarker.isValid()) {
                    final int startOffset = myAttrValueMarker.getStartOffset();
                    final int endOffset = myAttrValueMarker.getEndOffset();
                    WriteCommandAction.writeCommandAction(project).run(
                            () -> editor.getDocument().replaceString(startOffset, endOffset, ">"));
                }
            }
        });
    }

    @Nullable
    private static StringBuilder addRequiredAttributes(@Nullable XmlTag tag,
                                                       Template template,
                                                       PsiFile containingFile) {

        Set<String> notRequiredAttributes = Collections.emptySet();

        if (tag instanceof HtmlTag) {
            final InspectionProfile profile = InspectionProjectProfileManager.getInstance(
                    tag.getProject()).getCurrentProfile();
            XmlEntitiesInspection inspection = (XmlEntitiesInspection) profile.getUnwrappedTool(
                    XmlEntitiesInspection.REQUIRED_ATTRIBUTES_SHORT_NAME, tag);

            if (inspection != null) {
                StringTokenizer tokenizer = new StringTokenizer(inspection.getAdditionalEntries());
                notRequiredAttributes = new HashSet<>();

                while (tokenizer.hasMoreElements()) notRequiredAttributes.add(tokenizer.nextToken());
            }
        }


        XmlAttributeDescriptor[] attributes = WXMLUtils.getWXMLAttributeDescriptors(tag);

        StringBuilder indirectRequiredAttrs = null;

        if (WebEditorOptions.getInstance().isAutomaticallyInsertRequiredAttributes()) {
            final XmlExtension extension = XmlExtension.getExtension(containingFile);

            for (XmlAttributeDescriptor attributeDecl : attributes) {
                String attributeName = attributeDecl.getName(tag);

                boolean shouldBeInserted = extension.shouldBeInserted(attributeDecl);
                if (!shouldBeInserted) continue;

                AttributeValuePresentation presenter =
                        extension.getAttributeValuePresentation(tag, attributeName,
                                XmlEditUtil.getAttributeQuote(containingFile));
                boolean htmlCode = HtmlUtil.hasHtml(containingFile) || HtmlUtil.supportsXmlTypedHandlers(
                        containingFile);
                if (tag == null || tag.getAttributeValue(attributeName) == null) {
                    if (!notRequiredAttributes.contains(attributeName)) {
                        if (!extension.isIndirectSyntax(attributeDecl)) {
                            if (WXMLAttributeInsertUtils.isBooleanTypeAttribute(attributeDecl) && Objects.equals(
                                    attributeDecl.getDefaultValue(), "false")) {
                                // Boolean类型，且默认值为false，之后不需要接等号
                                template.addTextSegment(" " + attributeName);
                            } else {
                                template.addTextSegment(" " + attributeName + "=" + presenter.getPrefix());
                                template.addVariable(presenter.showAutoPopup() ? new MacroCallNode(
                                        new CompleteMacro()) : new EmptyExpression(), true);
                                template.addTextSegment(presenter.getPostfix());
                            }
                        } else {
                            if (indirectRequiredAttrs == null) indirectRequiredAttrs = new StringBuilder();
                            indirectRequiredAttrs.append("\n<jsp:attribute name=\"").append(attributeName).append(
                                    "\"></jsp:attribute>\n");
                        }
                    }
                } else if (attributeDecl.isFixed() && attributeDecl.getDefaultValue() != null && !htmlCode) {
                    template.addTextSegment(" " + attributeName + "=" +
                            presenter.getPrefix() + attributeDecl.getDefaultValue() + presenter.getPostfix());
                }
            }
        }
        return indirectRequiredAttrs;
    }

    protected static boolean addTail(char completionChar,
                                     XmlElementDescriptor descriptor,
                                     boolean isHtmlCode,
                                     XmlTag tag,
                                     Template template,
                                     StringBuilder indirectRequiredAttrs) {
        if (completionChar == '>' || (completionChar == '/' && indirectRequiredAttrs != null)) {
            template.addTextSegment(">");
            boolean toInsertCDataEnd = false;

            if (descriptor instanceof XmlElementDescriptorWithCDataContent) {
                final XmlElementDescriptorWithCDataContent cDataContainer = (XmlElementDescriptorWithCDataContent) descriptor;

                if (cDataContainer.requiresCdataBracesInContext(tag)) {
                    template.addTextSegment("<![CDATA[\n");
                    toInsertCDataEnd = true;
                }
            }

            if (indirectRequiredAttrs != null) template.addTextSegment(indirectRequiredAttrs.toString());
            template.addEndVariable();

            if (toInsertCDataEnd) template.addTextSegment("\n]]>");

            if ((!(tag instanceof HtmlTag) || !HtmlUtil.isSingleHtmlTag(tag,
                    true)) && tag.getAttributes().length == 0) {
                if (WebEditorOptions.getInstance().isAutomaticallyInsertClosingTag()) {
                    final String name = descriptor.getName(tag);
                    if (name != null) {
                        template.addTextSegment("</");
                        template.addTextSegment(name);
                        template.addTextSegment(">");
                    }
                }
            }
        } else if (completionChar == '/') {
            template.addTextSegment(closeTag(tag));
        } else if (completionChar == ' ' && template.getSegmentsCount() == 0) {
            if (WebEditorOptions.getInstance().isAutomaticallyStartAttribute() &&
                    (descriptor.getAttributesDescriptors(tag).length > 0 || isTagFromHtml(
                            tag) && !HtmlUtil.isTagWithoutAttributes(tag.getName()))) {
                completeAttribute(tag.getContainingFile(), template);
                return true;
            }
        } else if (completionChar == Lookup.AUTO_INSERT_SELECT_CHAR || completionChar == Lookup.NORMAL_SELECT_CHAR || completionChar == Lookup.REPLACE_SELECT_CHAR) {
            if (WebEditorOptions.getInstance().isAutomaticallyInsertClosingTag() && isHtmlCode && HtmlUtil.isSingleHtmlTag(
                    tag, true)) {
                template.addTextSegment(HtmlUtil.isHtmlTag(tag) ? ">" : closeTag(tag));
            } else {
                if (needAlLeastOneAttribute(
                        tag) && WebEditorOptions.getInstance().isAutomaticallyStartAttribute() && tag.getAttributes().length == 0
                        && template.getSegmentsCount() == 0) {
                    completeAttribute(tag.getContainingFile(), template);
                    return true;
                } else {
                    completeTagTail(template, descriptor, tag.getContainingFile(), tag, true);
                }
            }
        }

        return false;
    }

    @NotNull
    private static String closeTag(XmlTag tag) {
        CodeStyleSettings settings = CodeStyle.getSettings(tag.getContainingFile());
        boolean html = HtmlUtil.isHtmlTag(tag);
        boolean needsSpace = (html && settings.getCustomSettings(
                HtmlCodeStyleSettings.class).HTML_SPACE_INSIDE_EMPTY_TAG) ||
                (!html && settings.getCustomSettings(XmlCodeStyleSettings.class).XML_SPACE_INSIDE_EMPTY_TAG);
        return needsSpace ? " />" : "/>";
    }

    private static void completeAttribute(PsiFile file, Template template) {
        template.addTextSegment(" ");
        template.addVariable(new MacroCallNode(new CompleteMacro()), true);
        template.addTextSegment("=" + XmlEditUtil.getAttributeQuote(file));
        template.addEndVariable();
        template.addTextSegment(XmlEditUtil.getAttributeQuote(file));
    }

    private static boolean needAlLeastOneAttribute(XmlTag tag) {
        for (XmlTagRuleProvider ruleProvider : XmlTagRuleProvider.EP_NAME.getExtensionList()) {
            for (XmlTagRuleProvider.Rule rule : ruleProvider.getTagRule(tag)) {
                if (rule.needAtLeastOneAttribute(tag)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean addRequiredSubTags(Template template, XmlElementDescriptor descriptor, PsiFile file,
                                              XmlTag context) {

        if (!WebEditorOptions.getInstance().isAutomaticallyInsertRequiredSubTags()) return false;
        List<XmlElementDescriptor> requiredSubTags = GenerateXmlTagAction.getRequiredSubTags(descriptor);
        if (!requiredSubTags.isEmpty()) {
            template.addTextSegment(">");
            template.setToReformat(true);
        }
        for (XmlElementDescriptor subTag : requiredSubTags) {
            if (subTag == null) { // placeholder for smart completion
                template.addTextSegment("<");
                template.addVariable(new MacroCallNode(new CompleteSmartMacro()), true);
                continue;
            }
            String qname = subTag.getName();
            if (subTag instanceof XmlElementDescriptorImpl) {
                String prefixByNamespace = context.getPrefixByNamespace(
                        ((XmlElementDescriptorImpl) subTag).getNamespace());
                if (StringUtil.isNotEmpty(prefixByNamespace)) {
                    qname = prefixByNamespace + ":" + subTag.getName();
                }
            }
            template.addTextSegment("<" + qname);
            addRequiredAttributes(null, template, file);
            completeTagTail(template, subTag, file, context, false);
        }
        if (!requiredSubTags.isEmpty()) {
            addTagEnd(template, descriptor, context);
        }
        return !requiredSubTags.isEmpty();
    }

    private static void completeTagTail(Template template, XmlElementDescriptor descriptor, PsiFile file,
                                        XmlTag context, boolean firstLevel) {
        boolean completeIt = !firstLevel || !canHaveAttributes(descriptor, context);
        switch (descriptor.getContentType()) {
            case XmlElementDescriptor.CONTENT_TYPE_UNKNOWN:
                return;
            case XmlElementDescriptor.CONTENT_TYPE_EMPTY:
                if (completeIt) {
                    template.addTextSegment(closeTag(context));
                }
                break;
            case XmlElementDescriptor.CONTENT_TYPE_MIXED:
                if (completeIt) {
                    template.addTextSegment(">");
                    if (firstLevel) {
                        template.addEndVariable();
                    } else {
                        template.addVariable(new MacroCallNode(new CompleteMacro()), true);
                    }
                    addTagEnd(template, descriptor, context);
                }
                break;
            default:
                if (!addRequiredSubTags(template, descriptor, file, context)) {
                    if (completeIt) {
                        template.addTextSegment(">");
                        template.addEndVariable();
                        addTagEnd(template, descriptor, context);
                    }
                }
                break;
        }
    }

    private static boolean canHaveAttributes(XmlElementDescriptor descriptor, XmlTag context) {
        XmlAttributeDescriptor[] attributes = descriptor.getAttributesDescriptors(context);
        int required = WebEditorOptions.getInstance().isAutomaticallyInsertRequiredAttributes() ?
                ArraysKt.count(attributes,
                        (attribute) -> attribute.isRequired() && context.getAttribute(attribute.getName()) == null) :
                0;
        return attributes.length - required > 0;
    }

    private static void addTagEnd(Template template, XmlElementDescriptor descriptor, XmlTag context) {
        template.addTextSegment("</" + descriptor.getName(context) + ">");
    }

    private static boolean isTagFromHtml(final XmlTag tag) {
        final String ns = tag.getNamespace();
        return XmlUtil.XHTML_URI.equals(ns) || XmlUtil.HTML_URI.equals(ns);
    }

    @Override
    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
        Project project = context.getProject();
        Editor editor = context.getEditor();
        int startOffset = context.getStartOffset();
        Document document = getTopLevelEditor(editor).getDocument();
        Ref<PsiElement> currentElementRef = Ref.create();
        // Need to insert " " to prevent creating tags like <tagThis is my text
        XmlTagNameSynchronizer.runWithoutCancellingSyncTagsEditing(document, () -> {
            final int offset = editor.getCaretModel().getOffset();
            editor.getDocument().insertString(offset, " ");
            PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
            currentElementRef.set(context.getFile().findElementAt(startOffset));
            editor.getDocument().deleteString(offset, offset + 1);
        });

        final XmlTag tag = PsiTreeUtil.getContextOfType(currentElementRef.get(), XmlTag.class, true);

        if (tag == null) return;

        if (context.getCompletionChar() != Lookup.COMPLETE_STATEMENT_SELECT_CHAR) {
            context.setAddCompletionChar(false);
        }

        if (XmlUtil.getTokenOfType(tag, XmlTokenType.XML_TAG_END) == null &&
                XmlUtil.getTokenOfType(tag, XmlTokenType.XML_EMPTY_ELEMENT_END) == null) {

            insertIncompleteTag(context.getCompletionChar(), editor, tag);
        } else if (context.getCompletionChar() == Lookup.REPLACE_SELECT_CHAR) {
            PsiDocumentManager.getInstance(project).commitAllDocuments();

            int caretOffset = editor.getCaretModel().getOffset();

            XmlTag otherTag = PsiTreeUtil.getParentOfType(context.getFile().findElementAt(caretOffset), XmlTag.class);

            PsiElement endTagStart = XmlUtil.getTokenOfType(otherTag, XmlTokenType.XML_END_TAG_START);

            if (endTagStart != null) {
                PsiElement sibling = endTagStart.getNextSibling();

                assert sibling != null;
                ASTNode node = sibling.getNode();
                assert node != null;
                if (node.getElementType() == XmlTokenType.XML_NAME) {
                    int sOffset = sibling.getTextRange().getStartOffset();
                    int eOffset = sibling.getTextRange().getEndOffset();

                    editor.getDocument().deleteString(sOffset, eOffset);
                    editor.getDocument().insertString(sOffset, otherTag.getName());
                }
            }

            editor.getCaretModel().moveToOffset(caretOffset + 1);
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
            editor.getSelectionModel().removeSelection();
        }

        if (context.getCompletionChar() == ' ' && TemplateManager.getInstance(project).getActiveTemplate(
                editor) != null) {
            return;
        }

        final TailType tailType = LookupItem.handleCompletionChar(editor, item, context.getCompletionChar());
        tailType.processTail(editor, editor.getCaretModel().getOffset());
    }

    private Editor getTopLevelEditor(Editor editor) {
        return editor instanceof EditorWindow ? ((EditorWindow) editor).getDelegate() : editor;
    }
}