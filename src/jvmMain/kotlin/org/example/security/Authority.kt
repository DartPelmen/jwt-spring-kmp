package org.example.security

import org.example.entity.Roles
import org.springframework.security.core.GrantedAuthority

class Authority (private val role: Roles): GrantedAuthority {
    override fun getAuthority(): String {
        return role.roleName
    }
}