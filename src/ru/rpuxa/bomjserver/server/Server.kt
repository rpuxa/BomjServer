package ru.rpuxa.bomjserver.server

interface Server {

    fun start(address: String)

    fun stop()
}
