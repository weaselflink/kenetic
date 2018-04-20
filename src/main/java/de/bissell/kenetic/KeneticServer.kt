package de.bissell.kenetic

import org.awaitility.Awaitility
import java.net.DatagramSocket
import kotlin.concurrent.thread

class KeneticServer(port: Int? = null) {

    private val socket: DatagramSocket
    private var nextMessage: KeneticMessage? = null

    val localPort: Int

    init {
        if (port != null) {
            socket = DatagramSocket(port)
        } else {
            socket = DatagramSocket()
        }
        localPort = socket.localPort

        thread {
            messageWaiter()
        }
    }

    fun receive(): KeneticMessage? {
        val message = nextMessage
        nextMessage = null
        return message
    }

    private fun messageWaiter() {
        while (true) {
            Awaitility.await().until { nextMessage == null }
            val packet = receivePacket(socket)
            nextMessage = KeneticMessage(String(packet))
        }
    }
}
