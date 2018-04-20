package de.bissell.kenetic

import org.assertj.core.api.KotlinAssertions
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.Test
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

class SocketTest {

    @Test
    fun simplePacket() {
        val receiveSocket = DatagramSocket()
        val sendSocket = DatagramSocket()

        val receivePacket = DatagramPacket(ByteArray(64), 0, 64)
        thread {
            receiveSocket.receive(receivePacket)
        }
        sendPacket(sendSocket, "dummy".toByteArray(), InetAddress.getLocalHost(), receiveSocket.localPort)

        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            KotlinAssertions.assertThat(String(receivePacket.data, 0, receivePacket.length)).isEqualTo("dummy")
        }

        receiveSocket.close()
        sendSocket.close()
    }
}