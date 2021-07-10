package network.commercio.sacco.models.types

import com.fasterxml.jackson.annotation.JsonProperty

data class Input(
    @JsonProperty("address") val address: String,
    @JsonProperty("coins") val coins: List<StdCoin>
)
