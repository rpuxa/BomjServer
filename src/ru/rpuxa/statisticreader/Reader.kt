package ru.rpuxa.statisticreader

import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.Review
import ru.rpuxa.bomjserver.server.Actions
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.ObjectInputStream
import java.lang.Math.round

const val FILE = "stat\\statistic.bomj"

val reader = BufferedReader(InputStreamReader(System.`in`))

lateinit var stat: Collection<Map<Int, Any>>
lateinit var reviews: ArrayList<Review>

@Suppress("UNCHECKED_CAST")
fun main(_u: Array<String>) {
    ObjectInputStream(FileInputStream(FILE)).use {
        stat = (it.readObject() as Map<Long, Map<Int, Any>>).values
        reviews = it.readObject() as ArrayList<Review>
    }
    val list = stat.sortedBy { it[AGE] as Int }
    stat = stat.filter { it[AGE] as Int > 30 }
    println("Загружено!")
    while (true) {
        print("> ")
        val line = reader.readLine().split(' ')
        val args = if (line.size > 1) line.subList(1, line.size) else emptyList()

        when (line[0]) {
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

            "percent" -> {
                val level = args[0].toInt()
                val menu = args[1].toInt()
                val actions = Actions.getActions(level, menu)
                val actionsCount = HashMap<Int, Int>()
                var all = 0L
                for (s in stat) {
                    val statActions = s[ACTIONS] as IntArray
                    for (action in actions) {
                        val id = action.id.toInt()
                        val c = actionsCount[id]
                        if (c == null) {
                            actionsCount[id] = 0
                        }
                        actionsCount[id] = actionsCount[id]!! + statActions[id]
                        all += statActions[id]
                    }
                }
                for (action in actions) {
                    println("${action.name} = ${actionsCount[action.id.toInt()]!!.toFloat() * 100 / all}%")
                }
            }

            "vip" -> {
                val vip = IntArray(4)
                for (s in stat) {
                    val v = s[VIPS] as IntArray
                    v.forEachIndexed { index, i -> vip[index] += i }
                }
                val all = vip.sum()
                println("all = $all")
                vip.forEachIndexed { index, i -> println("$index = ${i.toFloat() * 100 / all}%") }
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