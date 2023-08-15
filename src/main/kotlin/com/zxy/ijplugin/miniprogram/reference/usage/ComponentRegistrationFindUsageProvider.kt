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

import com.intellij.json.psi.JsonProperty
import com.intellij.lang.HelpID
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement

class ComponentRegistrationFindUsageProvider : FindUsagesProvider {

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return (element as? JsonProperty)?.name ?: ""
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return this.getNodeText(element, false)
    }

    override fun getType(element: PsiElement): String {
        return if (element is JsonProperty) "component registration" else ""
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return HelpID.FIND_OTHER_USAGES
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is JsonProperty
    }
}