package network.commercio.sacco.crypto

import java.util.*

fun Map<*, *>.sort(): Map<*, *> {

    val sortedValues = entries.map { (key, value) ->

        val sortedValue = when (value) {
            is Map<*, *> -> value.sort()
            else -> value
        }

        Pair(key, sortedValue)
    }.toMap()

    return TreeMap(sortedValues)
}