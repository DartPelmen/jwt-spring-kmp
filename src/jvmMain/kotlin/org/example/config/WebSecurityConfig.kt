package org.example.config

import org.example.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig(private val userDetailsService: AccountService, private val authFilter: JwtAuthFilter) {

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService?>(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }
    //https://docs.spring.io/spring-security/reference/migration-7/configuration.html#_use_the_lambda_dsl
    @Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        val build = http
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            .cors{
                it.disable()
            }
            .csrf{
                it.disable()
            }
            .httpBasic(withDefaults())
            .formLogin(withDefaults())
            .rememberMe{
                it.tokenValiditySeconds(86400)
            }
            .authorizeHttpRequests{
                it.requestMatchers("/private*").hasRole("ADMIN")
                it.anyRequest().permitAll()
            }
                .build()
        return build
    }

 
    
    companion object{
        @Bean
        fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

        @Bean
        @Throws(java.lang.Exception::class)
        fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
            return config.getAuthenticationManager()
        }

    }
    
}