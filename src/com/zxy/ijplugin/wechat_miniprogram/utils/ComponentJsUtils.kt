package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.lang.javascript.psi.*

object ComponentJsUtils {

    /**
     * 获取js文件中的所有properties字段
     * 如果这个js文件具有ComponentAPI
     */
    fun findPropertiesItems(jsFile: JSFile): Array<out JSProperty>? {
        return jsFile.children.filterIsInstance(JSExpressionStatement::class.java).mapNotNull {
            it.firstChild as? JSCallExpression
        }.find { jsCallExpression ->
            jsCallExpression.children.any { it is JSReferenceExpression && it.text== "Component" }
        }?.let { jsCallExpression ->
            (jsCallExpression.argumentList?.children?.find { it is JSObjectLiteralExpression } as? JSObjectLiteralExpression)?.properties
        }?.find {
            it.name == "properties"
        }?.let {
            (it.value as? JSObjectLiteralExpression)?.properties
        }
    }

    /**
     * 解析Component API 中的properties中的一项的type的值
     * @param jsProperty Component API 的properties中的一项
     */
    fun findTypeByPropertyValue(jsProperty: JSProperty): String? {
        val value = jsProperty.value?:return null
        return if (value is JSObjectLiteralExpression){
            value.properties.find {
                it.name == "type"
            }?.text
        }else{
            value.text
        }
    }

}