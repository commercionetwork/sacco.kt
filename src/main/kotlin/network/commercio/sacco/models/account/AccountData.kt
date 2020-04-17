package network.commercio.sacco.models.account

import com.fasterxml.jackson.annotation.JsonProperty
import network.commercio.sacco.models.types.StdCoin

/**
 * Contains all the data that are retrieved from the /auth/accounts API related to
 * a single account and its current state.
 */
internal data class AccountData(
    @JsonProperty("account_number") val accountNumber: Any,
    @JsonProperty("sequence") val sequence: Any,
    @JsonProperty("coins") val coins: List<StdCoin>
)