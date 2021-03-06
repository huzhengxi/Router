package com.jason.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("I'm from RouterPlugin, apply from ${project.name}")

        project.extensions.create("router", RouterExtension)

        project.afterEvaluate {
            RouterExtension extension = project["router"]
            println("用户设置的wiki路径为：${extension.wikiDir}")
        }
    }
}