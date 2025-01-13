package com.lamoonp.digital_wallet_demo.accountservice.service

import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

interface AccountService {
    fun createAccount(userId: String, currency: String): Mono<Account>
    fun getAccount(accountId: UUID): Mono<Account>
    fun getAccountByUserId(userId: String): Flux<Account>
    fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account>
    fun deactivateAccount(accountId: UUID): Mono<Account>
}