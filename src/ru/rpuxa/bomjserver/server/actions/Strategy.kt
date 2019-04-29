package ru.rpuxa.bomjserver.server.actions

import ru.rpuxa.bomjserver.server.sqr

object Strategy {


    /**
     * [a] - денег на локацию
     * [l] - сколько дней нужно задержаться на локации
     * [sigma] - сколько дней нужно потратить на восстановление 1 здоровья, энергии и на чтобы из потратить в работе
     * [alpha] - цена 1 здоровья
     * [gamma] - цена 1 энергии
     * @return сколько денег можно получить на 2 здоровья и 2 энергии
     */
    fun calculateCoefficients(a: Int, l: Int, sigma: Double, alpha: Double, gamma: Double): Double {
        return a.toDouble() * sigma / l + 2 * alpha + 2 * gamma
    }
}