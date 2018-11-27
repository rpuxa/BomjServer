package ru.rpuxa.bomjserver.server.actions

import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.CachedChainElement
import ru.rpuxa.bomjserver.CachedCourse
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.math.round

class CachedActions(params: Array<LocationParams>) {
    val actions: Array<CachedAction>
    val locations: Array<CachedChainElement>
    val friends: Array<CachedChainElement>
    val transports: Array<CachedChainElement>
    val homes: Array<CachedChainElement>
    val courses: Array<CachedCourse>

    private val actionsList = ArrayList<CachedAction>()

    init {
        for (param in params) {
            val needed = param.level + 1
            val elements = arrayOf(
                    Actions.transports.getOrElse(needed) { Actions.transports.last() },
                    Actions.friends.getOrElse(needed) { Actions.friends.last() },
                    Actions.homes.getOrElse(needed) { Actions.homes.last() }
            )
            var all = 0
            for (element in elements) {
                all += Actions.getCourse(element.course)?.cost?.toCU() ?: 0
                all += element.cost.toCU()
            }

            var (jobC, energyC, healthC) = Strategy.calculateCoefficients(param.days, all, 2.2, param.jobCost, param.energyCost, param.healthCost)
            jobC /= 30
            energyC /= 30
            healthC /= 30
            // работа
            var illegalIndex = 0
            var legalIndex = 0
            Actions.getActions(param.level, JOBS).forEach { action ->

                val energyNeeded = if (action.illegal) {
                    when (illegalIndex++) {
                        0 -> 30
                        1 -> 40
                        else -> throw IllegalStateException()
                    }
                } else {
                    when (legalIndex++) {
                        0 -> 16
                        1 -> 30
                        2 -> 40
                        3 -> 50
                        else -> throw IllegalStateException()
                    }
                }

                val healthNeeded = energyNeeded / 2

                val salary = (round((
                        energyNeeded * jobC).convertTo(action.currency) *
                        if (action.illegal) 2 else 1
                )).toInt()

                actionsList.add(CachedAction(
                        action.id.toShort(), param.level.toByte(), JOBS.toByte(), action.name,
                        -salary, action.currency.toByte(), (-energyNeeded).toShort(),
                        (-healthNeeded).toShort(), (-healthNeeded).toShort(), action.illegal
                ))
            }

            // бодрость
            legalIndex = 0
            illegalIndex = 0
            Actions.getActions(param.level, ENERGY).forEach { action ->
                var energyAdded = when (if (action.illegal) illegalIndex++ else legalIndex++) {
                    0 -> 30
                    1 -> 50
                    2 -> 80
                    else -> throw IllegalStateException()
                } * if (action.illegal) 2 else 1
                var cost = round((energyAdded * energyC).convertTo(action.currency)).toInt()
                var healthAdd = -5

                if (action.illegal) {
                    cost = 0
                } else if (action.free) {
                    energyAdded = 15
                    cost = 0
                    healthAdd = -7
                    legalIndex--
                }

                actionsList.add(CachedAction(
                        action.id.toShort(), param.level.toByte(), ENERGY.toByte(), action.name,
                        cost, action.currency.toByte(), energyAdded.toShort(),
                        healthAdd.toShort(), healthAdd.toShort(), action.illegal
                ))
            }

            // здоровье и еда
            for (menu in arrayOf(HEALTH, FOOD)) {
                legalIndex = 0
                illegalIndex = 0
                Actions.getActions(param.level, menu).forEach { action ->
                    var add = when (if (action.illegal) illegalIndex++ else legalIndex++) {
                        0 -> 30
                        1 -> 50
                        2 -> 80
                        else -> throw IllegalStateException()
                    } * if (action.illegal) 2 else 1

                    var cost = round((add * healthC).convertTo(action.currency)).toInt()
                    var remove = -5

                    if (action.illegal) {
                        cost = 0
                    } else if (action.free) {
                        add = 15
                        cost = 0
                        remove = -7
                        legalIndex--
                    }

                    actionsList.add(CachedAction(
                            action.id.toShort(), param.level.toByte(), menu.toByte(), action.name,
                            cost, action.currency.toByte(), remove.toShort(),
                            (if (menu == FOOD) add else remove).toShort(), (if (menu == HEALTH) add else remove).toShort(),
                            action.illegal
                    ))
                }
            }
        }

        actions = actionsList.toTypedArray()

        locations = Actions.locations.mapIndexed { index, element ->
            CachedChainElement(
                    element.name, (index - 1).toByte(), (index - 1).toByte(),
                    index.toByte(), index.toByte(), element.course.toByte(),
                    element.cost.value, element.cost.currency.toByte()
            )
        }.toTypedArray()

        friends = Actions.friends.mapIndexed { index, element ->
            CachedChainElement(
                    element.name, index.toByte(), (index - 1).toByte(),
                    index.toByte(), index.toByte(), element.course.toByte(),
                    element.cost.value, element.cost.currency.toByte()
            )
        }.toTypedArray()

        transports = Actions.transports.transportsOrHomesToCached()
        homes = Actions.homes.transportsOrHomesToCached()

        courses = Actions.courses.map {
            CachedCourse(
                    it.id.toByte(), it.name, it.cost.value,
                    it.cost.currency.toByte(), it.length.toShort()
            )
        }.toTypedArray()
    }

    private fun ArrayList<ChainElement>.transportsOrHomesToCached() = mapIndexed { index, element ->
        CachedChainElement(
                element.name, (index - 1).toByte(), (index - 1).toByte(),
                (index - 1).toByte(), (index - 1).toByte(), element.course.toByte(),
                element.cost.value, element.cost.currency.toByte()
        )
    }.toTypedArray()

    private fun Double.convertTo(currency: Int) = this / Money.getCU(currency)

    fun saveToFile() {
        try {
            ObjectOutputStream(FileOutputStream("actions.bomj")).use {
                val array = arrayOf(actions, locations, friends, transports, homes, courses)
                for (a in array)
                    it.writeObject(a)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}