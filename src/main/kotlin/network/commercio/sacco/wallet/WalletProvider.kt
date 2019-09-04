@file:JvmName("WalletProvider")

package network.commercio.sacco.wallet

import network.commercio.sacco.Wallet

private var wallet: Wallet? = null
private var walletFactory: WalletFactory? = null

private val message = """
        Retrieved wallet is null.
        Please make sure you set the proper Wallet factory with WalletProvider.walletFactory = YourFactory.
        Also make sure your factory is returning a non-null wallet.
    """.trimIndent()


/**
 * Sets the [WalletFactory] that is going to be used to retrieve a [Wallet] when creating and signing a
 * transaction.
 */
fun setWalletFactory(factory: WalletFactory) {
    walletFactory = factory
}

/**
 * Retrieves the [Wallet] of the user checking if there is already one set, or calls the provided [WalletFactory]
 * for one.
 * @throws NullPointerException if no [WalletFactory] has been provided.
 */
suspend fun getWallet(): Wallet {
    return wallet ?: walletFactory?.getWallet()?.also { wallet = it } ?: throw NullPointerException(message)
}

