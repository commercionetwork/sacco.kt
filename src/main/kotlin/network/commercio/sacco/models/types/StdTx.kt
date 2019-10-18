package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty


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
)
