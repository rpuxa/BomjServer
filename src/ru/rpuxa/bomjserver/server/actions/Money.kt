package ru.rpuxa.bomjserver.server.actions

class Money(val value: Int, val currency: Int) {

    fun toCU() = value * getCU(currency)

    companion object {
        fun getCU(currency: Int) = when (currency) {
            RUB -> 1
            BOTTLES -> 2
            else -> throw Exception()
        }
    }
}
