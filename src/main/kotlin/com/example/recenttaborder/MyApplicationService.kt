package com.example.recenttaborder

class MyApplicationService {

    init {
        println("XXXXX APPLICATION ASERVICE ASTARTUP XXXXXX")

        System.getenv("CI")
            ?: TODO("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }
}
