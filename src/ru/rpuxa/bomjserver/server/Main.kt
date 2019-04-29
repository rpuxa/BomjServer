package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.server.actions.Actions
import ru.rpuxa.bomjserver.server.actions.CachedActions
import ru.rpuxa.bomjserver.server.actions.LocationParams
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

val params = arrayOf(
        LocationParams(0, 200, 1.3, 1.0),
        LocationParams(1, 350, 2.0, 1.8),
        LocationParams(2, 450, 4.6, 2.4),
        LocationParams(3, 450, 7.0, 5.1),
        LocationParams(4, 650, 11.0, 10.0),
        LocationParams(5, 800, 20.0, 17.7),
        LocationParams(6, 1000, 34.4, 31.1),
        LocationParams(7, 1000, 50.0, 46.0)
)
val cached = CachedActions(params)
private val server = SocketServer()
private val reader = BufferedReader(InputStreamReader(System.`in`))



fun main(args: Array<String>) {
    try {
        thread(isDaemon = true) {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 10L)
                } catch (ignored: InterruptedException) {
                }

                save()
                println("Автоматическое сохранение...")
            }
        }
        println("Введите команду")
        while (true) {
            print("> ")
            val args0 = reader.readLine().split(' ')
            val args = if (args0.size > 1) args0.subList(1, args0.size) else emptyList()

            when (args0[0]) {
                "start" -> {
                    DataBase.loadFromFile()
                    println("Введите айпи")
                    val ip = reader.readLine()
                    Thread { server.start(ip) }.start()
                    println("Сервер запущен")
                }

                "stop" -> {
                    server.stop()
                    save()
                    println("Остановка...")
                    System.exit(0)
                    return
                }

                "save" -> {
                    save()
                    println("Сохранение прошло успешно")
                }

                "actionsList" -> {
                    Actions
                    println("Файл создан!!")
                }

                "news" -> {
                    var s = ""
                    args.forEach { s += "$it " }
                    DataBase.addNews(s)
                    println("Новость добавлена!")
                }

                else -> println("Неизвестная команда")
            }
        }
    } finally {
        DataBase.saveToFile()
    }
}

fun save() {
    DataBase.saveToFile()
}

