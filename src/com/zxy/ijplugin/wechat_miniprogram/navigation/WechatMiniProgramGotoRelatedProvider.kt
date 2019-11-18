package com.zxy.ijplugin.wechat_miniprogram.navigation

import com.intellij.json.JsonFileType
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.navigation.GotoRelatedItem
import com.intellij.navigation.GotoRelatedProvider
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.wechat_miniprogram.context.isWechatMiniProgramContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxs.WXSFileType

/**
 * 找到一个组件或页面的相关文件
 */
class WechatMiniProgramGotoRelatedProvider : GotoRelatedProvider() {

    companion object {
        const val GROUP_NAME = "Wechat Mini Program Component"
    }

    override fun getItems(dataContext: DataContext): MutableList<out GotoRelatedItem> {
        val project = dataContext.getData(CommonDataKeys.PROJECT)
        if (project != null && isWechatMiniProgramContext(project)) {
            val virtualFile = dataContext.getData(LangDataKeys.VIRTUAL_FILE)
            if (virtualFile != null) {
                val filename = virtualFile.nameWithoutExtension
                val psiDirectory = dataContext.getData(LangDataKeys.IDE_VIEW)?.orChooseDirectory
                if (psiDirectory != null) {
                    return psiDirectory.files.asSequence().filter {
                        // 找到同名的其他文件
                        it.virtualFile.nameWithoutExtension == filename && it.virtualFile.extension != virtualFile.extension
                    }.mapNotNull { sameNameFile ->
                        MyGotoRelatedItem.create(sameNameFile)
                    }.toMutableList()
                }
            }
        }
        return mutableListOf()
    }

    class MyGotoRelatedItem(
            element: PsiElement, private val name: String, private val filename: String, mnemonic: Int
    ) :
            GotoRelatedItem(element, GROUP_NAME, mnemonic) {

        companion object {
            fun create(psiFile: PsiFile): MyGotoRelatedItem? = when (psiFile.fileType) {
                JavaScriptFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Script", psiFile.name, 1)
                WXMLFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Template", psiFile.name, 2)
                WXSFileType.INSTANCE -> MyGotoRelatedItem(psiFile, "Styles", psiFile.name, 3)
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