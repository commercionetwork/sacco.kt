package network.commercio.sacco

import org.bouncycastle.util.encoders.Hex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class WalletTests {

    private val networkInfo = NetworkInfo(bech32Hrp = "cosmos", lcdUrl = "")
    private val testVectors = mapOf(
        "cosmos1huydeevpz37sd9snkgul6070mstupukw00xkw9" to
                "final random flame cinnamon grunt hazard easily mutual resist pond solution define knife female tongue crime atom jaguar alert library best forum lesson rigid",
        "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k" to
                "must lottery surround bike cash option split aspect cram volume repeat goose enemy mouse ostrich crowd thing huge fiscal fuel canal tuna hair educate",
        "cosmos1wclftxxzt2sshqz0xtq4rxtk82wawyg6y2uafs" to
                "pencil flat shed laundry idle phone glow hint dilemma roast bulb shop spice birth rigid project bar night song pluck then illegal obvious syrup",
        "cosmos1sc0ppj28frtyeqgs9gjk39lfd78507s3cu9e5k" to
                "embrace subway again gift toilet price security ordinary zoo owner orbit age destroy invest little scheme crumble leisure remove muffin shoe deliver defy draw",
        "cosmos1l8yr93zkltwzdphd8g6073jgxslmf2pax7ml77" to
                "garage jungle error orient puzzle crater cancel walk tissue fence dynamic bean aisle ring adult truth dog chapter claw six exhaust soda planet cycle",
        "cosmos1gmf3mqgxy6s89ac0n2uwxaw7ax5js88e7a5jgh" to
                "seven confirm glass lawsuit flower test power rain animal argue fetch play local erupt curious certain february hover zone carpet pipe alarm capable box",
        "cosmos17c7nap702zdjwlqu6aystxy23kk252my4gkkfp" to
                "minor craft between drive depart endorse fresh blade drill help skull hub evolve door sea comic pulse chicken awesome rebel leave series live brain",
        "cosmos1m87tazfksqu8d6cxwaexzg2e7w9a5q9nwjt2sc" to
                "hurdle satisfy excess hub month great ordinary crane begin laugh evoke domain humor absent dawn blanket prefer ice ripple auto boost vast version soup",
        "cosmos1ase8zsfkqgvxw8yynfklq73u5utff0xxyzam58" to
                "pipe apple lobster gadget front cloud reject whip village idle ready concert general scrub silver neutral crop oyster tackle enlist winner milk duty tomato",
        "cosmos1vwf547ntuvt69u46vzyzwwffmuxyhx9c7kx7st" to
                "solve retire concert illegal garage recall skill power lyrics bunker vintage silver situate gadget talent settle left snow fire bubble bar robot swing senior"
    )

    @Test
    fun `Wallets are generated correctly`() {
        testVectors.forEach { (address, mnemonicString) ->
            val mnemonic = mnemonicString.split(" ")
            val wallet = Wallet.derive(mnemonic, networkInfo)
            assertEquals(address, wallet.bech32Address)
        }
    }

    @Test
    fun `PubKeyAsHex returns valid length hex`() {
        val mnemonic = "final random flame cinnamon grunt hazard easily mutual resist pond solution define knife female tongue crime atom jaguar alert library best forum lesson rigid".split(" ")
        val wallet = Wallet.derive(mnemonic, networkInfo)
        assertEquals(66, wallet.pubKeyAsHex.length)
    }

    @Test
    fun `ecPublicKey returns valid length key`() {
        val mnemonic = "final random flame cinnamon grunt hazard easily mutual resist pond solution define knife female tongue crime atom jaguar alert library best forum lesson rigid".split(" ")
        val wallet = Wallet.derive(mnemonic, networkInfo)
        assertEquals(176, Hex.toHexString(wallet.ecPublicKey.encoded).length)
    }

    @Test
    fun `Random wallet is generated properly`() {
        Wallet.random(networkInfo)
    }

    @Test
    fun `Sign data returns non deterministic signatures`() {
        val info = NetworkInfo(bech32Hrp = "did:com:", lcdUrl = "")
        val mnemonic = listOf(
                "will",
                "hard",
                "topic",
                "spray",
                "beyond",
                "ostrich",
                "moral",
                "morning",
                "gas",
                "loyal",
                "couch",
                "horn",
                "boss",
                "across",
                "age",
                "post",
                "october",
                "blur",
                "piece",
                "wheel",
                "film",
                "notable",
                "word",
                "man"
        )
        val wallet = Wallet.derive(mnemonic, info)

        val data = "Test";
        val sig1 = Hex.toHexString(wallet.sign(data.toByteArray()))
        val sig2 = Hex.toHexString(wallet.sign(data.toByteArray()))
        assertNotEquals(sig1, sig2)
    }
}