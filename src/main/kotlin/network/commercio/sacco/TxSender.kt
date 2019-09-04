package network.commercio.sacco

import network.commercio.sacco.models.types.StdTx
import network.commercio.sacco.utils.ChainRepository

/**
 * Allows to easily send a [StdTx] using the data contained
 * inside the specified [Wallet] instance.
 */
object TxSender {

    /**
     * Broadcasts the given [stdTx] using the info contained inside the given [wallet].
     * Returns the hash of the transaction once it has been send, or throws
     * and exception if an error is risen during the sending.
     */
    suspend fun broadcastStdTx(wallet: Wallet, stdTx: StdTx): String {
        return ChainRepository.postTx(wallet, stdTx)
    }
}