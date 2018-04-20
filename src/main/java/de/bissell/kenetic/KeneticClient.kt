package de.bissell.kenetic

import java.net.DatagramSocket
import java.net.InetAddress

class KeneticClient(private val address: KeneticAddress) {

    private val socket: DatagramSocket

    init {
        socket = DatagramSocket()
    }

    fun send(message: KeneticMessage) {
        sendPacket(socket, message.payload.toByteArray(), InetAddress.getByName(address.host), address.port)
    }
}
