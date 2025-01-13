package com.lamoonp.digital_wallet_demo.accountservice.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfig(
    @Value("\${spring.data.mongodb.host}") private val host: String,
    @Value("\${spring.data.mongodb.port}") private val port: Int,
    @Value("\${spring.data.mongodb.database}") private val database: String,
    @Value("\${spring.data.mongodb.username}") private val username: String,
    @Value("\${spring.data.mongodb.password}") private val password: String,
) {
    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = "mongodb://$username:$password@$host:$port"
        return MongoClients.create(connectionString)
    }
}