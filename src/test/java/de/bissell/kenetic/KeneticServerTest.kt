package de.bissell.kenetic

import org.assertj.core.api.KotlinAssertions
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.DatagramSocket
import java.net.InetAddress

class KeneticServerTest {

    private lateinit var peerMock: DatagramSocket
    private lateinit var objectUnderTest: KeneticServer

    @Before
    fun startPeerMock() {
        peerMock = DatagramSocket()
        objectUnderTest = KeneticServer()
    }

    @After
    fun stopPeerMock() {
        peerMock.close()
    }

    @Test
    fun sendsMessage() {
        val payload = "foo".toByteArray()
        val packet = KeneticMessage(KeneticMessageType.MESSAGE, payload).bytes
        sendPacket(peerMock, packet, InetAddress.getLocalHost(), objectUnderTest.localPort)

        assertServerReceived(payload)
    }

    private fun assertServerReceived(expected: ByteArray) {
        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            val message = objectUnderTest.receive()
            KotlinAssertions.assertThat(message).isNotNull()
            KotlinAssertions.assertThat(message!!.payload).isEqualTo(expected)
        }
    }
}