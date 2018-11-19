package ru.rpuxa.bomjserver.server.actions

data class ChainElement(
        val name: String,
        val location: Int,
        val friend: Int,
        val transport: Int,
        val home: Int,
        val course: Int,
        val cost: Money
)
