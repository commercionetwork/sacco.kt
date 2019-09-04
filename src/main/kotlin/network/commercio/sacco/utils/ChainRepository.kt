package network.commercio.sacco.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.Wallet
import network.commercio.sacco.models.account.AccountData
import network.commercio.sacco.models.types.StdTx
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ChainRepository {

    private val retrofit = Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create()).build()
    private val chainService = retrofit.create(ChainService::class.java)

    suspend fun getAccountData(wallet: Wallet): AccountData {
        // Get the endpoint
        val endpoint = "${wallet.networkInfo.lcdUrl}/auth/accounts/${wallet.bech32Address}"

        // Get server response
        val response = chainService.getAccountData(endpoint)
        if (!response.isSuccessful) {
            throw Exception("Expected status code 200 but got ${response.code()} - ${response.errorBody()}")
        }

        val data = when (response.body()!!.containsKey("result")) {
            true -> response.body()!!["result"]
            false -> response.body()
        }

        return jacksonObjectMapper().readValue(jacksonObjectMapper().writeValueAsString(data), AccountData::class.java)
    }

    suspend fun postTx(wallet: Wallet, stdTx: StdTx): String {
        // Get the endpoint
        val url = "${wallet.networkInfo.lcdUrl}/txs"

        // Build the request body
        val requestBody = mapOf("tx" to stdTx, "mode" to "sync")
        val requestBodyJson = jacksonObjectMapper().writeValueAsString(requestBody)

        // Get the response
        val response = chainService.postTx(url, requestBodyJson)
        if (!response.isSuccessful) {
            throw Exception("Expected status code 200 but got ${response.code()} - ${response.errorBody()}")
        }

        // Get the Tx hash
        return response.body()?.get("txhash")?.toString()
            ?: throw Exception("No hash inside response: ${response.body()}")
    }
}