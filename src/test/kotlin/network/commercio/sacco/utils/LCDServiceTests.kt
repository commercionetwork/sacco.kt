package network.commercio.sacco.utils

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import network.commercio.sacco.NetworkInfo
import network.commercio.sacco.TxResponse
import network.commercio.sacco.Wallet
import network.commercio.sacco.models.types.StdTx
import network.commercio.sacco.readResource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

/**
 * Tests for [LCDService].
 */
class LCDServiceTests {

    private val netInfo = NetworkInfo(lcdUrl = "http://localhost:1317", bech32Hrp = "did:com:")
    private val wallet = Wallet.derive(
        mnemonic = listOf(
            "faculty",
            "enhance",
            "resource",
            "toy",
            "bind",
            "gossip",
            "book",
            "access",
            "able",
            "damage",
            "round",
            "garment",
            "potato",
            "feature",
            "tonight",
            "head",
            "parrot",
            "device",
            "crunch",
            "ketchup",
            "useful",
            "blanket",
            "sauce",
            "pencil"
        ),
        networkInfo = netInfo
    )

    @Test
    fun `postTx returns the proper result with failed posting`() = mockkObject(LCDService) {
        val stdTxString = readResource("StdTx.json")
        val stdTx = LCDService.objectMapper.readValue(stdTxString, StdTx::class.java)

        val responseString = readResource("ErrorTxResponse.json")
        val response = LCDService.objectMapper.readValue(responseString, TxResponseJson::class.java)
        val mockService = mockk<ChainService> {
            coEvery { postTx(any(), any()) } returns Response.success(response)
        }
        LCDService.chainService = mockService

        val result = runBlocking { LCDService.postTx(stdTx = stdTx, wallet = wallet) }
        assertTrue(result is TxResponse.Error)

        result as TxResponse.Error
        assertEquals(6, result.code)
        assertEquals("Unsupported metadata schema: .", result.message)
    }

    @Test
    fun `postTx returns the proper result with failed posting and no logs`() = mockkObject(LCDService) {
        val stdTxString = readResource("StdTx.json")
        val stdTx = LCDService.objectMapper.readValue(stdTxString, StdTx::class.java)

        val responseString = readResource("ErrorRawLogTx.json")
        val response = LCDService.objectMapper.readValue(responseString, TxResponseJson::class.java)
        val mockService = mockk<ChainService> {
            coEvery { postTx(any(), any()) } returns Response.success(response)
        }
        LCDService.chainService = mockService

        val result = runBlocking { LCDService.postTx(stdTx = stdTx, wallet = wallet) }
        assertTrue(result is TxResponse.Error)

        result as TxResponse.Error
        assertEquals(6, result.code)
        assertEquals("Field publicKey must have length of 3", result.message)
    }

    @Test
    fun `postTx returns the proper result with correct posting`() = mockkObject(LCDService) {
        val stdTxString = readResource("StdTx.json")
        val stdTx = LCDService.objectMapper.readValue(stdTxString, StdTx::class.java)

        val responseString = readResource("SuccessTxResponse.json")
        val response = LCDService.objectMapper.readValue(responseString, TxResponseJson::class.java)
        val mockService = mockk<ChainService> {
            coEvery { postTx(any(), any()) } returns Response.success(response)
        }
        LCDService.chainService = mockService

        val result = runBlocking { LCDService.postTx(stdTx = stdTx, wallet = wallet) }
        assertTrue(result is TxResponse.Successful)

        result as TxResponse.Successful
        assertEquals("2CB2471712192815191A679268E4993C67CAD1654FED8F398B698F57EF4A23C1", result.txHash)
    }
}