package com.lamoonp.digital_wallet_demo.account.repository

import com.lamoonp.digital_wallet_demo.account.model.Account
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

interface AccountRepositoryCustom {
    fun updateBalance(accountId: UUID, amount: BigDecimal): Mono<Account>
}