package de.bissell.kenetic

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun sendPacket(socket: DatagramSocket, payload: ByteArray, address: InetAddress, port: Int) {
    val packet = DatagramPacket(payload, 0, payload.size, address, port)
    socket.send(packet)
}

fun receivePacket(socket: DatagramSocket, maxSize: Int = 64 * 1024): ByteArray {
    val packet = DatagramPacket(ByteArray(maxSize), 0, maxSize)
    socket.receive(packet)
    return packet.data.copyOf(packet.length)
}