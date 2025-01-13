package com.lamoonp.digital_wallet_demo.account.service

import com.lamoonp.digital_wallet_demo.account.util.AccountTestFactory
import com.lamoonp.digital_wallet_demo.accountservice.exception.AccountNotFoundException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InsufficientBalanceException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InvalidAccountDataException
import com.lamoonp.digital_wallet_demo.accountservice.exception.InvalidAccountStatusException
import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import com.lamoonp.digital_wallet_demo.accountservice.model.AccountStatus
import com.lamoonp.digital_wallet_demo.accountservice.repository.AccountRepository
import com.lamoonp.digital_wallet_demo.accountservice.service.AccountService
import com.lamoonp.digital_wallet_demo.accountservice.service.AccountServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class AccountServiceTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var accountService: AccountService

    @BeforeEach
    fun setup() {
        accountRepository = mockk()
        accountService = AccountServiceImpl(accountRepository)
    }

    @Nested
    @DisplayName("Account Creation Tests")
    inner class AccountCreationTests {

        @Test
        fun `should successfully create account with valid data`() {

            val userId = UUID.randomUUID().toString()
            val currency = "THB"
            val accountSlot = slot<Account>()

            coEvery {
                accountRepository.save(capture(accountSlot))
            } answers {
                Mono.just(accountSlot.captured)
            }

            StepVerifier.create(accountService.createAccount(userId, currency))
                .expectNextMatches { account ->
                    account.userId == userId &&
                        account.balance == BigDecimal.ZERO &&
                        account.currency == "THB" &&
                        account.status == AccountStatus.ACTIVE
                }
                .verifyComplete()
        }

        @Test
        fun `should throw exception when creating account with invalid currency`() {

            val userId = UUID.randomUUID().toString()
            val invalidCurrency = "INVALID"

            StepVerifier.create(accountService.createAccount(userId, invalidCurrency))
                .expectError(InvalidAccountDataException::class.java)
                .verify()
        }

        @Test
        fun `should throw exception when creating account with empty userId`() {

            val userId = ""
            val currency = "THB"

            StepVerifier.create(accountService.createAccount(userId, currency))
                .expectError(InvalidAccountDataException::class.java)
                .verify()
        }
    }

    @Nested
    @DisplayName("Account Retrieval Tests")
    inner class AccountRetrievalTests {

        @Test
        fun `should successfully retrieve existing account`() {

            val accountId = UUID.randomUUID()
            val account = Account(
                accountId = accountId,
                userId = UUID.randomUUID().toString(),
                balance = BigDecimal.ZERO,
                currency = "THB",
                status = AccountStatus.ACTIVE
            )

            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.just(account)

            StepVerifier.create(accountService.getAccount(accountId))
                .expectNext(account)
                .verifyComplete()
        }

        @Test
        fun `should throw exception when account not found`() {

            val accountId = UUID.randomUUID()
            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.empty()

            StepVerifier.create(accountService.getAccount(accountId))
                .expectError(AccountNotFoundException::class.java)
                .verify()
        }
    }

    @Nested
    @DisplayName("Balance Update Tests")
    inner class BalanceUpdateTests {
        @Test
        fun `should successfully update balance for valid amount`() {

            val accountId = UUID.randomUUID()
            val initialBalance = BigDecimal(100.00)
            val updateAmount = BigDecimal(50.00)

            val initialAccount = Account(
                accountId = accountId,
                userId = UUID.randomUUID().toString(),
                balance = initialBalance,
                currency = "THB",
                status = AccountStatus.ACTIVE
            )

            val updatedAccount = initialAccount.copy(
                balance = updateAmount,
                updatedAt = LocalDateTime.now()
            )

            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.just(initialAccount)
            coEvery { accountRepository.save(any()) } returns Mono.just(updatedAccount)

            StepVerifier.create(accountService.updateBalance(accountId, updateAmount))
                .expectNext(updatedAccount)
                .verifyComplete()
        }

        @Test
        fun `should throw exception when update results in negative balance`() {

            val accountId = UUID.randomUUID()
            val initialBalance = BigDecimal(100.00)
            val updateAmount = BigDecimal(-150.00)

            val account = Account(
                accountId = accountId,
                userId = UUID.randomUUID().toString(),
                balance = initialBalance,
                currency = "THB",
                status = AccountStatus.ACTIVE,
            )

            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.just(account)

            StepVerifier.create(accountService.updateBalance(accountId, updateAmount))
                .expectError(InsufficientBalanceException::class.java)
                .verify()
        }
    }

    @Nested
    @DisplayName("Account Deactivation Tests")
    inner class AccountDeactivationTests {
        @Test
        fun `should successfully deactivate active account`() {

            val accountId = UUID.randomUUID()
            val activeAccount = Account(
                accountId = accountId,
                userId = UUID.randomUUID().toString(),
                balance = BigDecimal(100.00),
                currency = "THB",
                status = AccountStatus.ACTIVE,
            )

            val deactivatedAccount = activeAccount.copy(
                status = AccountStatus.INACTIVE,
                updatedAt = LocalDateTime.now()
            )

            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.just(activeAccount)
            coEvery { accountRepository.save(any()) } returns Mono.just(deactivatedAccount)

            StepVerifier.create(accountService.deactivateAccount(accountId))
                .expectNext(deactivatedAccount)
                .verifyComplete()
        }

        @Test
        fun `should throw exception when deactivating already inactive account`() {

            val accountId = UUID.randomUUID()
            val inactiveAccount = Account(
                accountId = accountId,
                userId = UUID.randomUUID().toString(),
                balance = BigDecimal(100.00),
                currency = "THB",
                status = AccountStatus.INACTIVE,
            )

            coEvery { accountRepository.findByAccountId(accountId) } returns Mono.just(inactiveAccount)

            StepVerifier.create(accountService.deactivateAccount(accountId))
                .expectError(InvalidAccountStatusException::class.java)
                .verify()
        }
    }

    @Nested
    @DisplayName("User Accounts Retrieval Tests")
    inner class UserAccountsRetrievalTests {
        @Test
        fun `should successfully retrieve all accounts for user`() {

            val userId = "test-user"
            val accounts = listOf(
                AccountTestFactory.createAccount(
                    userId = userId,
                    currency = "THB"
                ),
                AccountTestFactory.createAccount(
                    userId = userId,
                    currency = "SGD"
                )
            )

            coEvery { accountRepository.findByUserId(userId) } returns Flux.fromIterable(accounts)

            StepVerifier.create(accountService.getAccountByUserId(userId))
                .expectNextSequence(accounts)
                .verifyComplete()
        }

        @Test
        fun `should return empty flux when user has no accounts`() {

            val userId = "test-user"

            coEvery { accountRepository.findByUserId(userId) } returns Flux.empty()

            StepVerifier.create(accountService.getAccountByUserId(userId))
                .expectSubscription() // or expectNextCount(0)
                .verifyComplete()
        }
    }
}