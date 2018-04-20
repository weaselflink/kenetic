package de.bissell.kenetic

import java.net.InetAddress

data class KeneticAddress(
        val host: String,
        val port: Int
) {
    val inetAddress: InetAddress = InetAddress.getByName(host)
}