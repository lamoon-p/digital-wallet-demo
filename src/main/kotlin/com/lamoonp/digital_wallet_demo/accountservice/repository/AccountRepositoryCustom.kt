package com.lamoonp.digital_wallet_demo.accountservice.repository

import com.lamoonp.digital_wallet_demo.accountservice.model.Account
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

interface AccountRepositoryCustom {
    fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account>
}