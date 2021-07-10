package network.commercio.sacco.models

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import network.commercio.sacco.models.messages.MsgMultiSend
import network.commercio.sacco.models.types.Input
import network.commercio.sacco.models.types.Output
import network.commercio.sacco.models.types.StdCoin
import network.commercio.sacco.readResource
import org.junit.Assert.assertEquals
import org.junit.Test

class MsgMultiSendTests {

    @Test
    fun `MsgMultiSend is serialized correctly`() {
        val message = MsgMultiSend(
          inputs = listOf(
            Input(
              address = "cosmos1huydeevpz37sd9snkgul6070mstupukw00xkw9",
              coins = listOf(StdCoin(denom = "uatom", amount = "100"))
            )
          ),
          outputs = listOf(
            Output(
              address = "cosmos12lla7fg3hjd2zj6uvf4pqj7atx273klc487c5k",
              coins = listOf(StdCoin(denom = "uatom", amount = "100"))
            )
          )
        )

        val json = readResource("MsgMultiSend.json")
        assertEquals("cosmos-sdk/MsgMultiSend", message.type)
        assertEquals(json, jacksonObjectMapper().writeValueAsString(message))
    }
}
