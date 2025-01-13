package com.lamoonp.digital_wallet_demo.accountservice.service

import com.lamoonp.digital_wallet_demo.accountservice.exception.InvalidAccountDataException
import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import com.lamoonp.digital_wallet_demo.accountservice.repository.AccountRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*

class AccountServiceImpl(
    private val accountRepository: AccountRepository
): AccountService {

    private val

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

    private fun validateCreateAccountRequest(
        userId: String,
        currency: String
    ): Mono<Boolean> {
        return when {
            userId.isBlank() -> {
                Mono.error(
                    InvalidAccountDataException("User ID cannot be empty")
                )
            }
        }
    }
}