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

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.zxy.ijplugin"
version = "3.5.16"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "17" }

    patchPluginXml {
        sinceBuild = "231"
        untilBuild = "232.*"

        changeNotes = """
<ul lang="cn">
    <li> 兼容2023.1 </li>
</ul>
<br/>
<ul lang="en">
    <li> Compatible with 2023.1 </li>
</ul>
"""
        pluginDescription = """
Support <a href="https://developers.weixin.qq.com/miniprogram/introduction/"> WeChat Mini Program </a> project
<h3>使用入门</h3>
<ul>
    <li>打开微信小程序或QQ小程序项目</li>
    <li>确保project.config.json配置文件存在</li>
    <li>现在您可以使用所有此插件提供的功能</li>
</ul>
<h3>主要功能</h3>
<ul>
    <li>wxml / wxss / wxs文件支持</li>
    <li>创建微信小程序组件和页面</li>
    <li>相关文件导航</li>
    <li>微信小程序自定义组件支持</li>
    <li>微信小程序配置文件支持</li>
    <li>代码检查和自动修复</li>
    <li>支持QQ小程序项目 (v3.1.0)</li>
    <li>支持npm中的组件</li>
</ul>
<h3> Get started </h3>
<ul>
    <li> Open WeChat Mini Program Project</li>
    <li> Ensure that the project.config.json configuration file exists</li>
    <li> Now you can use all the features provided by this plugin</li>
</ul>
<h3> Main functions </h3>
<ul>
    <li> wxml / wxss / wxs file support</li>
    <li> Create WeChat applet components and pages</li>
    <li> Relevant file navigation</li>
    <li> WeChat applet custom component support</li>
    <li> WeChat applet configuration file support</li>
    <li> Code inspection and automatic repairs</li>
</ul>
For detailed usage documents and function descriptions, please visit
<a href="https://gitee.com/zxy_c/wechat-miniprogram-plugin/wikis"> Gitee Wiki </a>
"""
    }

    publishPlugin {
        // 发布插件用的 TOKEN
        // token.set(System.getenv("TOKEN"))
    }
}

intellij {
    version = "2023.1"
    pluginName = "wechat-miniprogram-plugin"
    type = "IU"
    updateSinceUntilBuild = false
    downloadSources = true
    plugins = listOf("JavaScript", "com.intellij.css", "less", "sass", "org.jetbrains.plugins.stylus:231.8109.91")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.jetbrains:annotations-java5:24.0.1")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
}
