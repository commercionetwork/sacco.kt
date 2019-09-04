package network.commercio.sacco

import network.commercio.sacco.crypto.TransactionSigner
import network.commercio.sacco.crypto.convertBits
import org.bitcoinj.core.Bech32
import org.bitcoinj.core.ECKey
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.toKey
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Contains the data of the wallet that is going to be used while creating and signing the transactions.
 */
data class Wallet internal constructor(
    private val privateKey: PrivateKey,
    val publicKey: PublicKey,
    val networkInfo: NetworkInfo
) {

    /**
     * Bech32 representation of the address associated to this wallet.
     */
    val bech32Address: String
        get() {
            val pubKeyHash = ecKey.pubKeyHash
            return Bech32.encode(networkInfo.bech32Hrp, pubKeyHash.convertBits())
        }

    /**
     * [ECKey] instance associated to this wallet.
     */
    val ecKey: ECKey
        get() {
            val privateKeyHex = privateKey.key.toHexStringNoPrefix()
            return ECKey.fromPrivate(BigInteger(privateKeyHex, 16))
        }

    /**
     * Signs the given [data] with the private key contained inside this wallet.
     */
    fun signTxData(data: String): ByteArray {
        val bytes = data.toByteArray()
        val hash = MessageDigest.getInstance("SHA-256").digest(bytes)
        return TransactionSigner.deriveFrom(hash, ecKey)
    }

    companion object {

        /**
         * Derives the private key from the given [mnemonic] using the specified
         * [derivationPath] and [networkInfo].
         */
        fun derive(mnemonic: List<String>, derivationPath: String, networkInfo: NetworkInfo): Wallet {
            val keyPair = MnemonicWords(mnemonic).toKey(derivationPath).keyPair
            return Wallet(
                privateKey = keyPair.privateKey,
                publicKey = keyPair.publicKey,
                networkInfo = networkInfo
            )
        }
    }
}