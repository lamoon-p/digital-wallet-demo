package com.lamoonp.digital_wallet_demo.account.util

import com.lamoonp.digital_wallet_demo.account.model.Account
import com.lamoonp.digital_wallet_demo.account.model.AccountStatus
import java.math.BigDecimal

object AccountTestFactory {
    fun createAccount(
        userId: String = "test-user",
        balance: BigDecimal = BigDecimal(10000.00),
        currency: String = "THB",
        status: AccountStatus = AccountStatus.ACTIVE
    ) = Account(
        userId = userId,
        balance = balance,
        currency = currency,
        status = status
    )
}