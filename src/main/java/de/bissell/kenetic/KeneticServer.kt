package de.bissell.kenetic

class KeneticServer(private val address: KeneticAddress) {

    fun receive(): KeneticMessage {
        return KeneticMessage("foo")
    }
}
