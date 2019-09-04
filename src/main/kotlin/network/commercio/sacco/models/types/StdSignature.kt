package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains the data of a Cosmos.network standard message signature.
 * More on this type can be found at the official Cosmos.network StdSignature documentation:
 * https://cosmos.network/docs/spec/auth/03_types.html#stdsignature
 */
data class StdSignature(
    @JsonProperty("pub_key") val pubKey: PubKey,
    @JsonProperty("signature") val value: String
) {

    data class PubKey(
        @JsonProperty("type") val type: String,
        @JsonProperty("value") val value: String
    )
}