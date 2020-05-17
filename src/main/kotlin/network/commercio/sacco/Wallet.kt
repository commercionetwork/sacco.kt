package network.commercio.sacco

import network.commercio.sacco.crypto.TransactionSigner
import network.commercio.sacco.crypto.convertBits
import org.bitcoinj.core.Bech32
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithRandom
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.math.ec.ECPoint
import org.kethereum.bip39.generateMnemonic
import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.toKey
import org.kethereum.bip39.wordlists.WORDLIST_ENGLISH
import org.kethereum.extensions.toHexStringNoPrefix
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.ECPublicKeySpec
import java.util.*


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
//        return Signature.getInstance("SHA256WithECDSA", BouncyCastleProvider()).apply {
//            initSign(ecPrivateKey)
//            update(data)
//        }.sign()

        //CODICE DART
//        Uint8List sign1(Uint8List data) {
//            final ecdsaSigner = Signer("SHA-256/ECDSA")..init(true,
//            ParametersWithRandom(
//                PrivateKeyParameter(_ecPrivateKey),
//                _getSecureRandom(),
//            ));
//            final dataToGenerate= ecdsaSigner.generateSignature(data);
//            ECSignature ecSignature =_toCanonicalised(dataToGenerate);
//            final sigBytes = Uint8List.fromList(pcUtils.encodeBigInt(ecSignature.r) + pcUtils.encodeBigInt(ecSignature.s),
//            );
//            return sigBytes;}


//TENTAIVO 7


        val secureRandom = SecureRandom().apply {
            nextBytes(ByteArray(32))
        }


        //return data

        // fun sign(data: ByteArray):ByteArray{
        //val secureRandom = SecureRandom()

        val eCNamed = ECNamedCurveTable.getParameterSpec("secp256k1")
        val params = ParametersWithRandom(
            ECPrivateKeyParameters(
                privateEcKey.privKey,
                ECDomainParameters(eCNamed.curve, eCNamed.g, eCNamed.n)
            ), secureRandom
        )
        val signer = ECDSASigner()//(HMacDSAKCalculator(SHA256Digest()))
        signer.init(true, params)
        val components = signer.generateSignature(data)
        val signature = org.web3j.crypto.ECDSASignature(components[0], components[1]).toCanonicalised()
        val rBytes = Utils.bigIntegerToBytes(signature.r, 32)
        val sBytes = Utils.bigIntegerToBytes(signature.s, 32)
        return byteArrayOf(*rBytes, *sBytes)
        // }


//val eCDSASigner= ECDSASigner().init(true, ParametersWithRandom(ECPrivateKeyParameters(), SecureRandom.getInstance("")) )

        //TENTATIVO N.1 - Modificato
        // size 64 e aspetto coerente con quella creata in dart.
        // Non funziona: unauthorized: proof signature verification failed
//        print("\nprivateKey.key: ${privateKey.key}")
//        print("\npublicKey.key: ${publicKey.key}")
//
//        val eCKeyPair =ECKeyPair(privateKey.key, publicKey.key)
//        print("eCKeyPair: $eCKeyPair")
//        val eCDSASignature: ECDSASignature =  eCKeyPair.sign(data)
//        val eCDSASignatureCanonicalised = eCDSASignature.toCanonicalised()
//        val R = eCDSASignatureCanonicalised.r
//        val S = eCDSASignatureCanonicalised.s
//
//        print("\nMetodo sign del Wallet di sacco.kt:\neCDSASignature:  \n ${eCDSASignature.r} \n  ${eCDSASignature.s}")
//
//        // stampe che mostrano la differenza tra ByteArray e UByteArray :i valori sono gli stessi, solo stampati in
//        // formato diverso
//        print("\nR.toUByteArray(): ")
//        R.toByteArray().toUByteArray().forEach { print("$it, ") }
//        print("\nS.toUByteArray(): ")
//        S.toByteArray().toUByteArray().forEach { print("$it, ") }
//        print("\nR.toByteArray(): ")
//        R.toByteArray().forEach { print("$it, ") }
//        print("\nS.toByteArray(): ")
//        S.toByteArray().forEach { print("$it, ") }
//
////        print("\nunion in formato toByteArray: ")
////        val unionByteArray = R.toByteArray() + S.toByteArray()
////        unionByteArray.forEach { print("$it, ") }
////        print("\nunion in formato toUByteArray: ")
////        val unionUByteArray = R.toByteArray().toUByteArray() + S.toByteArray().toUByteArray()
////        unionUByteArray.forEach { print("$it, ") }
//
//
//        val result = R.toByteArray().copyOfRange(R.toByteArray().size-32,R.toByteArray().size) + S.toByteArray()
//        val resultToBase64 = result.toBase64()
//        print("\nR size: ${R.toByteArray().size}, S size: ${S.toByteArray().size}\n")
//
//        //R può avere una size>32, allora elimino i primi size-32 valori e il valore restituito combacia con quello di Dart:
//        result.forEach { print("$it, ") }
//
//        print("\n valore ritornato convertito in base64: $resultToBase64")
//        return result

        //TENTATIVO N.4
        // size 64 e aspetto coerente con quella creata in dart.
//        val eCDSASignatur: ECDSASignature = ECKeyPair(privateKey.key, publicKey.key).sign(data).toCanonicalised()
//        val signature = eCDSASignatur
//
//        val result = ByteArray(64)
//        val rBytes = Utils.bigIntegerToBytes(signature.r, 32)
//        val sBytes = Utils.bigIntegerToBytes(signature.s, 32)
//
//        rBytes.copyInto(result, destinationOffset = 0, startIndex = 0, endIndex = rBytes.size)
//        sBytes.copyInto(result, destinationOffset = 32, startIndex = 0, endIndex = sBytes.size)
//
//        return result

        //TENTATIVO N.5

//        val hash = Sha256Hash.wrap(data.toBase64());
//        print("\nHASH:" + hash)
//        val signature = privateEcKey.sign(hash)
//        print("\nSIGNATURE:" + signature.toString())
//        val R = signature.r
//        val S = signature.s
//        return (R.toByteArray() + S.toByteArray())

        //TENTATIVO N.6

//        val hashTransaction = Sha256Hash.wrap(Sha256Hash.hash(data))
//        print("\nhashTransaction:" + hashTransaction)
//        val signature = privateEcKey.sign(hashTransaction)
//        print("\nSIGNATURE:" + signature.toString())
//        val R = signature.r
//        val S = signature.s
//
//        val union = R.toByteArray() + S.toByteArray()
//
//
//        // print("\nMetodo sign del Wallet di sacco:\neCDSASignature:   \n ${eCDSASignature.r} \n  ${eCDSASignature.s}")
//        print("\nR.toByteArray(): \n$R\n")
//        R.toByteArray().forEach { print("$it, ") }
//        print("\nS.toByteArray():  \n$S\n ")
//        S.toByteArray().forEach { print("$it, ") }
//        print("\nunion: ")
//        union.forEach { print("$it, ") }
//
//        return (R.toByteArray() + S.toByteArray())

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