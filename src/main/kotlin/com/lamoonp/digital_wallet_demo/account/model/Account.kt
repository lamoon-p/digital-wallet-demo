package com.lamoonp.digital_wallet_demo.account.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "accounts")
data class Account(
    @Id
    val id: String = ObjectId().toString(),
    val accountId: UUID = UUID.randomUUID(),
    val userId: String,
    val balance: BigDecimal,
    val currency: String,
    val status: AccountStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)