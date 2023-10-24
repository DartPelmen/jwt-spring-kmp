package org.example.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "app_roles", schema = "public")
open class Roles {
    @Id open var roleName: String = ""
    
    @ManyToMany
    @JoinTable(
        name = "accounts_roles",
        joinColumns = [JoinColumn(name = "role_name")],
        inverseJoinColumns = [JoinColumn(name = "account_id")]
    )
    @JsonBackReference
    open var accounts: MutableList<Account> = mutableListOf()
}