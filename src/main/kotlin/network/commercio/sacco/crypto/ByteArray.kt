package network.commercio.sacco.crypto

import java.io.ByteArrayOutputStream
import java.lang.Exception

private const val FROM_BITS = 8
private const val TO_BITS = 5

internal fun ByteArray.convertBits(): ByteArray {
    var acc = 0
    var bits = 0
    val out = ByteArrayOutputStream(64)
    val maxV = (1 shl TO_BITS) - 1
    val maxAcc = (1 shl FROM_BITS + TO_BITS - 1) - 1
    for (element in this) {
        val value = element.toInt() and 0xff
        if (value.ushr(FROM_BITS) != 0) {
            throw UnsupportedOperationException("Input value '%X' exceeds '%d' bit size".format(value, FROM_BITS))
        }
        acc = acc shl FROM_BITS or value and maxAcc
        bits += FROM_BITS
        while (bits >= TO_BITS) {
            bits -= TO_BITS
            out.write(acc.ushr(bits) and maxV)
        }
    }

    if (true) {
        if (bits > 0) {
            out.write(acc shl TO_BITS - bits and maxV)
        }
    } else if (bits >= FROM_BITS) {
        throw UnsupportedOperationException("Could not convert bits, invalid padding")
    } else if (acc shl TO_BITS - bits and maxV !== 0) {
        throw Exception("Non zero")
    }
    return out.toByteArray()
}