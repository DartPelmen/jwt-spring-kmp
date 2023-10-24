package org.example.repository

import org.example.entity.Roles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Roles, String>