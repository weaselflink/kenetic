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
        var data: ByteArray? = null
        thread {
            data = receivePacket(peerMock)
        }
        Awaitility.await().atMost(Duration.FIVE_SECONDS).untilAsserted {
            KotlinAssertions.assertThat(data).isNotNull()
            KotlinAssertions.assertThat(String(data!!)).isEqualTo(expected)
        }
    }
}