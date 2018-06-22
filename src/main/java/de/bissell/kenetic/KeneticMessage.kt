package de.bissell.kenetic

data class KeneticMessage(val type: KeneticMessageType, val payload: String? = null) {

    val bytes by lazy { generateBytes() }

    private fun generateBytes(): ByteArray {
        val result = mutableListOf<Byte>()
        result.add(type.code)
        if (payload != null) {
            result.addAll(payload.toByteArray().toList())
        }
        return result.toByteArray()
    }
}

enum class KeneticMessageType(val code: Byte) {
    MESSAGE(1)
}

fun decodeMessage(packet: ByteArray) =
        KeneticMessage(decodeMessageType(packet.get(0)), String(packet, 1, packet.size - 1))

fun decodeMessageType(code: Byte) =
        KeneticMessageType.values().filter { it.code == code }.first()
