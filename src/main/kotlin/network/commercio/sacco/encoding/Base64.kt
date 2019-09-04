package network.commercio.sacco.encoding

import kotlinx.io.charsets.Charsets
import kotlinx.io.core.String
import kotlinx.io.core.toByteArray
import java.io.ByteArrayOutputStream

/**
 * Encodes [this] string value into its Base64 representation.
 */
fun String.toBase64(): String {
    return this.toByteArray(Charsets.UTF_8).toBase64()
}

/**
 * Converts [this] [ByteArray] value into its Base64 string representation.
 */
fun ByteArray.toBase64(): String {
    val table = (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange('0', '9') + '+' + '/').toCharArray()
    val output = ByteArrayOutputStream()
    var padding = 0
    var position = 0
    while (position < this.size) {
        var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
        if (position + 1 < this.size) b = b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
        if (position + 2 < this.size) b = b or (this[position + 2].toInt() and 0xFF) else padding++
        for (i in 0 until 4 - padding) {
            val c = b and 0xFC0000 shr 18
            output.write(table[c].toInt())
            b = b shl 6
        }
        position += 3
    }
    for (i in 0 until padding) {
        output.write('='.toInt())
    }
    return String(output.toByteArray())
}