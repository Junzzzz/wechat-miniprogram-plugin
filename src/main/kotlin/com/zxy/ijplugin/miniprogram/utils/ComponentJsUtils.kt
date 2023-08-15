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

import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.impl.JSLiteralExpressionImpl

object ComponentJsUtils {

    /**
     * 获取js文件中的所有properties字段
     * 如果这个js文件具有ComponentAPI
     */
    fun findPropertiesItems(jsFile: JSFile): Array<out JSProperty>? {
        return findComponentApiOptionProperties(jsFile)?.find {
            it.name == "properties"
        }?.let {
            (it.value as? JSObjectLiteralExpression)?.properties
        }
    }

    /**
     * 找到Component API或Page API的方法调用
     */
    fun findComponentOrPageCallExpression(jsFile: JSFile): JSCallExpression? {
        return jsFile.children.filterIsInstance(JSExpressionStatement::class.java).mapNotNull {
            it.firstChild as? JSCallExpression
        }.find { jsCallExpression ->
            jsCallExpression.methodExpression.let {
                it is JSReferenceExpression && (it.text ==
                        "Component" || it.text == "Page")
            }
        }
    }

    fun findCallExpressionFirstObjectArg(callExpression: JSCallExpression): JSObjectLiteralExpression? {
        return callExpression.arguments.firstOrNull() as? JSObjectLiteralExpression
    }

    /**
     * 找到js文件中的第一个component API的选项列表
     */
    private fun findComponentApiOptionProperties(jsFile: JSFile): Array<JSProperty>? {
        return jsFile.findChildrenOfType<JSCallExpression>()
            .firstOrNull { jsCallExpression -> jsCallExpression.children.any { it is JSReferenceExpression && it.text == "Component" } }
            ?.let { jsCallExpression ->
                jsCallExpression.argumentList?.children?.asSequence()?.filterIsInstance<JSObjectLiteralExpression>()
                    ?.firstOrNull()
            }?.properties
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