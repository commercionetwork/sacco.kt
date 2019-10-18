package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a generic message that can be inserted inside a transaction.
 */
open class StdMsg(
    @JsonProperty("type") val type: String,
    @JsonProperty("value") open val value: Map<String, Any?>
)