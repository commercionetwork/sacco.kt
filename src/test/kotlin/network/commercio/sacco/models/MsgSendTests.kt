package network.commercio.sacco.models

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.models.messages.MsgSend
import network.commercio.sacco.models.types.StdCoin
import org.junit.Assert.assertEquals
import org.junit.Test

class MsgSendTests {

    @Test
    fun `MsgSend is built correctly`() {
        val message = MsgSend(
            fromAddress = "cosmos1huydeevpz37sd9snkgul6070mstupukw00xkw9",
            toAddress = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
            amount = listOf(StdCoin(denom = "uatom", amount = "100"))
        )

        val json = """
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
        """.trimIndent().replace("\\s".toRegex(), "")

        assertEquals("cosmos-sdk/MsgSend", message.type)
        assertEquals(json, jacksonObjectMapper().writeValueAsString(message))
    }
}