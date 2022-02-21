package network.commercio.sacco.models.account

import com.fasterxml.jackson.annotation.JsonProperty
import network.commercio.sacco.models.types.StdCoin
import network.commercio.sacco.models.types.StdSignature

/**
 * Contains all the data that are retrieved from the /auth/accounts API related to
 * a single account and its current state.
 */
internal data class AccountData(
    @JsonProperty("address") val address: String,
    @JsonProperty("public_key") val publicKey: StdSignature.PubKey,
    @JsonProperty("account_number") val accountNumber: String,
    @JsonProperty("sequence") val sequence: String,
    @JsonProperty("coins") val coins: List<StdCoin>? = null
)