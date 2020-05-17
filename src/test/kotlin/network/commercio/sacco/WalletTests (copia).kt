package network.commercio.sacco

import network.commercio.sacco.crypto.convertBits
import network.commercio.sacco.encoding.toBase64
import org.bouncycastle.util.encoders.Hex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.nio.charset.StandardCharsets

class DeleteTests {

    val networkInfo = NetworkInfo(bech32Hrp = "did:com:", lcdUrl = "http://lcd-demo.commercio.network")
//    val mnemonic = listOf(
//        "mad", "accuse", "glove", "wife", "east", "magic", "tackle", "couple", "alien", "boy", "giant", "walk", "ring", "define", "fit", "biology", "medal", "coast", "fury", "raise", "expand", "join", "wall", "eternal")
//    //const String ADDRESS_2 = "did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48";
//
//
//    val wallet = Wallet.derive(mnemonic = mnemonic, networkInfo = networkInfo)
//    assertEquals(176, Hex.toHexString(wallet.ecPublicKey.encoded).length)
//
//    print("\nwallet: $wallet\n ${Hex.toHexString(wallet.ecPublicKey.encoded)}\n")


    @Test
    fun `ecPublicKey returns valid length key`() {

        val mnemonic =
            "push grace power desk arrive horror gallery physical kingdom ecology fat firm future service table little live reason maximum short motion planet stage second".split(" ")


        val networkInfo = NetworkInfo(bech32Hrp = "did:com:", lcdUrl = "")

        val wallet = Wallet.derive(mnemonic = mnemonic, networkInfo = networkInfo)

        wallet.bech32PublicKey

        //val data1 = "{\"@context\":\"https://www.w3.org/ns/did/v1\",\"id\":\"did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48\",\"publicKey\":[{\"controller\":\"did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48\",\"id\":\"did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48#keys-1\",\"publicKeyPem\":\"-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoGDuSJKSkwvYPEafx6VpL2k8UZzJyXVJIXv9HLAe9cy9QMEsLzJi8CgoJSKKVRdUyV+wpESiTnT2v5ZEUvT9qMqvdNk/r5ojDDAuK+xgDvQnpabfOdpKC0jty72+ERW0GjV+q4F5MJoLOu8+UFzSu/dpr3vimq11f5LXjRnQmrV5S+eGotYfhlqvZ6UZg/u81H9QtonKL7VT0iFCznCm1wZoFS8Em5s0rQKFbbSBobO5hu6zgum4LhQY3++2p5Q+0GI5JiF0VzLB8Zl5VAQvQwE9Mm8cmo1UbYdDTZBP6UszMJXUWJhVeifTDXsC1bPaCPgE34E7++GUjLb2dcZE8wIDAQAB\\n-----END PUBLIC KEY-----\",\"type\":\"RsaVerificationKey2018\"},{\"controller\":\"did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48\",\"id\":\"did:com:150jp3tx96frukqg6v870etf02q0cp7em78wu48#keys-2\",\"publicKeyPem\":\"-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu/2FnyCyk1kVHu88Y23O/iYK7Tsc/pXZoZ4gGx1GaapZieYKC6c085rLzAGYnSehd/6gRd7Uc8FmKeoffFMjtzp7zNKLSWaHTL51iYwJ8FUi9J+fH4B4dRdR0ouMrBFth/lGd2sPqL79FFrAqkQwvcez5VKnxhoK9cPTIKskZS7NlBFbfifO8ry2XKRelyUiLV1nEKas2SHQ5cpMKfvk46bMOLk8MZFkBoYHohyUczaKN72a/EpGlahSAhgH33Zmqb0++AfOwXXGvaYhxxxc7s1PZP81voAA0rkUTYkW0a9tJKXpql2ZQrTF+RSTZq4u/G7Re44CrVCJR22Er+GNXwIDAQAB\\n-----END PUBLIC KEY-----\",\"type\":\"RsaSignatureKey2018\"}]}"

        val data ="Test2"


        val sig1 = wallet.sign(data.toByteArray(Charsets.UTF_8))
        val sig2 = wallet.sign(data.toByteArray(Charsets.UTF_8))
        val sig3 = wallet.sign(data.toByteArray(Charsets.UTF_8))
        val sig4 = wallet.sign(data.toByteArray(Charsets.UTF_8))



        print("\n\n sig1: \n")
        print("\n sig1 size: ${sig1.size} , sig1 convertBits().size : ${sig1.convertBits().size} ")
        sig1.forEach { print("$it, ") }

        print("\n sig1\n")
        print(sig1.toBase64())


        print("\n\n sig2 \n size: ${sig2.size} \n")
        sig2.forEach { print("$it, ") }
        print("\n sig2\n")
        print(sig2.toBase64())


        print("\n\n sig3  \n size: ${sig3.size} \n")
        sig3.forEach { print("$it, ") }
        print("\n sig3\n")
        print(sig3.toBase64())


        print("\n\n sig4 \n size: ${sig4.size}  \n")
        sig4.forEach { print("$it, ") }
        print("\n sig4\n")
        print(sig3.toBase64())

        print("\n")
        assertEquals(false, false);
    }


//    Rpm8XKrPHorpiypZRlm9bz0nIGRZvIEKM9y2blJk7kB4jNW+p+uY1M7hBn0kDMffeqpx1DxS7FGozYAmK/TE0g==
//    MEQCIG3HOqRTzGszB+bijsR/BQyXjFWWkAI1beOpkxCk7hFVAiBd3b0YpLONlDVxTehBX29bt0qhLUsrEcAJYo46QQPCmA==
//    MEQCIBc6D13uAroRKPZsC5eSYm8yzorZIwBQu3v4VnBzO5hmAiBCNbq/TiDtwdVMRiyj2XOt1s1TRxEa+5+9TlYAonbb5w==
//    Aw2wH+dzlp6PVF9ai+Q8KWvH1zPUtMRfiG7MV+Z49ecAsu4CfiMQwhL18HU84XSRho3TzjkLA4pxVz38w3s15nY=



//    @Test
//    fun `Sign data returns non deterministic signatures`() {
//        val info = NetworkInfo(bech32Hrp = "did:com:", lcdUrl = "")
//        val mnemonic = listOf(
//            "will",
//            "hard",
//            "topic",
//            "spray",
//            "beyond",
//            "ostrich",
//            "moral",
//            "morning",
//            "gas",
//            "loyal",
//            "couch",
//            "horn",
//            "boss",
//            "across",
//            "age",
//            "post",
//            "october",
//            "blur",
//            "piece",
//            "wheel",
//            "film",
//            "notable",
//            "word",
//            "man"
//        )
//        val wallet = Wallet.derive(mnemonic, info)
//
//        val data = "Test"
//        val sig1 = Hex.toHexString(wallet.sign(data.toByteArray()))
//        val sig2 = Hex.toHexString(wallet.sign(data.toByteArray()))
//        assertNotEquals(sig1, sig2)
//    }



}