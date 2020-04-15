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

package com.zxy.ijplugin.wechat_miniprogram.context

import com.intellij.json.JsonFileType
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.javascript.JavaScriptFileType
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import kotlin.reflect.KClass

fun isWechatMiniProgramContext(psiElement: PsiElement, strict: Boolean = true): Boolean {
    return isWechatMiniProgramContext(psiElement.project)
}

/**
 * @param strict 是否去检查project.config.json的值
 */
fun isWechatMiniProgramContext(project: Project, strict: Boolean = true): Boolean {
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            val projectConfigJsonFile = baseDir.children.find { it.name == "project.config.json" } ?: return false
            return if (strict) {
                runReadAction {
                    // 读取文件内容创建文件
                    val jsonFile = PsiManager.getInstance(project).findFile(projectConfigJsonFile)
                    ((jsonFile?.children?.getOrNull(0) as? JsonObject)?.propertyList?.find {
                        it.name == "compileType"
                    }?.value as? JsonStringLiteral)?.value == "miniprogram"
                }
            } else {
                true
            }
        }
    }
    return false
}

enum class RelateFileType(val fileType: LanguageFileType) {
    MARKUP(WXMLFileType.INSTANCE),
    JSON(JsonFileType.INSTANCE),
    SCRIPT(JavaScriptFileType.INSTANCE),
    STYLE(WXSSFileType.INSTANCE);

    fun findRelateSelf(virtualFile: VirtualFile) {

    }
}
@Deprecated("QQ")
val componentFileClasses = arrayOf(
        JSFile::class.java, WXMLPsiFile::class.java, WXSSPsiFile::class.java, JsonFile::class.java
)

fun LanguageFileType.getRelateFileType(): RelateFileType? {
    return when (this) {
        WXMLFileType.INSTANCE -> RelateFileType.MARKUP
        JsonFileType.INSTANCE -> RelateFileType.JSON
        JavaScriptFileType.INSTANCE -> RelateFileType.SCRIPT
        WXSSFileType.INSTANCE -> RelateFileType.STYLE
        else -> null
    }
}

fun <T : PsiFile> Class<T>.getRelateFileType(): RelateFileType? {
    return when {
        WXSSPsiFile::class.java.isAssignableFrom(this) -> RelateFileType.STYLE
        WXMLPsiFile::class.java.isAssignableFrom(this) -> RelateFileType.MARKUP
        JsonFile::class.java.isAssignableFrom(this) -> RelateFileType.JSON
        JSFile::class.java.isAssignableFrom(this) -> RelateFileType.SCRIPT
        else -> null
    }
}

/**
 * 找到指定的文件所对应的指定扩展的文件
 * 例如：
 * 已知一个wxss
 * 找到其对应的文件
 * @param relateFileType 对应的文件类型
 */
@Deprecated(message = "使用RelateFileHolder替换", replaceWith = ReplaceWith(expression = "RelateFileHolder.findFile"))
fun findRelateFile(originFile: VirtualFile, relateFileType: RelateFileType): VirtualFile? {
    return originFile.parent?.children?.find { it.nameWithoutExtension == originFile.nameWithoutExtension && it.extension == relateFileType.fileType.defaultExtension }
}

@Deprecated(
        message = "使用RelateFileHolder替换", replaceWith = ReplaceWith(expression = "RelateFileHolder.findFile(psiFile)")
)
inline fun <reified T : PsiFile> findRelatePsiFile(psiFile: PsiFile): T? {
    val originFile = psiFile.originalFile.virtualFile
    val project = psiFile.project
    return findRelatePsiFile(originFile, project)
}

@Deprecated(message = "使用RelateFileHolder替换", replaceWith = ReplaceWith(expression = "RelateFileHolder.findFile"))
inline fun <reified T : PsiFile> findRelatePsiFile(originFile: VirtualFile, project: Project): T? {
    val relateFileType = getRelateFileTypeFromClass(T::class) ?: return null
    val virtualFile = findRelateFile(originFile, relateFileType) ?: return null
    return PsiManager.getInstance(project).findFile(virtualFile) as? T
}

@Deprecated(message = "使用RelateFileHolder替换", replaceWith = ReplaceWith(expression = "RelateFileHolder.findFile"))
fun <T : PsiFile> getRelateFileTypeFromClass(clazz: KClass<T>): RelateFileType? {
    return when (clazz) {
        WXSSPsiFile::class -> RelateFileType.STYLE
        WXMLPsiFile::class -> RelateFileType.MARKUP
        JsonFile::class -> RelateFileType.JSON
        JSFile::class -> RelateFileType.SCRIPT
        else -> null
    }
}

@Deprecated(message = "使用RelateFileHolder替换", replaceWith = ReplaceWith(expression = "RelateFileHolder.findFile"))
fun findRelatePsiFile(psiFile: PsiFile, relateFileType: RelateFileType): PsiFile? {
    val virtualFile = findRelateFile(psiFile.originalFile.virtualFile, relateFileType) ?: return null
    return PsiManager.getInstance(psiFile.project).findFile(virtualFile)
}

@Deprecated(
        message = "使用RelateFileHolder替换",
        replaceWith = ReplaceWith(expression = "RelateFileHolder.findAppFile(project)")
)
fun findAppFile(project: Project, relateFileType: RelateFileType): VirtualFile? {
    if (relateFileType == RelateFileType.MARKUP) return null
    val basePath = project.basePath
    if (basePath != null) {
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath)
        if (baseDir != null) {
            return baseDir.findChild("app." + relateFileType.fileType.defaultExtension)
        }
    }
    return null
}

@Deprecated(
        message = "使用RelateFileHolder替换",
        replaceWith = ReplaceWith(expression = "RelateFileHolder.findAppFile(project)")
)
inline fun <reified T : PsiFile> findAppFile(project: Project): T? {
    val relateFileType = getRelateFileTypeFromClass(T::class) ?: return null
    val virtualFile = findAppFile(project, relateFileType)
    return virtualFile?.let { PsiManager.getInstance(project).findFile(it) } as? T
}