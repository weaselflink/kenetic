package de.bissell.kenetic

import org.assertj.core.api.KotlinAssertions
import org.awaitility.Awaitility
import org.awaitility.Duration
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.DatagramSocket
import kotlin.concurrent.thread

class KeneticClientTest {

    private lateinit var peerMock: DatagramSocket
    private lateinit var objectUnderTest: KeneticClient

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
        val payload = "bar".toByteArray()
        objectUnderTest.send(KeneticMessage(KeneticMessageType.MESSAGE, payload))

        assertReceiveWithMock(payload)
    }

    private fun assertReceiveWithMock(expected: ByteArray) {
        var data: ByteArray? = null
        thread {
            data = receivePacket(peerMock)
        }
        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            KotlinAssertions.assertThat(data).isNotNull()
            KotlinAssertions.assertThat(decodeMessage(data!!).type).isEqualTo(KeneticMessageType.MESSAGE)
            KotlinAssertions.assertThat(decodeMessage(data!!).payload).isEqualTo(expected)
        }
    }
}