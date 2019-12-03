package network.commercio.sacco.utils

import com.fasterxml.jackson.annotation.JsonProperty

internal data class TxResponseJson(
    @JsonProperty("txhash") val hash: String,
    @JsonProperty("code") val code: Int?,
    @JsonProperty("logs") val logs: List<MsgLog>?,
    @JsonProperty("raw_log") val rawLog: String
)

internal data class MsgLog(
    @JsonProperty("msg_index") val msgIndex: Int,
    @JsonProperty("success") val success: Boolean,
    @JsonProperty("log") val log: String
)

internal data class MsgResultLog(
    @JsonProperty("codespace") val codeSpace: String,
    @JsonProperty("code") val code: Int,
    @JsonProperty("message") val message: String
)
