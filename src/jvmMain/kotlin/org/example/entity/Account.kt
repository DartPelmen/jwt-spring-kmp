package org.example.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.example.entity.Roles

@Entity
@Table(name = "accounts", schema = "public")
open class Account{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    open var id: Long? = 0 
    @Basic
    @Column
    open var username: String = ""
    @Basic
    @Column
    open var password: String = ""
    @Basic
    @Column
    open var confirmPassword: String =""
    @Basic
    @Column
    open var isLocked: Boolean = false
    @Basic
    @Column
    open var isActive: Boolean = true
    @Basic
    @Column
    open var isExpired: Boolean = false
 
    @ManyToMany
    @JoinTable(
        name = "accounts_roles",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_name")]
    )
    @JsonManagedReference
    open var roles: MutableList<Roles> = mutableListOf<Roles>()
}