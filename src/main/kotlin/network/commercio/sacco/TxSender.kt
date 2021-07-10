package network.commercio.sacco

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.*
import java.util.concurrent.CompletableFuture
import network.commercio.sacco.models.types.StdTx
import network.commercio.sacco.utils.LCDService

/**
 * Allows to easily send a [StdTx] using the data contained
 * inside the specified [Wallet] instance.
 */
object TxSender {

    fun broadcastStdTxAsync(stdTx: StdTx, wallet: Wallet, mode: String = "sync"): CompletableFuture<TxResponse> =
      GlobalScope.future { broadcastStdTx(stdTx, wallet, mode) }

    /**
     * Broadcasts the given [stdTx] using the info contained inside the given [wallet] using the proper [mode] given
     * or the default one if none is passed.
     * Returns the hash of the transaction once it has been send, or throws
     * and exception if an error is risen during the sending.
     */
    suspend fun broadcastStdTx(stdTx: StdTx, wallet: Wallet, mode: String = "sync"): TxResponse {
        return LCDService.postTx(stdTx = stdTx, wallet = wallet, mode = mode)
    }
}
