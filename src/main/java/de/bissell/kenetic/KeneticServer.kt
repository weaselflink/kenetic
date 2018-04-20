package de.bissell.kenetic

import org.awaitility.Awaitility
import java.net.DatagramSocket
import kotlin.concurrent.thread

class KeneticServer(port: Int? = null) {

    private val socket: DatagramSocket = if (port != null) {
        DatagramSocket(port)
    } else {
        DatagramSocket()
    }
    private var nextMessage: KeneticMessage? = null

    val localPort: Int

    init {
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
