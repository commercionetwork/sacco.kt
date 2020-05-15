package network.commercio.sacco

import network.commercio.sacco.crypto.TransactionSigner
import network.commercio.sacco.crypto.convertBits
import network.commercio.sacco.encoding.toBase64
import org.bitcoinj.core.Bech32
import org.bitcoinj.core.ECKey
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.math.ec.ECPoint
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.encoders.Hex
import org.kethereum.bip39.generateMnemonic
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.toKey
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.web3j.crypto.ECDSASignature
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.ECPublicKeySpec
import java.util.Base64.getEncoder


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
            val pubKeyHash = privateEcKey.pubKeyHash
            return Bech32.encode(networkInfo.bech32Hrp, pubKeyHash.convertBits())
        }

    /**
     *  Returns the associated [publicKey] as a Bech32 string
     */
    val bech32PublicKey: String
        get() {
            val type = byteArrayOf(235.toByte(), 90, 233.toByte(), 135.toByte(), 33)
            val prefix = networkInfo.bech32Hrp + "pub"
            val pubKeyCompressed = pubKeyPoint.getEncoded(true)
            val fullPublicKey = (type + pubKeyCompressed).convertBits()
            return Bech32.encode(prefix, fullPublicKey)

        }

    /**
     * Gets the private key in the form of an [ECKey] object.
     */
    private val privateEcKey: ECKey
        get() {
            val privateKeyHex = privateKey.key.toHexStringNoPrefix()
            return ECKey.fromPrivate(BigInteger(privateKeyHex, 16))
        }

    /**
     * Gets the private key in the form of a [java.security.PrivateKey] object.
     */
    private val ecPrivateKey: java.security.PrivateKey by lazy {
        val ecParamSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val privateKeySpec = ECPrivateKeySpec(privateEcKey.privKey, ecParamSpec)
        KeyFactory.getInstance("EC", BouncyCastleProvider()).generatePrivate(privateKeySpec)
    }

    /**
     * Gets the public key in the form of a [java.security.PublicKey] object.
     */
    val ecPublicKey: java.security.PublicKey by lazy {
        val point = java.security.spec.ECPoint(pubKeyPoint.xCoord.toBigInteger(), pubKeyPoint.yCoord.toBigInteger())
        val parameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val spec = ECNamedCurveSpec(
            "secp256k1",
            parameterSpec.curve,
            parameterSpec.g,
            parameterSpec.n,
            parameterSpec.h,
            parameterSpec.seed
        )
        KeyFactory.getInstance("EC", BouncyCastleProvider()).generatePublic(ECPublicKeySpec(point, spec))
    }

    /**
     * Gets the public key in the form of an elliptic curve point object
     */
    val pubKeyPoint: ECPoint = privateEcKey.pubKeyPoint

    /**
     * Returns the public key in the form of a HEX string
     */
    val pubKeyAsHex: String = privateEcKey.publicKeyAsHex

    /**
     * Signs the given [data] with the private key contained inside this wallet.
     * The resulting signature is going to be 64 bytes long and composed of the
     * `r` component and the `s` component put together.
     *
     * If you want to sign data *not for chain usage* you should use
     * the [sign] method instead.
     */
    fun signTxData(data: ByteArray): ByteArray {
        val hash = MessageDigest.getInstance("SHA-256").digest(data)
        return TransactionSigner.deriveFrom(hash, privateEcKey)
    }


    fun sign(data: ByteArray): ByteArray {

//        ORIGINALE SACCO.KT --- togliendo BouncyCastleProvider () diventa deterministica
        return Signature.getInstance("SHA256WithECDSA", BouncyCastleProvider()).apply {
            initSign(ecPrivateKey)
            update(data)
        }.sign()


        //TENTATIVO N.1
        // size 64 e aspetto coerente con quella creata in dart.
        // Non funziona: unauthorized: proof signature verification failed

//        val eCDSASignature: ECDSASignature = ECKeyPair(privateKey.key, publicKey.key).sign(data)
//        val R = eCDSASignature.r
//        val S = eCDSASignature.s
//        val union = (R.toByteArray() + S.toByteArray())
//
//
//        print("\nMetodo sign del Wallet di sacco:\neCDSASignature:  \n ${eCDSASignature.r} \n  ${eCDSASignature.s}")
//        print("\nR.toByteArray(): ")
//        R.toByteArray().forEach { print("$it, ") }
//        print("\nS.toByteArray(): ")
//        S.toByteArray().forEach { print("$it, ") }
//        print("\nunion: ")
//        union.forEach { print("$it, ") }
//
//        return union


        //TENTATIVO N.2
        // size 64 e aspetto coerente con quella creata in dart.
        // Non funziona: unauthorized: proof signature verification failed

//        val privKey =privateKey.key
//        val pubKey = Sign.publicKeyFromPrivate(privKey)
//        val keyPair = ECKeyPair(privKey, pubKey)
//        println("Private key: " + privKey.toString(16))
//        println("Public key: " + pubKey.toString(16))
//        //System.out.println("Public key (compressed): " + compressPubKey(pubKey))
//
//        val msg = "Message for signing"
//        val msgHash = Hash.sha3(msg.toByteArray())
//        val signature = Sign.signMessage(msgHash, keyPair, false)
//        println("Msg: $msg")
//        System.out.println("Msg hash: " + Hex.toHexString(msgHash))
//        System.out.printf(
//            "Signature: [ r = %s, s = %s]\n",
//            Hex.toHexString(signature.r),
//            Hex.toHexString(signature.s)
//        )
//
//        val pubKeyRecovered = Sign.signedMessageToKey(msg.toByteArray(), signature)
//        println("Recovered public key: " + pubKeyRecovered.toString(16))
//
//        val validSig = pubKey == pubKeyRecovered
//        println("Signature valid? $validSig")
//
//        return (signature.r+signature.s)


        //TENTATIVO N.2
        // size 64 e aspetto coerente con quella creata in dart.
        // Non funziona: unauthorized: proof signature verification failed

//        val privKey =privateKey.key
//        val pubKey = Sign.publicKeyFromPrivate(privKey)
//        val keyPair = ECKeyPair(privKey, pubKey)
//        println("Private key: " + privKey.toString(16))
//        println("Public key: " + pubKey.toString(16))
//        //System.out.println("Public key (compressed): " + compressPubKey(pubKey))
//
//
//        val msgHash = Hash.sha3(data)
//        val signature = Sign.signMessage(msgHash, keyPair, false)
//
//        System.out.println("Msg hash: " + Hex.toHexString(msgHash))
//        System.out.printf(
//            "Signature: [ r = %s, s = %s]\n",
//            Hex.toHexString(signature.r),
//            Hex.toHexString(signature.s)
//        )
//
////        val pubKeyRecovered = Sign.signedMessageToKey(data, signature)
////        println("Recovered public key: " + pubKeyRecovered.toString(16))
////
////        val validSig = pubKey == pubKeyRecovered
////        println("Signature valid? $validSig")
//
//        return (signature.r+signature.s)



        //TENTATIVO N.3
        // size variabile

//        val signature = Signature.getInstance("SHA256withECDSA");
//        val secureRandom = SecureRandom()
//
//        signature.initSign(ecPrivateKey, secureRandom);
//        signature.update(data);
//
//        val digitalSignature = signature.sign();
//        return digitalSignature

    }


    companion object {
        private const val BASE_DERIVATION_PATH = "m/44'/118'/0'/0"

        /**
         * Derives the private key from the given [mnemonic] using the specified [networkInfo].
         * Optionally can define a different derivation path setting [lastDerivationPathSegment].
         */
        fun derive(
            mnemonic: List<String>, networkInfo: NetworkInfo, lastDerivationPathSegment: Int = 0
        ): Wallet {

            if (lastDerivationPathSegment < 0)
                throw Exception("Invalid index format:  $lastDerivationPathSegment, Number must be positive")

            val keyPair = MnemonicWords(mnemonic).toKey("$BASE_DERIVATION_PATH/$lastDerivationPathSegment").keyPair
            return Wallet(
                privateKey = keyPair.privateKey,
                publicKey = keyPair.publicKey,
                networkInfo = networkInfo
            )
        }

        /**
         * Generates a new random wallet for the given [networkInfo].
         */
        fun random(networkInfo: NetworkInfo): Wallet {
            val mnemonic = generateMnemonic(strength = 256, wordList = WORDLIST_ENGLISH).split(" ")
            return derive(mnemonic, networkInfo)
        }
    }
}