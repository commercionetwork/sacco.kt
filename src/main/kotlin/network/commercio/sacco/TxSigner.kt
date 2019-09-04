package network.commercio.sacco

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.encoding.toBase64
import network.commercio.sacco.models.account.AccountData
import network.commercio.sacco.models.types.*
import network.commercio.sacco.utils.ChainRepository

/**
 * Allows to easily sign a [StdTx] object that already contains a message.
 */
object TxSigner {

    /**
     * Signs the given [stdTx] using the data contained inside the given [wallet].
     *
     * NOTE. This method is [suspend] because it needs to ask the network in order to get the current account
     * number and sequence number when composing the message signature.
     */
    suspend fun signStdTx(wallet: Wallet, stdTx: StdTx): StdTx {
        // Get the account data from the network
        val account = ChainRepository.getAccountData(wallet)

        // Sign each message
        val signatures = stdTx.messages.map { msg ->
            getStdSignature(wallet, account, msg, stdTx.fee, stdTx.memo)
        }

        // Assemble the transaction
        return stdTx.copy(signatures = signatures)
    }

    /**
     * Creates an [StdSignature] object containing the signature value of the given [msg].
     * When creating the signature, the given [fee] and [memo] are inserted inside the signature, and the whole
     * JSON object is signed using the provided [wallet].
     *
     * Note that in order to properly sign a message, the following operations are performed.
     *
     * 1. The message value keys are sorted alphabetically
     * 2. A [StdSignatureMessage] is created, containing all the necessary data.
     * 3. The [StdSignatureMessage] is converted to a byte array, hashed using the SHA-256 and signed using the private key
     *    present inside the user wallet.
     * 4. The signed data is converted into a Base64 string and put inside a new [StdSignature] object.
     */
    private fun getStdSignature(
        wallet: Wallet,
        account: AccountData,
        msg: StdMsg,
        fee: StdFee,
        memo: String
    ): StdSignature {

        // Create the signature object
        val signature = StdSignatureMessage(
            accountNumber = account.accountNumber,
            chainId = wallet.networkInfo.id,
            memo = memo,
            msgs = listOf(msg),
            sequence = account.sequence,
            fee = fee
        )

        // Convert the signature to a JSON and sort it
        val objectMapper = jacksonObjectMapper().apply {
            configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        }
        val jsonSignData = objectMapper.writeValueAsString(signature)

        // Sign the message
        val signatureData = wallet.signTxData(jsonSignData)

        // Get the compressed Base64 public key
        val pubKeyCompressed = wallet.ecKey.pubKeyPoint.getEncoded(true)

        // Build the StdSignature
        return StdSignature(
            value = signatureData.toBase64(),
            pubKey = StdSignature.PubKey(
                type = "tendermint/PubKeySecp256k1",
                value = pubKeyCompressed.toBase64()
            )
        )
    }
}