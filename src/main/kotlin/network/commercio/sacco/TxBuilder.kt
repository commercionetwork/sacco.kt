package network.commercio.sacco

import network.commercio.sacco.models.types.StdFee
import network.commercio.sacco.models.types.StdMsg
import network.commercio.sacco.models.types.StdTx

/**
 * Allows to easily build and sign a [StdTx] that can later be sent over
 * the network.
 */
object TxBuilder {

    /**
     * Builds a [StdTx] object containing the given [stdMsgs] and having the
     * optional [memo] and [fee] specified.
     */
    fun buildStdTx(
        stdMsgs: List<StdMsg>,
        memo: String = "",
        fee: StdFee = StdFee(gas = "200000", amount = listOf())
    ): StdTx {
        return StdTx(
            messages = stdMsgs,
            memo = memo,
            fee = fee,
            signatures = null
        )
    }
}