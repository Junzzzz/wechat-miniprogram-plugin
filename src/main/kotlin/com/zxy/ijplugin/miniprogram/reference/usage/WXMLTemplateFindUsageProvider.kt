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

package com.zxy.ijplugin.miniprogram.reference.usage

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lexer.XmlLexer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTokenType

class WXMLTemplateFindUsageProvider : FindUsagesProvider {


    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(
            XmlLexer(), TokenSet.create(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN),
            XmlTokenType.COMMENTS,
            TokenSet.create(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN),
            TokenSet.andNot(TokenSet.ANY, TokenSet.create(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN))
        )
    }

    override fun getNodeText(psiElement: PsiElement, p1: Boolean): String {
        return (psiElement as? PsiNamedElement)?.name ?: ""
    }

    override fun getDescriptiveName(psiElement: PsiElement): String {
        return this.getNodeText(psiElement, false)
    }

    override fun getType(psiElement: PsiElement): String {
        if (psiElement is XmlAttributeValue) {
            val attributeName = (psiElement.parent as? XmlAttribute)?.name
            return if (attributeName == "name") "template definition" else "template usage"
        }
        return ""
    }

    override fun getHelpId(p0: PsiElement): String? {
        return HelpID.FIND_OTHER_USAGES
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is XmlAttributeValue
    }
}