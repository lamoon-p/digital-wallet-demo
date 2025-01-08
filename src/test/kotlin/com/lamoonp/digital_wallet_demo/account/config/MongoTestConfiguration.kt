package com.lamoonp.digital_wallet_demo.account.config

import com.mongodb.WriteConcern
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@TestConfiguration
class MongoTestConfiguration {
    @Bean
    fun mongoTemplate(mongoDbFactory: ReactiveMongoDatabaseFactory): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(mongoDbFactory).apply {
            setWriteConcern(WriteConcern.ACKNOWLEDGED)
        }
    }
}