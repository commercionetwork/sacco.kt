package network.commercio.sacco

import io.mockk.coEvery
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import network.commercio.sacco.models.account.AccountData
import network.commercio.sacco.models.chain.NodeInfo
import network.commercio.sacco.models.messages.MsgSend
import network.commercio.sacco.models.types.StdCoin
import network.commercio.sacco.models.types.StdFee
import network.commercio.sacco.utils.LCDService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TxSignerTests {

    private val mnemonic =
        "sibling auction sibling flavor judge foil tube dust work mixed crush action menu property project ride crouch hat mom scale start ill spare panther"
            .split(" ")

    @Test
    fun `StdTx with fee is signed correctly`() {
        val networkInfo = NetworkInfo(
            bech32Hrp = "cosmos",
            lcdUrl = "http://localhost:1317"
        )

        // Build a transaction
        val message = MsgSend(
            fromAddress = "cosmos1hafptm4zxy5nw8rd2pxyg83c5ls2v62tstzuv2",
            toAddress = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
            amount = listOf(StdCoin(amount = "100", denom = "uatom"))
        )
        val fee = StdFee(gas = "200000", amount = listOf(StdCoin(amount = "250", denom = "uatom")))
        val tx = TxBuilder.buildStdTx(stdMsgs = listOf(message), fee = fee)

        // Create a wallet
        val wallet = Wallet.derive(mnemonic, networkInfo)

        mockkObject(LCDService) {
            coEvery { LCDService.getNodeInfo(any()) } returns NodeInfo(info = NodeInfo.Info(chainId = "cosmos-hub2"))
            coEvery { LCDService.getAccountData(any()) } returns AccountData("0", "0", listOf())

            val signedTx = runBlocking { TxSigner.signStdTx(wallet = wallet, stdTx = tx) }
            assertEquals(1, signedTx.signatures?.size)

            val signature = signedTx.signatures!![0]
            assertEquals("tendermint/PubKeySecp256k1", signature.pubKey.type)
            assertEquals("ArMO2T5FNKkeF2aAZY012p/cpa9+PqKqw2GcQRPhAn3w", signature.pubKey.value)
            assertEquals(
                "m2op4CCBa39fRZD91WiqtBLKbUQI+1OWsc1tJkpDg+8FYB4y51KahGn26MskVMpTJl5gToIC1pX26hLbW1Kxrg==",
                signature.value
            )
        }
    }


    @Test
    fun `StdTx with fee is signed with the same value if AccountData has accountNumber and sequence as Integer`() {
        val networkInfo = NetworkInfo(
            bech32Hrp = "cosmos",
            lcdUrl = "http://localhost:1317"
        )

        // Build a transaction
        val message = MsgSend(
            fromAddress = "cosmos1hafptm4zxy5nw8rd2pxyg83c5ls2v62tstzuv2",
            toAddress = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
            amount = listOf(StdCoin(amount = "100", denom = "uatom"))
        )
        val fee = StdFee(gas = "200000", amount = listOf(StdCoin(amount = "250", denom = "uatom")))
        val tx = TxBuilder.buildStdTx(stdMsgs = listOf(message), fee = fee)

        // Create a wallet
        val wallet = Wallet.derive(mnemonic, networkInfo)

        mockkObject(LCDService) {
            coEvery { LCDService.getNodeInfo(any()) } returns NodeInfo(info = NodeInfo.Info(chainId = "cosmos-hub2"))
            coEvery { LCDService.getAccountData(any()) } returns AccountData(0, 0, listOf())

            val signedTx = runBlocking { TxSigner.signStdTx(wallet = wallet, stdTx = tx) }
            assertEquals(1, signedTx.signatures?.size)

            val signature = signedTx.signatures!![0]
            assertEquals("tendermint/PubKeySecp256k1", signature.pubKey.type)
            assertEquals(
                "m2op4CCBa39fRZD91WiqtBLKbUQI+1OWsc1tJkpDg+8FYB4y51KahGn26MskVMpTJl5gToIC1pX26hLbW1Kxrg==",
                signature.value
            )
        }
    }
}