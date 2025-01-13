package com.lamoonp.digital_wallet_demo.accountservice.repository

import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class AccountRepositoryCustomImpl(
    private val mongoTemplate: ReactiveMongoTemplate
) : AccountRepositoryCustom {
    override fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account> {
        val query = Query(Criteria.where("accountId").`is`(accountId))
        val update = Update().set("balance", amount).set("updatedAt", LocalDateTime.now())

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions().returnNew(true),
            Account::class.java
        )
    }
}