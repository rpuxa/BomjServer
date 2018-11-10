package ru.rpuxa.statisticreader

import ru.rpuxa.bomjserver.Review
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.lang.Math.round

const val FILE = "C:\\Users\\Григорий\\statistic.bomj"

val reader = BufferedReader(InputStreamReader(System.`in`))

lateinit var stat: Map<Long, Map<Int, Any>>
lateinit var reviews: ArrayList<Review>

@Suppress("UNCHECKED_CAST")
fun main(args: Array<String>) {
    ObjectInputStream(FileInputStream(FILE)).use {
        stat = it.readObject() as Map<Long, Map<Int, Any>>
        reviews = it.readObject() as ArrayList<Review>
    }
    val list = stat.map { it.value }.sortedBy { it[AGE] as Int }
    println("Загружено!")
    while (true) {
        print("> ")
        val cmd = reader.readLine()

        when (cmd) {
            "reviews" -> {
                reviews.forEach {
                    println("${round(it.rating)} - ${it.rev}")
                    println("============================================================")
                }
            }

            "days" -> {
                for (v in list) {
                    println(v[AGE])
                }
            }
        }
    }
}

const val ID = 0
const val ACTIONS = 1
const val AGE = 2
const val LOCATION = 3
const val FRIEND = 4
const val TRANSPORT = 5
const val HOME = 6
const val COURSES = 7
const val VIPS = 8
const val VERSION_CODE = 9