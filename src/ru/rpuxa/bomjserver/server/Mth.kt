package ru.rpuxa.bomjserver.server

import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.sqrt

fun Double.sqr() = this * this

tailrec fun binResearch(right: Double, left: Double, function: (Double) -> Double): Double {
    val rightY = function(right)
    val leftY = function(left)
    if (rightY > 0 == leftY > 0) return Double.NaN

    val middle = (right + left) / 2
    if (abs(right - left) < 1e-3) return middle

    return if (function(middle) > 0 != leftY > 0) {
        binResearch(middle, left, function)
    } else {
        binResearch(right, middle, function)
    }
}

fun solvePolynomial2(a: Double, b: Double, c: Double): Pair<Double, Double> {
    val d = sqrt(b.sqr() - 4 * a * c)
    return (-b + d) / (2 * a) to (-b - d) / (2 * a)
}

fun solvePolynomial(vararg coefficients: Double) = solvePolynomial(coefficients.toList())

fun solvePolynomial(coefficients: List<Double>): List<Double> {
    if (coefficients.size == 3) {
        return solvePolynomial2(coefficients[0], coefficients[1], coefficients[2])
                .toList()
                .filter { it == it }
    }

    val count = coefficients.size
    val e = (solvePolynomial(
            coefficients
                    .subList(0, coefficients.lastIndex)
                    .mapIndexed { n, a -> a * (count - n - 1) }
    ) as MutableList<Double>).apply {
        add(0, -1e4)
        add(1e4)
    }
    val solves = ArrayList<Double>()
    for (i in 0..e.size - 2) {
        val solve = binResearch(e[i], e[i + 1]) { x ->
            var sum = 0.0
            for (c in 0 until count) {
                sum += pow(x, (count - c - 1).toDouble()) * coefficients[c]
            }
            sum
        }

        if (solve == solve)
            solves.add(solve)
    }

    return solves
}
