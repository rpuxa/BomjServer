package ru.rpuxa.bomjserver.server.actions

import ru.rpuxa.bomjserver.server.sqr

object Strategy {

    /**
     * first - коэффициент для работы (a)
     * second - коэффициент для бодрости (b)
     * third - коэффициент для здоровья\еды (c)
     * x - количество дней на восстановление 2 здоровья и 2 бодрости и переработки этих показателей в деньги
     */
    fun calculateCoefficients(days: Int, all: Int, x: Double, a: Double, b: Double, c: Double): Triple<Double, Double, Double> {
        val l = all.toDouble() * x / days
        val p = 559_504.0
        val n = 1135.0 / p
        val m = 1509.0 / p
        val i = arrayOf(
                m * n,
                m.sqr() + 1,
                l * m - a * m - b,
                n.sqr() + 1,
                m * n,
                l * n - a * n - c
        )
        val beta = (i[2] * i[3] - i[5] * i[0]) / (i[0] * i[4] - i[1] * i[3])
        val gamma = -(i[2] + i[1] * beta) / i[0]
        val alpha = l + n * gamma + m * beta

        return Triple(alpha, beta, gamma)
    }
}