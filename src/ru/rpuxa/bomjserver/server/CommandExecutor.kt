package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.Review
import ru.rpuxa.bomjserver.ServerCommand
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
            HASH -> Objects.hash(
                    Arrays.hashCode(actions),
                    Arrays.hashCode(locations),
                    Arrays.hashCode(friends),
                    Arrays.hashCode(transports),
                    Arrays.hashCode(homes),
                    Arrays.hashCode(courses)
            )
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
}
