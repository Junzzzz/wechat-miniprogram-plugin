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

package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.json.psi.*
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findAppFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile

class WechatMiniProgramJSONReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        // 小程序的json配置文件中的usingComponents配置
        // 解析被引入的组件的路径
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, processingContext: ProcessingContext
                    ): Array<out PsiReference> {
                        psiElement as JsonStringLiteral
                        // 确定此元素是正确的usingComponents配置项的值
                        val parent = psiElement.parent
                        if (parent is JsonProperty && parent.value == psiElement) {
                            val usingComponentsProperty = parent.parent?.parent
                            if (usingComponentsProperty is JsonProperty && usingComponentsProperty.name == "usingComponents") {
                                // 找到usingComponents配置
                                val wrapObject = usingComponentsProperty.parent
                                if (wrapObject is JsonObject && wrapObject.parent is JsonFile) {
                                    val fileReferences = FileReferenceSet(psiElement).allReferences
                                    return handlerFileReferences(psiElement, fileReferences)
                                }
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 小程序的app.json配置文件中的pages配置项
        // 解析被注册的page的路径
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(JsonStringLiteral::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, processingContext: ProcessingContext
                    ): Array<out PsiReference> {
                        psiElement as JsonStringLiteral
                        val parentArray = psiElement.parent
                        if (findAppFile(
                                        psiElement.project, RelateFileType.JSON
                                ) == psiElement.containingFile.originalFile.virtualFile) {
                            // 确定是app.json
                            if (parentArray is JsonArray) {
                                val parentProperty = parentArray.parent
                                if (parentProperty is JsonProperty && parentProperty.name == "pages") {
                                    val rootObject = parentProperty.parent
                                    if (rootObject is JsonObject && rootObject.parent is JsonFile) {
                                        // 确定是app.json下的pages配置项
                                        val fileReferences = FileReferenceSet(psiElement).allReferences
                                        return handlerFileReferences(psiElement,fileReferences)
                                    }
                                }
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }

                })
    }

}

internal fun handlerFileReferences(psiElement: JsonStringLiteral, fileReferences: Array<out FileReference>): Array<out PsiReference> {
    if (fileReferences.isNotEmpty()) {
        val last = fileReferences.last()
        if (last.resolve() == null && fileReferences.size >= 2) {
            // 没有文件扩展名 最后一个引用无法解析出文件
            val filename = last.text
            // 获取倒数第二项
            // 解析出文件夹
            val psiDirectory = fileReferences[fileReferences.size - 2].resolve()
            if (psiDirectory is PsiDirectory) {
                val references = fileReferences.map { it as PsiReference }
                        .toTypedArray()
                // 最后一个引用可能解析出多个文件 js|wxss|wxml|json
                val lastFileReference = object : PsiPolyVariantReferenceBase<JsonStringLiteral>(
                                psiElement, last.rangeInElement
                        ) {
                    override fun multiResolve(p0: Boolean): Array<ResolveResult> =
                            psiDirectory.files.filter {
                                it.virtualFile.nameWithoutExtension == filename
                                        && (it is JSFile
                                        || it is WXMLPsiFile
                                        || it is WXSSPsiFile
                                        || it is JsonFile)
                            }.map {
                                PsiElementResolveResult(it)
                            }.toTypedArray()

                }
                references[fileReferences.size - 1] = lastFileReference
                return references
            }
        } else {
            return fileReferences
        }
    }
    return PsiReference.EMPTY_ARRAY
}