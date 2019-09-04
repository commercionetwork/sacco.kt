package network.commercio.sacco

data class NetworkInfo(
    val id: String,
    val bech32Hrp: String,
    val lcdUrl: String,
    val name: String? = null,
    val iconUrl: String? = null,
    val defaultTokenDenom: String? = null
)