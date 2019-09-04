package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains the data of a message signature.
 * More on this type can be found on the Cosmos.network official documentation at
 * https://cosmos.network/docs/spec/auth/03_types.html#stdsigndoc
 */
data class StdSignatureMessage(
    @JsonProperty("account_number") val accountNumber: String,
    @JsonProperty("chain_id") val chainId: String,
    @JsonProperty("fee") val fee: StdFee,
    @JsonProperty("memo") val memo: String?,
    @JsonProperty("msgs") val msgs: List<StdMsg>,
    @JsonProperty("sequence") val sequence: String
)
