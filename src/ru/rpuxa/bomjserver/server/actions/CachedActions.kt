package ru.rpuxa.bomjserver.server.actions

import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.CachedChainElement
import ru.rpuxa.bomjserver.CachedCourse
import java.io.*
import java.util.*
import kotlin.math.round

class CachedActions(params: Array<LocationParams>) {
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

       DataOutputStream(FileOutputStream(ACTIONS_NAME)).use { stream ->
            stream.writeShort(actionsList.size)
            actionsList.forEach { action ->
                stream.writeShort(action.id.toInt())
                stream.writeByte(action.level.toInt())
                stream.writeByte(action.menu.toInt())
                stream.writeUTF(action.name)
                stream.writeInt(action.cost)
                stream.writeByte(action.energy.toInt())
                stream.writeByte(action.food.toInt())
                stream.writeByte(action.health.toInt())
                stream.writeBoolean(action.isIllegal)
            }
            arrayOf(Actions.transports, Actions.homes, Actions.friends, Actions.locations).forEach { chainElements ->
                stream.writeByte(chainElements.size)
                chainElements.forEachIndexed { i, e ->
                    stream.writeUTF(e.name)
                    if (chainElements === Actions.locations) {
                        stream.writeByte(i)
                        stream.writeByte(i)
                        stream.writeByte(i)
                        stream.writeByte(0)
                    } else {
                        stream.writeByte(i - 1)
                        stream.writeByte(i - 1)
                        stream.writeByte(i - 1)
                        stream.writeByte(i - 1)
                    }
                    stream.writeByte(e.course)
                    stream.writeInt(e.cost.value)
                }
            }

            stream.writeByte(Actions.courses.size)
            Actions.courses.forEach {
                stream.writeByte(it.id)
                stream.writeUTF(it.name)
                stream.writeInt(it.cost.value)
                stream.writeShort(it.length)
            }
        }
    }

    private fun Double.convertTo(currency: Int) = this / Money.getCU(currency)

    companion object {
        const val ACTIONS_NAME = "new_actions.bomj"
    }
}