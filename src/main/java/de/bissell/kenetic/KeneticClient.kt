package de.bissell.kenetic

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class KeneticClient(private val address: KeneticAddress) {

    private val socket: DatagramSocket

    init {
        socket = DatagramSocket()
    }

    fun send(message: KeneticMessage) {
        socket.send(createDatagramPacket(message))
    }

    private fun createDatagramPacket(message: KeneticMessage): DatagramPacket {
        val bytes = message.payload.toByteArray()
        return DatagramPacket(bytes, 0, bytes.size, InetAddress.getByName(address.host), address.port)
    }
}
