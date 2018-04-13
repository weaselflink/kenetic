package de.bissell.kenetic

import org.assertj.core.api.KotlinAssertions
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.concurrent.thread

class KeneticClientTest {

    lateinit var peerMock: DatagramSocket
    lateinit var objectUnderTest: KeneticClient

    @Before
    fun startPeerMock() {
        peerMock = DatagramSocket()
        objectUnderTest = KeneticClient(KeneticAddress("localhost", peerMock.localPort))
    }

    @After
    fun stopPeerMock() {
        peerMock.close()
    }

    @Test
    fun sendsMessage() {
        objectUnderTest.send(KeneticMessage("bar"))

        assertReceiveWithMock("bar")
    }

    fun assertReceiveWithMock(expected: String) {
        val packet = DatagramPacket(ByteArray(64), 64)
        thread {
            peerMock.receive(packet)
        }
        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            KotlinAssertions.assertThat(String(packet.data, 0, packet.length)).isEqualTo(expected)
        }
    }
}