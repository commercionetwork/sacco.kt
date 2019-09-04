package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains the data of the fee that the user is going to pay in order for his message to be transmitted.
 * More on this type can be found inside the Cosmos.network official documentation:
 * https://cosmos.network/docs/spec/auth/03_types.html#stdfee
 */
data class StdFee(
    @JsonProperty("amount") val amount: List<StdCoin>,
    @JsonProperty("gas") val gas: String
)
