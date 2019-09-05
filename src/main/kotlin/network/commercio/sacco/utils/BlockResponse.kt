package network.commercio.sacco.utils

import com.fasterxml.jackson.annotation.JsonProperty

data class BlockResponse(
    @JsonProperty("block_meta") val meta: Meta
) {
    data class Meta(
        @JsonProperty("header") val header: Header
    ) {
        data class Header(
            @JsonProperty("height") val height: Long
        )
    }
}