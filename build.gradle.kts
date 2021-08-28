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
buildscript {

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    }
}
fun properties(key: String) = project.findProperty(key).toString()
plugins {
    id("org.jetbrains.intellij") version "1.1.4"
}

// JDK compatibility

//tasks.withType(JavaCompile) { options.encoding = "UTF-8" }

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.5"
            apiVersion = "1.5"
            freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=enable"
        }
    }
    publishPlugin {
        token.set(properties("intellijPublishToken"))
    }
    patchPluginXml {
        sinceBuild.set("212")
        untilBuild.set("212.*")
        version.set(version)
        val changeNotes = """
<ul lang="cn">
    <li> 兼容 212.* </li>
</ul>
<br/>
<ul lang="en">
    <li> Compatible with 212.* </li>
</ul>
"""
        val pluginDescription = """
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
        this.changeNotes.set(changeNotes)
        this.pluginDescription.set(pluginDescription)
    }

}

sourceSets {
    main {
        java {
            srcDirs("src")
            srcDirs("gen")
        }
        resources {
            srcDirs("resources")
        }
    }
}

intellij {
//    localPath "C:\\Users\\Administrator\\Downloads\\ideaIU-LATEST-EAP-SNAPSHOT"
    type.set("IU")
    version.set("IU-LATEST-EAP-SNAPSHOT")
    pluginName.set("wechat mini program")
    downloadSources.set(true)
    updateSinceUntilBuild.set(false)
    plugins.set(listOf("JavaScriptLanguage", "CSS", "less", "sass", "stylus"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
    implementation("org.jetbrains:annotations-java5:17.0.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.5.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

version = "3.5.7"