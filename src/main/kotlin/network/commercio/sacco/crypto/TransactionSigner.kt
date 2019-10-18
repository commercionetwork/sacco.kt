package network.commercio.sacco.crypto

import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils

internal object TransactionSigner {

    /**
     * Signs the given [data] with the given [ecKey], retuning the signature bytes.
     */
    fun deriveFrom(data: ByteArray, ecKey: ECKey): ByteArray {
        val signature = ecKey.sign(Sha256Hash.wrap(data))

        val result = ByteArray(64)
        val rBytes = Utils.bigIntegerToBytes(signature.r, 32)
        val sBytes = Utils.bigIntegerToBytes(signature.s, 32)

        rBytes.copyInto(result, destinationOffset = 0, startIndex = 0, endIndex = rBytes.size)
        sBytes.copyInto(result, destinationOffset = 32, startIndex = 0, endIndex = sBytes.size)

        return result
    }
}