package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Wraps a transaction value describing also its type.
 */
data class Tx(
    @JsonProperty("type") val type: String,
    @JsonProperty("value") val value: StdTx
)