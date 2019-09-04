package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


/**
 * Contains the data of a standard Cosmos transaction.
 * More on this type can be found on the official Cosmos.network documentation at
 * https://cosmos.network/docs/spec/auth/03_types.html#stdtx
 */
data class StdTx(
    @JsonProperty("msg") val messages: List<StdMsg>,
    @JsonProperty("fee") val fee: StdFee,
    @JsonProperty("signatures") val signatures: List<StdSignature>?,
    @JsonProperty("memo") val memo: String
) {

    override fun toString(): String {
        return jacksonObjectMapper()
            .writeValueAsString(
                mapOf(
                    "type" to "cosmos-sdk/StdTx",
                    "value" to this
                )
            )
    }
}
