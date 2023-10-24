package org.example

import org.example.config.WebSecurityConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringApp

fun main(args: Array<String>) {
    runApplication<SpringApp>(*args)
    println(WebSecurityConfig.passwordEncoder()
        .encode("admin"))
}

