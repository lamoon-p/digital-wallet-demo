package com.lamoonp.digital_wallet_demo.accountservice.exception

class AccountNotFoundException(message: String) : RuntimeException(message)
class InsufficientBalanceException(message: String) : RuntimeException(message)
class InvalidAccountDataException(message: String) : RuntimeException(message)
class InvalidAccountStatusException(message: String) : RuntimeException(message)