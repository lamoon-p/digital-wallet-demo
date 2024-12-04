package com.lamoonp.digital_wallet_demo.account.config

import com.lamoonp.digital_wallet_demo.account.model.Account
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.Index

@Configuration
class MongoIndexConfig(
    private val mongoTemplate: ReactiveMongoTemplate
) {
    @PostConstruct
    fun initIndexes() {
        mongoTemplate.indexOps(Account::class.java)
            .ensureIndex(
                Index()
                    .on("accountId", Sort.Direction.ASC)
                    .unique()
            ).subscribe()

        mongoTemplate.indexOps(Account::class.java)
            .ensureIndex(
                Index()
                    .on("userId", Sort.Direction.ASC)
            ).subscribe()
    }
}