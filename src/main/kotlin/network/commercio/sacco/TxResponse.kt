package network.commercio.sacco

/**
 * Represents the response that is returned from a full node when sending a transaction.
 */
sealed class TxResponse {

    /**
     * Represents a successful transaction.
     * @property txHash represents the hash of the inserted transaction.
     */
    data class Successful(val txHash: String) : TxResponse()

    /**
     * Represents an errored transaction.
     * @property code code of the error
     * @property message error message
     */
    data class Error(val code: Int, val message: String) : TxResponse()
}