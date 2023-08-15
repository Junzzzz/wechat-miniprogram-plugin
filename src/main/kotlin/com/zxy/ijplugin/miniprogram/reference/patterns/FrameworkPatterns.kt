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

package com.zxy.ijplugin.miniprogram.reference.patterns

import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.miniprogram.context.isQQContext
import com.zxy.ijplugin.miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.miniprogram.qq.QMLFileType

object FrameworkPatterns {
    val qmlFileTypeAndQQContextPattern = object : PatternCondition<WXMLPsiFile>("withQmlFileTypeAndQQContext") {
        override fun accepts(wxmlPsiFile: WXMLPsiFile, context: ProcessingContext?): Boolean {
            return wxmlPsiFile.project.isQQContext() && wxmlPsiFile.fileType == QMLFileType.INSTANCE
        }
    }

    val qmlFileAndQQContextPattern = PlatformPatterns.psiFile(WXMLPsiFile::class.java)
        .with(qmlFileTypeAndQQContextPattern)
}