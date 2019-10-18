package network.commercio.sacco.examples

import kotlinx.coroutines.runBlocking
import network.commercio.sacco.*
import network.commercio.sacco.models.messages.MsgSend
import network.commercio.sacco.models.types.StdCoin
import network.commercio.sacco.models.types.StdFee

fun main() = runBlocking {
    val mnemonic =
        "sibling auction sibling flavor judge foil tube dust work mixed crush action menu property project ride crouch hat mom scale start ill spare panther"
            .split(" ")

    // Create a NetworkInfo
    val networkInfo = NetworkInfo(
        bech32Hrp = "cosmos",
        lcdUrl = "http://localhost:1317"
    )

    // Create a wallet
    val wallet = Wallet.derive(mnemonic, networkInfo)

    // Build a transaction
    val message = MsgSend(
        fromAddress = "cosmos1hafptm4zxy5nw8rd2pxyg83c5ls2v62tstzuv2",
        toAddress = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
        amount = listOf(StdCoin(amount = "100", denom = "uatom"))
    )
    val fee = StdFee(gas = "200000", amount = listOf(StdCoin(amount = "250", denom = "uatom")))
    val tx = TxBuilder.buildStdTx(stdMsgs = listOf(message), fee = fee)

    // Sign the transaction
    val signedTx = TxSigner.signStdTx(wallet = wallet, stdTx = tx)

    // Send the transaction
    val txHash = TxSender.broadcastStdTx(wallet = wallet, stdTx = signedTx)
    println("Tx sent successfully. Hash: $txHash")
}