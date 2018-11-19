package ru.rpuxa.bomjserver.server.actions

class Course(
        val id: Int,
        val name: String,
        var cost: Money,
        val length: Int
)
