package network.commercio.sacco.models.messages

import network.commercio.sacco.models.types.Input
import network.commercio.sacco.models.types.Output
import network.commercio.sacco.models.types.StdMsg

/**
 * Represents a message that should be used when wanting to send a specific amount
 * of tokens from one user to another one.
 */
data class MsgMultiSend(
    private val inputs: List<Input>,
    private val outputs: List<Output>
) : StdMsg(
    type = "cosmos-sdk/MsgMultiSend",
    value = mapOf(
        "inputs" to inputs,
        "outputs" to outputs
    )
)
