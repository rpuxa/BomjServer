package ru.rpuxa.bomjserver.server.actions

class Money(val value: Int, val currency: Int) {

    fun toCU() = value * getCU(currency)

    companion object {
        fun getCU(currency: Int) = when (currency) {
            RUB -> 2
            BOTTLES -> 3
            EURO -> 140
            BITCOIN -> 4000
            else -> throw Exception()
        }
    }
}
