package com.lamoonp.digital_wallet_demo.accountservice.service

import com.lamoonp.digital_wallet_demo.accountservice.exception.AccountNotFoundException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InsufficientBalanceException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InvalidAccountDataException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InvalidAccountStatusException
import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import com.lamoonp.digital_wallet_demo.accountservice.model.AccountStatus
import com.lamoonp.digital_wallet_demo.accountservice.repository.AccountRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class AccountServiceImpl(
    private val accountRepository: AccountRepository
): AccountService {

    private val supportedCurrencies = setOf(
        Currency.getInstance("USD"),
        Currency.getInstance("THB"),
        Currency.getInstance("SGD"),
    )

    private fun validateCurrency(currencyCode: String): Mono<Currency> {
        return try {
            val currency = Currency.getInstance(currencyCode.uppercase())
            if (currency in supportedCurrencies) {
                Mono.just(currency)
            } else {
                Mono.error(
                    InvalidAccountDataException(
                        "Currency $currencyCode is not supported. Supported currencies are: " +
                                supportedCurrencies.joinToString { it.currencyCode }
                    )
                )
            }
        } catch (e: IllegalArgumentException) {
            Mono.error(
                InvalidAccountDataException("Invalid currency code: $currencyCode")
            )
        }
    }

    override fun createAccount(userId: String, currency: String): Mono<Account> {
        return validateCreateAccountRequest(userId, currency)
            .flatMap { validCurrency ->
                accountRepository.save(
                Account(
                    accountId = UUID.randomUUID(),
                    userId = userId,
                    balance = BigDecimal.ZERO,
                    currency = validCurrency.currencyCode,
                    status = AccountStatus.ACTIVE,
                )
            )
        }
    }

    private fun validateCreateAccountRequest(
        userId: String,
        currency: String
    ): Mono<Currency> {
        return when {
            userId.isBlank() -> {
                Mono.error(
                    InvalidAccountDataException("User ID cannot be empty")
                )
            }

            else -> validateCurrency(currency)
        }
    }

    override fun getAccount(accountId: UUID): Mono<Account> {
        return accountRepository.findByAccountId(accountId)
            .switchIfEmpty(
                Mono.error(
                    AccountNotFoundException("Account not found with account ID: $accountId")
                )
            )
    }

    override fun getAccountByUserId(userId: String): Flux<Account> {
        return accountRepository.findByUserId(userId)
            .switchIfEmpty(
                Mono.error(
                    AccountNotFoundException("Account not found with user ID: $userId")
                )
            )
    }

    override fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account> {
        TODO("Not yet implemented")
    }

    override fun deactivateAccount(accountId: UUID): Mono<Account> {
        TODO("Not yet implemented")
    }
}