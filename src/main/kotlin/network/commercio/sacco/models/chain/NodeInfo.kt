package network.commercio.sacco.models.chain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the JSON object that is returned from the server when asked for a node information.
 */
internal data class NodeInfo(@JsonProperty("node_info") val info: Info) {
    data class Info(@JsonProperty("network") val chainId: String)
}