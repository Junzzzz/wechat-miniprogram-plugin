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

package com.zxy.ijplugin.miniprogram.navigation

import com.intellij.json.JsonFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.navigation.GotoRelatedItem
import com.intellij.navigation.GotoRelatedProvider
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.zxy.ijplugin.miniprogram.context.RelateFileHolder
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.localization.message
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.qq.QSSFileType

/**
 * 找到一个组件或页面的相关文件
 */
class WechatMiniProgramGotoRelatedProvider : GotoRelatedProvider() {

    override fun getItems(dataContext: DataContext): MutableList<out GotoRelatedItem> {
        val project = dataContext.getData(CommonDataKeys.PROJECT)
        if (project != null && isWechatMiniProgramContext(project)) {
            val virtualFile = dataContext.getData(LangDataKeys.VIRTUAL_FILE)
            if (virtualFile != null) {
                val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return mutableListOf()
                val currentFileHolder = RelateFileHolder.findInstance(psiFile) ?: return mutableListOf()
                if (currentFileHolder.findFile(psiFile) == psiFile) {
                    // 当前文件是正确的组件文件

                    return RelateFileHolder.INSTANCES.filter {
                        it != currentFileHolder
                    }.mapNotNull {
                        // 寻找其他类型的文件
                        it.findFile(psiFile)
                    }.mapNotNull {
                        MyGotoRelatedItem.create(it)
                    }.toMutableList()
                }
            }
        }
        return mutableListOf()
    }

    class MyGotoRelatedItem(
        element: PsiElement, private val name: String, private val filename: String, mnemonic: Int
    ) :
        GotoRelatedItem(
            element,
            message("goto.related.group.name", message(if (element.project.isQQContext()) "qq" else "weixin")),
            mnemonic
        ) {

        companion object {
            fun create(psiFile: PsiFile): MyGotoRelatedItem? = when (psiFile.fileType) {
                JavaScriptFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Script", psiFile.name, 1)
                WXMLFileType.INSTANCE, QMLFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Template", psiFile.name, 2)
                WXSSFileType.INSTANCE, QSSFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Styles", psiFile.name, 3)
                JsonFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Configurations", psiFile.name, 4)
                else -> null
            }

        }

        override fun getCustomName(): String? {
            return this.name
        }

        override fun getCustomContainerName(): String? {
            return "($filename)"
        }
    }

}