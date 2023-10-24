package org.example.controller

import org.example.config.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ExampleController(private val jwtService: JwtService,
                        private val authenticationManager: AuthenticationManager){


    @PostMapping("/generateToken")
    fun authenticateAndGetToken(): String {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                "user",
                "admin"
            )
        )
        return if (authentication.isAuthenticated) {
            jwtService.generateToken("user")
        } else {
            throw UsernameNotFoundException("invalid user request !")
        }
    }
}