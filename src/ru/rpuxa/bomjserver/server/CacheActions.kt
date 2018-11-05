package ru.rpuxa.bomjserver.server

import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.lang.IllegalStateException

object CacheActions {
    operator fun get(type: Int) = with(Actions) {
        when (type) {
            ACTIONS -> actions
            LOCATIONS -> locations
            FRIENDS -> friends
            TRANSPORTS -> transports
            HOMES -> homes
            COURSES -> courses
            HASH -> hash
            else -> throw IllegalStateException()
        }
    }

    const val ACTIONS = 0
    const val LOCATIONS = 1
    const val FRIENDS = 2
    const val TRANSPORTS = 3
    const val HOMES = 4
    const val COURSES = 5
    const val HASH = 6


    private const val ACTIONS_FILE = "actions.bomj"

  /*  @Suppress("UNCHECKED_CAST")
    val cached = loadFromFile() as Array<Any>

    private fun cache() = with(Actions) { arrayOf(actions, locations, friends, transports, homes, courses, hash) }

    private fun loadFromFile() =
            try {
                ObjectInputStream(FileInputStream(ACTIONS_FILE)).use {
                    it.readObject()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                cache()
            }*/

    fun saveToFile() {
        try {
            ObjectOutputStream(FileOutputStream(ACTIONS_FILE)).use {
                val array = with(Actions) { arrayOf(actions, locations, friends, transports, homes, courses, hash) }
                for (a in array)
                    it.writeObject(a)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
