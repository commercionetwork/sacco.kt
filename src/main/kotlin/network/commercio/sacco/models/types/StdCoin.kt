package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holds some amount of one currency.
 * For more information about this type, visit the official Cosmos.network documentation at
 * https://godoc.org/github.com/cosmos/cosmos-sdk/types#Coin
 */
data class StdCoin(
    @JsonProperty("denom") val denom: String,
    @JsonProperty("amount") val amount: String
)