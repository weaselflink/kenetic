package de.bissell.kenetic

data class KeneticMessage(val type: KeneticMessageType, val payload: ByteArray = ByteArray(0)) {

    val bytes by lazy { generateBytes() }

    private fun generateBytes(): ByteArray {
        val result = mutableListOf<Byte>()
        result.add(type.code)
        result.addAll(payload.toList())
        return result.toByteArray()
    }
}

enum class KeneticMessageType(val code: Byte) {
    MESSAGE(1)
}

fun decodeMessage(packet: ByteArray) =
        KeneticMessage(decodeMessageType(packet.get(0)), packet.copyOfRange(1, packet.size))

fun decodeMessageType(code: Byte) =
        KeneticMessageType.values().filter { it.code == code }.first()
