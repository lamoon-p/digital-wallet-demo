package com.lamoonp.digital_wallet_demo.account.util

import com.lamoonp.digital_wallet_demo.account.model.Account
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import reactor.test.StepVerifier
import java.time.Duration
import kotlin.test.BeforeTest

@DataMongoTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMongoDbTest {

    companion object {
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .apply {
                withExposedPorts(27017)
                withStartupTimeout(Duration.ofSeconds(60))
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
            registry.add("spring.data.mongodb.host") { "digital-wallet-test" }
        }
    }

    @Autowired
    private lateinit var mongodbTemplate: ReactiveMongoTemplate

    @BeforeTest
    fun cleanDatabase() {
        StepVerifier.create(mongodbTemplate.dropCollection(Account::class.java))
            .expectComplete()
            .verify(Duration.ofSeconds(10))
    }
}