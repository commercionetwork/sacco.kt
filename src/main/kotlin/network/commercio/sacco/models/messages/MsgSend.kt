package network.commercio.sacco.models.messages

import network.commercio.sacco.models.types.StdCoin
import network.commercio.sacco.models.types.StdMsg

/**
 * Represents a message that should be used when wanting to send a specific amount
 * of tokens from one user to another one.
 */
data class MsgSend(
    private val fromAddress: String,
    private val toAddress: String,
    private val amount: List<StdCoin>
) : StdMsg(type = "cosmos-sdk/MsgSend", value = mapOf()) {

    override val value: Map<String, Any>
        get() = mapOf(
            "from_address" to fromAddress,
            "to_address" to toAddress,
            "amount" to amount
        )
}