package network.commercio.sacco

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.models.types.StdMsg
import org.junit.Assert.assertEquals
import org.junit.Test

class TxBuilderTest {

    @Test
    fun `StdTx is built correctly`() {
        val message = StdMsg(
            type = "cosmos-sdk/MsgSend",
            value = mapOf(
                "from_address" to "cosmos1huydeevpz37sd9snkgul6070mstupukw00xkw9",
                "to_address" to "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
                "amount" to listOf(
                    mapOf("denom" to "uatom", "amount" to "100")
                )
            )
        )

        val txString = """
        {
            "msg": [
              {
                "type": "cosmos-sdk/MsgSend",
                "value": {
                  "from_address": "cosmos1huydeevpz37sd9snkgul6070mstupukw00xkw9",
                  "to_address": "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
                  "amount": [
                    {
                      "denom": "uatom",
                      "amount": "100"
                    }
                  ]
                }
              }
            ],
            "fee": {
              "amount": [],
              "gas": "200000"
            },
            "signatures": null,
            "memo": ""
        }
        """.trimIndent().replace("\\s".toRegex(), "")

        val stdTx = TxBuilder.buildStdTx(stdMsgs = listOf(message))
        assertEquals(txString, jacksonObjectMapper().writeValueAsString(stdTx))
    }

}