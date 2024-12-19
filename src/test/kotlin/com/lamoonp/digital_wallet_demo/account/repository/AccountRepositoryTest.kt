package com.lamoonp.digital_wallet_demo.account.repository

import com.lamoonp.digital_wallet_demo.account.model.Account
import com.lamoonp.digital_wallet_demo.account.model.AccountStatus
import com.lamoonp.digital_wallet_demo.account.util.AccountTestFactory
import com.yourname.digitalwallet.account.util.AbstractMongoDbTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.math.BigDecimal

@DataMongoTest
class AccountRepositoryTest : AbstractMongoDbTest() {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Test
    fun `should create a new account`() {
        val account = AccountTestFactory.createAccount()

        StepVerifier.create(accountRepository.save(account))
            .expectNextMatches { savedAccount ->
                savedAccount.userId == account.userId &&
                    savedAccount.accountId == account.accountId &&
                    savedAccount.balance == account.balance &&
                    savedAccount.currency == account.currency &&
                    savedAccount.status == account.status
            }
            .verifyComplete()
    }

    @Test
    fun `should find all accounts for given userId`() {

        val userId = "test-user"
        val account1 = AccountTestFactory.createAccount(userId = userId, balance = BigDecimal("100.00"))
        val account2 = AccountTestFactory.createAccount(userId = userId, balance = BigDecimal("200.00"))

        StepVerifier.create(accountRepository.saveAll(listOf(account1, account2)))
            .expectNextCount(2)
            .verifyComplete()

        StepVerifier.create(accountRepository.findByUserId(userId))
            .recordWith { ArrayList<Account>() }
            .expectNextCount(2)
            .consumeRecordedWith { accounts ->
                assertSoftly { softly ->
                    softly.assertThat(accounts).hasSize(2)
                    softly.assertThat(accounts)
                        .allMatch { it.userId == userId }
                    softly.assertThat(accounts.map { it.balance })
                        .containsExactlyInAnyOrder(
                            BigDecimal("100.00"),
                            BigDecimal("200.00")
                        )
                }
            }
            .verifyComplete()
    }

    @Test
    fun `should find account by accountId`() {
        val userId = "test-user"
        val account1 = AccountTestFactory.createAccount(userId = userId, balance = BigDecimal("100.00"))
        val account2 = AccountTestFactory.createAccount(userId = userId, balance = BigDecimal("200.00"))

        StepVerifier.create(accountRepository.saveAll(listOf(account1, account2)))
            .expectNextCount(2)
            .verifyComplete()

        StepVerifier.create(accountRepository.findByAccountId(accountId = account1.accountId))
            .expectNextMatches { foundAccount ->
                foundAccount.accountId == account1.accountId
            }
            .verifyComplete()
        StepVerifier.create(accountRepository.findByAccountId(accountId = account2.accountId))
            .expectNextMatches { foundAccount ->
                foundAccount.accountId == account2.accountId
            }
            .verifyComplete()
    }

    @Test
    fun `should find account by status`() {

        val userId = "test-user"
        val activeAccount = AccountTestFactory.createAccount(
            userId = userId,
            balance = BigDecimal("100.00"),
            status = AccountStatus.ACTIVE
        )
        val inactiveAccount = AccountTestFactory.createAccount(
            userId = userId,
            balance = BigDecimal("200.00"),
            status = AccountStatus.INACTIVE
        )

        StepVerifier.create(accountRepository.saveAll(listOf(activeAccount, inactiveAccount)))
            .expectNextCount(2)
            .verifyComplete()

        StepVerifier.create(accountRepository.findByStatus(AccountStatus.ACTIVE))
            .expectNextMatches { foundAccount ->
                foundAccount.userId == activeAccount.userId &&
                    foundAccount.accountId == activeAccount.accountId &&
                    foundAccount.balance == activeAccount.balance &&
                    foundAccount.currency == activeAccount.currency &&
                    foundAccount.status == activeAccount.status
            }
            .verifyComplete()

        StepVerifier.create(accountRepository.findByStatus(AccountStatus.INACTIVE))
            .expectNextMatches { foundAccount ->
                foundAccount.userId == inactiveAccount.userId &&
                    foundAccount.accountId == inactiveAccount.accountId &&
                    foundAccount.balance == inactiveAccount.balance &&
                    foundAccount.currency == inactiveAccount.currency &&
                    foundAccount.status == inactiveAccount.status
            }
            .verifyComplete()
    }
}