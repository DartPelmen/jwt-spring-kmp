package org.example.repository

import jakarta.transaction.Transactional
import org.example.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, Long>{
    @Transactional
    @Query("SELECT t FROM Account t  LEFT JOIN FETCH t.roles where t.username = ?1")
    fun getAccountEntityByUsername(username: String): Account?
}