package com.zxy.ijplugin.wechat_miniprogram.lang.scrpits

import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL


fun main() {
    val zipUrl = "https://github.com/mdn/browser-compat-data/archive/master.zip"
    val zipFileName = "browser-compat-data.zip"
    val localZipFile = File("gen/$zipFileName")
    val mdnProjectExtractDir = File("gen")
    println("下载zip中。。。")
    FileUtils.copyURLToFile(URL(zipUrl), localZipFile,4000,600000)
    println("解压中。。。")
    println("解压完成")
    localZipFile.deleteOnExit()
    FileExtractUtils.extractZip(localZipFile,mdnProjectExtractDir)
    val mdnProjectExtract = File("gen/browser-compat-data-master")
    val cssDir = mdnProjectExtract.list { _, name -> name =="css" }!!.first()
}
