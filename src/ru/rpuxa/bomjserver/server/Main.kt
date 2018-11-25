package ru.rpuxa.bomjserver.server

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread


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
                    CommandExecutor.cached.saveToFile()
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

