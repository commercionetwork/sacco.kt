package network.commercio.sacco.crypto

import java.io.ByteArrayOutputStream

private const val BIP44_PATH = "m/44'/118'/0'/0/0"
private const val FROM_BITS = 8
private const val TO_BITS = 5

fun ByteArray.convertBits(): ByteArray {
    var acc = 0
    var bits = 0
    val out = ByteArrayOutputStream(64)
    val maxv = (1 shl TO_BITS) - 1
    val max_acc = (1 shl FROM_BITS + TO_BITS - 1) - 1
    for (i in 0 until this.size) {
        val value = this[i].toInt() and 0xff
        if (value.ushr(FROM_BITS) != 0) {
            throw UnsupportedOperationException("Input value '%X' exceeds '%d' bit size".format(value, FROM_BITS))
        }
        acc = acc shl FROM_BITS or value and max_acc
        bits += FROM_BITS
        while (bits >= TO_BITS) {
            bits -= TO_BITS
            out.write(acc.ushr(bits) and maxv)
        }
    }
    if (bits >= FROM_BITS || acc shl TO_BITS - bits and maxv != 0) {
        throw UnsupportedOperationException("Could not convert bits, invalid padding")
    }
    return out.toByteArray()

}