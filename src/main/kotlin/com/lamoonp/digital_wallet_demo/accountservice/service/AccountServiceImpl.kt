package com.lamoonp.digital_wallet_demo.accountservice.service

import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import com.lamoonp.digital_wallet_demo.accountservice.repository.AccountRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*

class AccountServiceImpl(
    accountRepository: AccountRepository
): AccountService {
    override fun createAccount(userId: String, currency: String): Mono<Account> {
        TODO("Not yet implemented")
    }

    override fun getAccount(accountId: UUID): Mono<Account> {
        TODO("Not yet implemented")
    }

    override fun getAccountByUserId(userId: String): Flux<Account> {
        TODO("Not yet implemented")
    }

    override fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account> {
        TODO("Not yet implemented")
    }

    override fun deactivateAccount(accountId: UUID): Mono<Account> {
        TODO("Not yet implemented")
    }

}