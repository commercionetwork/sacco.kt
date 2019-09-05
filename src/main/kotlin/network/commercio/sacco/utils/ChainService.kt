package network.commercio.sacco.utils

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

@JvmSuppressWildcards
interface ChainService {

    @GET
    suspend fun getAccountData(@Url url: String): Response<Map<String, Any>>

    @GET
    suspend fun getLatestBlock(@Url url: String): Response<BlockResponse>

    @POST
    suspend fun postTx(@Url url: String, @Body body: Map<String, Any>): Response<Map<String, Any>>
}