package network.commercio.sacco

/**
 * Represents the response that is returned from a full node when sending a transaction.
 */
sealed class TxResponse {

    /**
     * Represents a successful transaction.
     * @property txHash represents the hash of the inserted transaction.
     * @property code represents the error related if the transaction goes wrong after broadcasting.
     */
    data class Successful(val txHash: String, val code: Int?) : TxResponse()

    /**
     * Represents an errored transaction before broadcasting it.
     * @property code code of the error
     * @property message error message
     */
    data class Error(val code: Int, val message: String) : TxResponse()
}
