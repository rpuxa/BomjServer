package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.server.actions.Actions
import ru.rpuxa.bomjserver.server.actions.CachedActions
import ru.rpuxa.bomjserver.server.actions.LocationParams
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

val params = arrayOf(
        LocationParams(0, 200, 50.0, 40.0, 40.0),
        LocationParams(1, 350, 120.0, 100.0, 120.0),
        LocationParams(2, 450, 400.0, 200.0, 180.0),
        LocationParams(3, 750, 500.0, 350.0, 320.0),
        LocationParams(4, 1050, 1500.0, 600.0, 700.0),
        LocationParams(5, 1500, 3000.0, 1300.0, 1300.0),
        LocationParams(6, 5000, 6000.0, 2000.0, 2000.0),
        LocationParams(7, 5000, 12000.0, 4000.0, 4000.0)
)
val cached = CachedActions(params)
private val server = SocketServer()
private val reader = BufferedReader(InputStreamReader(System.`in`))

fun main(unused: Array<String>) {
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

