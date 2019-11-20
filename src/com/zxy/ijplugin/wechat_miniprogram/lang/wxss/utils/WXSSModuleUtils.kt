package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils

import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSPsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSImport
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.WXSSStringText

object WXSSModuleUtils {

    fun findImportedFilesWithSelf(wxssPsiFile: WXSSPsiFile): Collection<WXSSPsiFile> {
        return findImportedFiles(wxssPsiFile).toMutableList().apply {
            this.add(0,wxssPsiFile)
        }
    }

    private fun findImportedFiles(wxssPsiFile:WXSSPsiFile): Collection<WXSSPsiFile> {
        val result = hashSetOf<WXSSPsiFile>()
        addImportedFiles(wxssPsiFile, result)
        return result
    }

    private fun addImportedFiles(wxssPsiFile: WXSSPsiFile, result:MutableCollection<WXSSPsiFile>){
        val imports = PsiTreeUtil.findChildrenOfType(wxssPsiFile, WXSSImport::class.java)
        imports.mapNotNull {
            wxssImport ->
            val references = PsiTreeUtil.findChildOfType(wxssImport, WXSSStringText::class.java)?.references?: emptyArray()
            val resolveResult = references.find { it is FileReference }?.let { it as FileReference? }
                    ?.lastFileReference?.resolve()
            if (resolveResult is WXSSPsiFile){
                resolveResult
            }else{
                null
            }
        }.forEach {
            if (!result.contains(it)){
                // 如果此文件已经在结果集中
                // 则忽略

                // 添加此文件
                result.add(it)
                // 继续解析此文件的imports
                addImportedFiles(it,result)
            }
        }
    }

}