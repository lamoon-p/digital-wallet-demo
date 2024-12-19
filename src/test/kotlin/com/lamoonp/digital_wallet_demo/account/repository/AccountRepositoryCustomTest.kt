package com.lamoonp.digital_wallet_demo.account.repository

import com.lamoonp.digital_wallet_demo.account.util.AccountTestFactory
import com.yourname.digitalwallet.account.util.AbstractMongoDbTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.test.StepVerifier
import java.math.BigDecimal

@DataMongoTest
class AccountRepositoryCustomTest: AbstractMongoDbTest() {
    @Autowired
    private lateinit var mongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var accountRepository: AccountRepository

    private lateinit var accountRepositoryCustom: AccountRepositoryCustom

    @BeforeEach
    fun setup() {
        accountRepositoryCustom = AccountRepositoryCustomImpl(mongoTemplate)
    }

    @Test
    fun `should update account balance for given account`() {
        val initialBalance = BigDecimal("100.00")
        val newBalance = BigDecimal("200.00")
        val account = AccountTestFactory.createAccount(balance = initialBalance)

        StepVerifier.create(accountRepository.save(account))
            .expectNextMatches { savedAccount ->
                savedAccount.accountId == account.accountId &&
                savedAccount.balance == initialBalance
            }
            .verifyComplete()

        StepVerifier.create(accountRepositoryCustom.updateBalance(accountId = account.accountId, amount = newBalance))
            .expectNextMatches { updatedAccount ->
                updatedAccount.accountId == account.accountId &&
                    updatedAccount.balance == newBalance &&
                    updatedAccount.updatedAt.isAfter(account.updatedAt)
            }
            .verifyComplete()

        StepVerifier.create(accountRepository.findByAccountId(accountId = account.accountId))
            .expectNextMatches { foundAccount ->
                foundAccount.balance == newBalance
            }
            .verifyComplete()
    }
}