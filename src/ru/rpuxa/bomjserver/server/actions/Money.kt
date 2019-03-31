package ru.rpuxa.bomjserver.server.actions

class Money(val value: Int, val currency: Int) {

    fun toCU() = value * getCU(currency)

    companion object {
        fun getCU(currency: Int) = when (currency) {
            RUB -> 2
            BOTTLES -> 3
            else -> throw Exception()
        }
    }
}
