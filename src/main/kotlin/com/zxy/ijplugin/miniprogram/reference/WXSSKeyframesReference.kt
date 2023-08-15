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

package com.zxy.ijplugin.miniprogram.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.css.CssKeyframesRule
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.miniprogram.utils.findChildrenOfType

/**
 * 动画的属性值的引用
 */
class WXSSKeyframesReference(wxssValue: PsiElement) : PsiReferenceBase<PsiElement>(wxssValue) {

    /**
     * 关键帧可能会定义多个但以最近的一处为主
     */
    override fun resolve(): PsiElement? {
        val wxssFiles = WXSSModuleUtils.findImportedFilesWithSelf(
            this.element.containingFile as? WXSSPsiFile ?: return null
        )
        return wxssFiles.asSequence().flatMap { wxssPsiFile ->
            wxssPsiFile.findChildrenOfType<CssKeyframesRule>().filter {
                it.name == this.value
            }.asSequence()
        }.firstOrNull()
    }

}