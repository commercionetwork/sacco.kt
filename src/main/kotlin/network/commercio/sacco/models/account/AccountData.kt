package network.commercio.sacco.models.account

import network.commercio.sacco.models.types.StdCoin

data class AccountData(
    val accountNumber: String,
    val sequence: String,
    val coins: List<StdCoin>
)