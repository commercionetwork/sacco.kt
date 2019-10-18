package network.commercio.sacco.utils

import network.commercio.sacco.models.chain.NodeInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

@JvmSuppressWildcards
internal interface ChainService {

    @GET
    suspend fun getNodeInfo(@Url url: String): Response<NodeInfo>

    @GET
    suspend fun getAccountData(@Url url: String): Response<Map<String, Any>>

    @GET
    suspend fun getLatestBlock(@Url url: String): Response<BlockResponse>

    @POST
    suspend fun postTx(@Url url: String, @Body body: Map<String, Any>): Response<TxResponseJson>
}