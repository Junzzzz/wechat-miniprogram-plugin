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

package com.zxy.ijplugin.miniprogram.utils

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.miniprogram.lang.wxss.WXSSLanguage
import com.zxy.ijplugin.miniprogram.qq.QMLFileType
import com.zxy.ijplugin.miniprogram.qq.QSSFileType

object ComponentFilesCreator {

    fun createWechatComponentFiles(
        fileName: String,
        psiDirectory: PsiDirectory,
        jsFileTemplateName: String,
        jsonFileTemplateName: String,
        wxmlTemplate: String = ""
    ) {
        val project = psiDirectory.project
        val fileTemplateManager = FileTemplateManager.getInstance(project)
        val fileTemplateDefaultProps = fileTemplateManager.defaultProperties
        val jsComponentTemplate = fileTemplateManager.getInternalTemplate(
            jsFileTemplateName
        )
        FileTemplateUtil.createFromTemplate(
            jsComponentTemplate, fileName,
            fileTemplateDefaultProps, psiDirectory
        )
        val jsonComponentTemplate = fileTemplateManager.getInternalTemplate(
            jsonFileTemplateName
        )
        FileTemplateUtil.createFromTemplate(
            jsonComponentTemplate, fileName, fileTemplateDefaultProps,
            psiDirectory
        )

        // 创建空的标记文件和样式文件
        val wxmlFile = PsiFileFactory.getInstance(project).createFileFromText(
            fileName + "." + if (project.isQQContext()) QMLFileType.INSTANCE.defaultExtension else WXMLFileType.INSTANCE.defaultExtension,
            WXMLLanguage.INSTANCE, wxmlTemplate
        )
        val wxssFile = PsiFileFactory.getInstance(project).createFileFromText(
            fileName + "." + if (project.isQQContext()) QSSFileType.INSTANCE.defaultExtension else WXSSFileType.INSTANCE.defaultExtension,
            WXSSLanguage.INSTANCE, ""
        )
        psiDirectory.add(wxmlFile)
        psiDirectory.add(wxssFile)
    }

    fun createComponentPathFromFile(targetFile: PsiFile): String? {
        return targetFile.virtualFile.getPathRelativeToRootRemoveExt(targetFile.project)
    }

}