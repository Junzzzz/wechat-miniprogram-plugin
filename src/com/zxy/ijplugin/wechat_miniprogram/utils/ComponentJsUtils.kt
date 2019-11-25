package com.zxy.ijplugin.wechat_miniprogram.utils

import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.impl.JSLiteralExpressionImpl

object ComponentJsUtils {

    /**
     * 获取js文件中的所有properties字段
     * 如果这个js文件具有ComponentAPI
     */
    fun findPropertiesItems(jsFile: JSFile): Array<out JSProperty>? {
        return jsFile.children.filterIsInstance(JSExpressionStatement::class.java).mapNotNull {
            it.firstChild as? JSCallExpression
        }.find { jsCallExpression ->
            jsCallExpression.children.any { it is JSReferenceExpression && it.text == "Component" }
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
        val value = jsProperty.value ?: return null
        return if (value is JSObjectLiteralExpression) {
            value.properties.find {
                it.name == "type"
            }?.text
        } else {
            value.text
        }
    }

    /**
     * 获取Component中的externalClasses配置
     * https://developers.weixin.qq.com/miniprogram/dev/framework/custom-component/wxml-wxss.html#%E5%A4%96%E9%83%A8%E6%A0%B7%E5%BC%8F%E7%B1%BB
     */
    fun findComponentExternalClasses(jsFile: JSFile): List<String>? {
        val options = findComponentOptions(jsFile)
        return (options?.findProperty("externalClasses")?.value as? JSArrayLiteralExpression)?.expressions?.mapNotNull {
            (it as? JSLiteralExpressionImpl)?.stringValue
        }
    }

    /**
     * 找到Component调用中的参数
     * Component(options)
     */
    private fun findComponentOptions(jsFile: JSFile): JSObjectLiteralExpression? {
        return jsFile.children.filterIsInstance(JSExpressionStatement::class.java).mapNotNull {
            it.firstChild as? JSCallExpression
        }.find { jsCallExpression ->
            jsCallExpression.children.any { it is JSReferenceExpression && it.text == "Component" }
        }?.let { jsCallExpression ->
            (jsCallExpression.argumentList?.children?.find { it is JSObjectLiteralExpression } as? JSObjectLiteralExpression)
        }
    }

}