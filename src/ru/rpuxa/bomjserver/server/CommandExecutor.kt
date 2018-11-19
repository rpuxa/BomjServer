package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.Review
import ru.rpuxa.bomjserver.ServerCommand
import ru.rpuxa.bomjserver.server.actions.CachedActions
import ru.rpuxa.bomjserver.server.actions.LocationParams
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

internal object CommandExecutor {

    const val EMPTY_MESSAGE = -1
    const val STATISTIC = 0
    const val REVIEW = 1
    const val NEWS_COUNT = 2
    const val GET_NEWS = 3
    const val GET_CACHED_ACTIONS = 4

    private var count = 0

    val params = arrayOf(
            LocationParams(0, 100, 50.0 / 30, 40.0 / 30, 50.0 / 30),
            LocationParams(1, 150, 120.0 / 30, 100.0 / 30, 120.0 / 30),
            LocationParams(2, 200, 200.0 / 30, 200.0 / 30, 180.0 / 30),
            LocationParams(3, 250, 400.0 / 30, 300.0 / 30, 350.0 / 30),
            LocationParams(4, 250, 700.0 / 30, 600.0 / 30, 700.0 / 30),
            LocationParams(5, 300, 1500.0 / 30, 1500.0 / 30, 1300.0 / 30),
            LocationParams(6, 300, 2500.0 / 30, 2500.0 / 30, 2000.0 / 30),
            LocationParams(7, 300, 4500.0 / 30, 4500.0 / 30, 4000.0 / 30)
    )
    val cached = CachedActions(params)

    fun connect(socket: Socket) = thread {
        val startTime = System.currentTimeMillis()
        socket.use { socket ->
            val ois = ObjectInputStream(socket.getInputStream())
            val oos = ObjectOutputStream(socket.getOutputStream())
            println("connected " + ++count + " " + socket.inetAddress.toString())

            fun closeAll() {
                try {
                    ois.close()
                } catch (e: Exception) {

                }
                try {
                    oos.close()
                } catch (e: Exception) {

                }
                try {
                    socket.close()
                } catch (e: Exception) {

                }
                try {
                    socket.shutdownInput()
                } catch (e: Exception) {

                }
                try {
                    socket.shutdownOutput()
                } catch (e: Exception) {
                }
            }

            val thread = thread {
                try {
                    Thread.sleep(30_000)
                } catch (e: InterruptedException) {
                }
                closeAll()
            }
            try {
                while (true) {
                    val cmd = ois.readObject() as ServerCommand
                    val ans = execute(cmd)
                    oos.writeObject(ans ?: ServerCommand(EMPTY_MESSAGE, null))
                }
            } catch (e: IOException) {
                closeAll()
            }
            thread.interrupt()
            println("disconnect ${System.currentTimeMillis() - startTime}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun execute(command: ServerCommand): ServerCommand? {
        when (command.id) {
            STATISTIC -> DataBase.saveStatistic(command.data as Map<Int, Any>)
            REVIEW -> DataBase.saveReview(command.data as Review)
            GET_NEWS -> return ServerCommand(GET_NEWS, DataBase.news[command.data as Int])
            NEWS_COUNT -> return ServerCommand(NEWS_COUNT, DataBase.news.size)
            GET_CACHED_ACTIONS -> return ServerCommand(GET_CACHED_ACTIONS, getCached(command.data as Int))
        }
        return null
    }

    private fun getCached(type: Int) = with(cached) {
        when (type) {
            ACTIONS -> actions
            LOCATIONS -> locations
            FRIENDS -> friends
            TRANSPORTS -> transports
            HOMES -> homes
            COURSES -> courses
            HASH -> Objects.hash(actions, locations, friends, transports, homes, courses)
            else -> throw IllegalStateException()
        }
    }

    val ACTIONS = 1
    val LOCATIONS = 2
    val FRIENDS = 3
    val TRANSPORTS = 4
    val HOMES = 5
    val COURSES = 6
    val HASH = 7
}
