![](.img/logo.jpg)

Sacco.kt is a Kotlin-JVM library that allows you to easily perform some operations related to the
[Cosmos.network](https://cosmos.network) ecosystem. This includes:

1. Creating an HD Wallet.
2. Creating a transaction.
3. Signing a transaction. 
4. Broadcasting a transaction. 

Being it in Kotlin-JVM means that you can use it inside your Android application as well as any JVM-based program. 

## Usage
### Creating a wallet
```kotlin
val derivationPath = "m/44'/118'/0'/0/0"
val networkInfo = NetworkInfo(id = "", bech32Hrp = "cosmos", lcdUrl = "")

val mnemonicString = "sibling auction sibling flavor judge foil tube dust work mixed crush action menu property project ride crouch hat mom scale start ill spare panther"
val mnemonic = mnemonicString.split(" ")
val wallet = Wallet.derive(mnemonic, derivationPath, networkInfo)
```  

### Creating a transaction
```kotlin
val message = MsgSend(
    fromAddress = "cosmos1hafptm4zxy5nw8rd2pxyg83c5ls2v62tstzuv2",
    toAddress = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
    amount = listOf(StdCoin(amount = "100", denom = "uatom"))
)

// The fee object is optional
val fee = StdFee(gas = "200000", amount = listOf(StdCoin(amount = "250", denom = "uatom")))

val tx = TxBuilder.buildStdTx(stdMsgs = listOf(message), fee = fee)
```

### Signing a transaction
```kotlin
val signedTx = TxSigner.signStdTx(wallet = wallet, stdTx = tx)
```

### Sending a transaction
```kotlin
val txHash = TxSender.broadcastStdTx(wallet = wallet, stdTx = signedTx)
println("Tx sent successfully. Hash: $txHash")
```

### Working example
A working example can be found inside the [Main.kt file](src/test/kotlin/network/commercio/sacco/examples/Main.kt).