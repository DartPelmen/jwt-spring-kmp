package org.example.security

import org.example.entity.Account
import org.example.entity.Roles
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AccountDetails(private val account: Account): UserDetails {
 
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(Authority(Roles().also { it.roleName = "ADMIN" }))


    override fun getPassword(): String {
        return account.password
    }

    override fun getUsername(): String {
        return account.username
    }

    override fun isAccountNonExpired(): Boolean {
        return !account.isExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return !account.isLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return account.isActive
    }
}