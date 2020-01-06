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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceService
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSItemPresentation
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.*
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSElementFactory
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSModuleUtils
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils.WXSSModuleUtils.findAppFileWithImportedFiles
import com.zxy.ijplugin.wechat_miniprogram.reference.WXSSClassSelectorSelfReference
import com.zxy.ijplugin.wechat_miniprogram.reference.WXSSIdSelectorSelfReference
import javax.swing.Icon


object WXSSPsiImplUtils {

    /*WXSSIdSelector*/

    private fun getIdNodeByWXSSIdSelector(
            element: WXSSIdSelector
    ) = element.node.findChildByType(WXSSTypes.IDENTIFIER)

    @JvmStatic
    fun getPresentation(element: WXSSIdSelector): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return element.text
            }

            override fun getLocationString(): String? {
                return element.containingFile.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.Xml.Html_id
            }
        }
    }

    @JvmStatic
    fun getName(element: WXSSIdSelector): String? {
        return getId(element)
    }

    @JvmStatic
    fun setName(element: WXSSIdSelector, newName: String): PsiElement {
        val idNode = getIdNodeByWXSSIdSelector(element)
        idNode?.let {
            element.node.replaceChild(idNode, WXSSElementFactory.createId(element.project, newName))
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSIdSelector): PsiElement? {
        return getIdNodeByWXSSIdSelector(element)?.psi
    }

    @JvmStatic
    fun getId(wxssIdSelector: WXSSIdSelector): String? {
        return getIdNodeByWXSSIdSelector(wxssIdSelector)?.text
    }

    @JvmStatic
    fun getIcon(wxssIdSelector: WXSSIdSelector): Icon {
        return AllIcons.Xml.Html_id
    }

    @JvmStatic
    fun getReference(wxssIdSelector: WXSSIdSelector): PsiReference? {
        val textRange = wxssIdSelector.nameIdentifier?.textRangeInParent ?: return null
        return WXSSIdSelectorSelfReference(wxssIdSelector, textRange)
    }

    @JvmStatic
    fun getTextOffset(wxssIdSelector: WXSSIdSelector): Int {
        return wxssIdSelector.textRange.startOffset + 1
    }

    @JvmStatic
    fun getReferences(wxssIdSelector: WXSSIdSelector): Array<PsiReference> {
        return wxssIdSelector.reference?.let {
            arrayOf(it)
        } ?: emptyArray()
    }

    @JvmStatic
    fun getUseScope(wxssIdSelector: WXSSIdSelector): SearchScope {
        val psiFile = wxssIdSelector.containingFile
        if (psiFile is WXSSPsiFile) {
            return GlobalSearchScope.filesScope(
                    wxssIdSelector.project,
                    WXSSModuleUtils.findImportedFilesWithSelf(psiFile).map { it.virtualFile }.toMutableList().apply {
                        // WXML文件
                        findRelateFile(psiFile.virtualFile, RelateFileType.WXML)?.let {
                            this.add(it)
                        }
                    })
        }
        return GlobalSearchScope.EMPTY_SCOPE
    }

    /*WXSSClassSelector*/

    private fun getClassNodeByWXSSClassSelector(
            wxssClassSelector: WXSSClassSelector
    ) = wxssClassSelector.node.findChildByType(WXSSTypes.IDENTIFIER)

    @JvmStatic
    fun getClassName(wxssClassSelector: WXSSClassSelector): String? {
        return getClassNodeByWXSSClassSelector(wxssClassSelector)?.text
    }

    @JvmStatic
    fun getPresentation(element: WXSSClassSelector): ItemPresentation {
        return object : WXSSItemPresentation(element) {

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.Xml.Css_class
            }
        }
    }

    @JvmStatic
    fun getName(element: WXSSClassSelector): String? {
        return getClassName(element)
    }

    @JvmStatic
    fun setName(element: WXSSClassSelector, newName: String): PsiElement {
        getClassNodeByWXSSClassSelector(element)?.let {
            element.node.replaceChild(it, WXSSElementFactory.createClass(element.project, newName))
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSClassSelector): PsiElement? {
        return getClassNodeByWXSSClassSelector(element)?.psi
    }

    @JvmStatic
    fun getIcon(wxssClassSelector: WXSSClassSelector): Icon {
        return AllIcons.Xml.Css_class
    }

    @JvmStatic
    fun getReference(wxssClassSelector: WXSSClassSelector): PsiReference? {
        val textRange = wxssClassSelector.nameIdentifier?.textRangeInParent ?: return null
        return WXSSClassSelectorSelfReference(wxssClassSelector, textRange)
    }

    @JvmStatic
    fun getTextOffset(wxssClassSelector: WXSSClassSelector): Int {
        return wxssClassSelector.textRange.startOffset + 1
    }

    @JvmStatic
    fun getReferences(wxssClassSelector: WXSSClassSelector): Array<PsiReference> {
        return wxssClassSelector.reference?.let {
            arrayOf(it)
        } ?: emptyArray()
    }

    @JvmStatic
    fun getUseScope(wxssClassSelector: WXSSClassSelector): SearchScope {
        val psiFile = wxssClassSelector.containingFile
        if (psiFile is WXSSPsiFile) {
            val result = hashSetOf<WXSSPsiFile>()
            val wxssFilesWithImported = WXSSModuleUtils.findImportedFilesWithSelf(psiFile)
            result.addAll(wxssFilesWithImported)

            // 解析app.wxss
            result.addAll(findAppFileWithImportedFiles(wxssClassSelector.project))

            return GlobalSearchScope.filesScope(
                    wxssClassSelector.project, result.map { it.virtualFile }.toMutableList().apply {
                // 自己对应的WXML文件
                findRelateFile(psiFile.virtualFile, RelateFileType.WXML)?.let {
                    this.add(it)
                }

                // 引入了自己的wxss文件
                this.addAll(
                        ReferencesSearch.search(
                                psiFile
                        ).filterIsInstance<FileReference>().mapNotNull { it.element.containingFile.virtualFile })
            })
        }
        return GlobalSearchScope.EMPTY_SCOPE
    }

    /*string text*/
    @JvmStatic
    fun createLiteralTextEscaper(element: WXSSStringText): LiteralTextEscaper<WXSSStringText> {
        return LiteralTextEscaper.createSimple(element)
    }

    @JvmStatic
    fun isValidHost(element: WXSSStringText): Boolean {
        return true
    }

    @JvmStatic
    fun updateText(element: WXSSStringText, newText: String): WXSSStringText {
        element.replace(WXSSElementFactory.createStringText(element.project, newText))
        return element
    }

    @JvmStatic
    fun getReferences(element: WXSSStringText): Array<out PsiReference> {
        return PsiReferenceService.getService().getContributedReferences(element)
    }

    /*styleStatement*/
    @JvmStatic
    fun getAttributeName(element: WXSSStyleStatement): String? {
        return element.firstChild?.text
    }

    /*keyframesDefinition*/
    @JvmStatic
    fun getName(element: WXSSKeyframesDefinition): String? {
        return element.keyframesName?.name
    }

    /*wxss value*/
    @JvmStatic
    fun getReferences(element: WXSSValue): Array<out PsiReference> {
        return PsiReferenceService.getService().getContributedReferences(element)
    }

    /*wxss keyframesName*/
    @JvmStatic
    fun getName(element: WXSSKeyframesName): String? {
        return element.text
    }

    @JvmStatic
    fun setName(element: WXSSKeyframesName, name: String): PsiElement {
        element.firstChild.replace(WXSSElementFactory.createIdentity(element.project, name))
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: WXSSKeyframesName): PsiElement? {
        return element.firstChild
    }

    /**
     * 关键帧的使用者在引用了自己或者当前文件以及app.wxss文件中
     */
    @JvmStatic
    fun getUseScope(element: WXSSKeyframesName): SearchScope {
        val psiFile = element.containingFile
        if (psiFile is WXSSPsiFile) {
            val result = hashSetOf(psiFile)
            // 解析app.wxss
            result.addAll(findAppFileWithImportedFiles(element.project))

            return GlobalSearchScope.filesScope(
                    element.project, result.map { it.virtualFile }.toMutableList().apply {

                // 引入了自己的wxss文件
                this.addAll(
                        ReferencesSearch.search(
                                psiFile
                        ).filterIsInstance<FileReference>().mapNotNull { it.element.containingFile.virtualFile })
            })
        }
        return GlobalSearchScope.EMPTY_SCOPE
    }

}