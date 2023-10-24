package org.example.service

import org.example.entity.Account
import org.example.repository.AccountRepository
import org.example.security.AccountDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class AccountService(val accountRepository: AccountRepository): UserDetailsService {
   

    override fun loadUserByUsername(username: String?): UserDetails {
        return username?.let {
            val account = accountRepository.getAccountEntityByUsername(it)
            return account?.let { a -> AccountDetails(a) } ?: AccountDetails(Account())
        } ?: AccountDetails(Account())
    }
}