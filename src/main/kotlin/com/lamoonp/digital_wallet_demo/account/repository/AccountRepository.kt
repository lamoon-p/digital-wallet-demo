package com.lamoonp.digital_wallet_demo.account.repository

import com.lamoonp.digital_wallet_demo.account.model.Account
import com.lamoonp.digital_wallet_demo.account.model.AccountStatus
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface AccountRepository : ReactiveMongoRepository<Account, String>, AccountRepositoryCustom {
    fun findByUserId(userId: String): Flux<Account>
    fun findByAccountId(accountId: UUID): Mono<Account>
    fun findByStatus(status: AccountStatus): Flux<Account>
}