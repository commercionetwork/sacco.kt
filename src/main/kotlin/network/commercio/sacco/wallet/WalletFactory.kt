package network.commercio.sacco.wallet

import network.commercio.sacco.Wallet

/**
 * Allows the retrieval of the wallet of the user that is going to sign the transactions.
 */
interface WalletFactory {

    /**
     * Returns the [Wallet] of the user that is going to sign the transactions.
     */
    suspend fun getWallet(): Wallet
}