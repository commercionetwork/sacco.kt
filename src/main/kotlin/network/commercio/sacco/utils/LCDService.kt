package network.commercio.sacco.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.TxResponse
import network.commercio.sacco.Wallet
import network.commercio.sacco.models.account.AccountData
import network.commercio.sacco.models.chain.NodeInfo
import network.commercio.sacco.models.types.StdTx
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException


/**
 * Allows to interact with a chain LCD in order to perform common operations such as
 * * account data retrieving
 * * transaction posting
 */
internal object LCDService {

    /**
     * Internal for testing
     */
    internal val objectMapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setSerializationInclusion(JsonInclude.Include.ALWAYS)
    }

    private val retrofit: Retrofit
        get() {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val interceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                        .addHeader("Cache-Control", "no-cache")
                        .build()
                    return chain.proceed(request)
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("http://localhost")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build()
        }

    /**
     * Internal and var for testing
     */
    internal var chainService = retrofit.create(ChainService::class.java)

    /**
     * Allows to read the [AccountData] associated with the given [wallet] instance.
     * @throws Exception if something goes wrong while reading the data.
     */
    suspend fun getAccountData(wallet: Wallet): AccountData {
        // Get the account data
        val accountEndpoint = "${wallet.networkInfo.lcdUrl}/auth/accounts/${wallet.bech32Address}"
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
     * Allows the [NodeInfo] from the blockchain endpoint.
     */
    suspend fun getNodeInfo(wallet: Wallet): NodeInfo {
        val nodeInfoEndpoint = "${wallet.networkInfo.lcdUrl}/node_info"
        val response = chainService.getNodeInfo(nodeInfoEndpoint)
        if (!response.isSuccessful) {
            throw Exception("Cannot get node info - ${response.errorBody()!!.string()}")
        }
        return checkNotNull(response.body())
    }

    /**
     * Allows to post the given [stdTx] on the chain associated with the given [wallet] instance.
     * @return the hash of the transaction when sent successfully
     * @throws Exception if something goes wrong while sending the transaction
     */
    suspend fun postTx(stdTx: StdTx, wallet: Wallet, mode: String): TxResponse {
        // Build the request body
        val requestBody = mapOf("tx" to stdTx, "mode" to mode)

        // Send the tx
        val url = "${wallet.networkInfo.lcdUrl}/txs"
        val response = chainService.postTx(url, requestBody)
        if (!response.isSuccessful) {
            throw Exception("Expected status code 200 but got ${response.code()} - ${response.errorBody()!!.string()}")
        }

        // Check the response
        val tx = checkNotNull(response.body())
        return checkLogs(tx)
    }

    /**
     * Checks the value of the logs that are returned from the posting of a transaction
     * and converts the response to the appropriate [TxResponse] instance.
     */
    private fun checkLogs(tx: TxResponseJson): TxResponse {
        val logs = tx.logs

        return when {
            logs == null -> checkRawLog(tx.rawLog)
            logs.all { it.success } -> TxResponse.Successful(txHash = tx.hash)
            else -> {
                val failedLog = tx.logs.first { !it.success }
                val result = objectMapper.readValue(failedLog.log, MsgResultLog::class.java)
                TxResponse.Error(code = result.code, message = result.message)
            }
        }
    }

    /**
     * Checks the raw log contained inside the response that is returned from the server
     * upon posting a transaction that has failed.
     */
    private fun checkRawLog(rawLog: String): TxResponse.Error {
        val result = objectMapper.readValue(rawLog, MsgResultLog::class.java)
        return TxResponse.Error(code = result.code, message = result.message)
    }
}
