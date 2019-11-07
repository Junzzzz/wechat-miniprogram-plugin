package com.zxy.ijplugin.wechat_miniprogram.lang.utils

import com.intellij.lang.ASTNode

fun <R : Any> ASTNode.mapChildren(transform: (ASTNode) -> R?): List<R?> {
    val list = mutableListOf<R?>()
    var child = this.firstChildNode
    while (child != null) {
        list.add(transform(child))
        child = child.treeNext
    }
    return list
}

fun <R:Any> ASTNode.mapChildrenNotNull(transform: (ASTNode) -> R?): List<R> {
    return this.mapChildren(transform).filterNotNull()
}