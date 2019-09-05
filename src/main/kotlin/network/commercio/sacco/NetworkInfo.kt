package network.commercio.sacco

/**
 * Contains all the information about a single Cosmos-SDK based chain.
 */
data class NetworkInfo(
    val id: String,
    val bech32Hrp: String,
    val lcdUrl: String,
    val name: String? = null,
    val iconUrl: String? = null,
    val defaultTokenDenom: String? = null
)