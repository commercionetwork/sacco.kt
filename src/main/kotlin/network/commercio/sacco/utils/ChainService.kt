package network.commercio.sacco.utils

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ChainService {

    @GET
    suspend fun getAccountData(@Url url: String): Response<Map<String, Any>>

    @POST
    suspend fun postTx(@Url url: String, @Body body: String): Response<Map<String, Any>>
}