package network.commercio.sacco.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.Wallet
import network.commercio.sacco.models.account.AccountData
import network.commercio.sacco.models.types.StdTx
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Allows to interact with a chain LCD in order to perform common operations such as
 * * account data retrieving
 * * transaction posting
 */
object LCDService {

    private val objectMapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost")
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build()
    private val chainService = retrofit.create(ChainService::class.java)

    /**
     * Allows to read the [AccountData] associated with the given [wallet] instance.
     * @throws Exception if something goes wrong while reading the data.
     */
    suspend fun getAccountData(wallet: Wallet): AccountData {
        // Get the latest block info
        val latestBlockEndpoint = "${wallet.networkInfo.lcdUrl}/blocks/latest"
        val latestBlockResponse = chainService.getLatestBlock(latestBlockEndpoint)
        if (!latestBlockResponse.isSuccessful) {
            throw Exception("Cannot get latest block  - ${latestBlockResponse.errorBody()!!.string()}")
        }
        val blockBody = checkNotNull(latestBlockResponse.body())
        val blockNumber = blockBody.meta.header.height

        // Get the account data
        val accountEndpoint = "${wallet.networkInfo.lcdUrl}/auth/accounts/${wallet.bech32Address}?height=${blockNumber}"
        val accountResponse = chainService.getAccountData(accountEndpoint)
        if (!accountResponse.isSuccessful) {
            throw Exception("Expected status code 200 but got ${accountResponse.code()} - ${accountResponse.errorBody()}")
        }
        val accountBody = checkNotNull(accountResponse.body())
        val accountJson = when (accountBody.containsKey("result")) {
            true -> accountBody["result"] as Map<String, Any>
            false -> accountBody
        }

        val jsonString = objectMapper.writeValueAsString(accountJson["value"] as Map<String, Any>)
        return objectMapper.readValue(jsonString, AccountData::class.java)
    }

    /**
     * Allows to post the given [stdTx] on the chain associated with the given [wallet] instance.
     * @return the hash of the transaction when sent successfully
     * @throws Exception if something goes wrong while sending the transaction
     */
    suspend fun postTx(wallet: Wallet, stdTx: StdTx): String {
        // Build the request body
        val requestBody = mapOf("tx" to stdTx, "mode" to "sync")

        // Send the tx
        val url = "${wallet.networkInfo.lcdUrl}/txs"
        val response = chainService.postTx(url, requestBody)
        if (!response.isSuccessful) {
            throw Exception("Expected status code 200 but got ${response.code()} - ${response.errorBody()!!.string()}")
        }

        // Check the response
        val json = checkNotNull(response.body())
        when {
            json["code"] != 0 -> throw Exception("Error while sending the transaction - $json")
            !json.containsKey("txhash") -> throw Exception("No hash inside response: $json")
            else -> return json["txhash"].toString()
        }
    }
}