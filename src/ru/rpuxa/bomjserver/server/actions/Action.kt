package ru.rpuxa.bomjserver.server.actions

class Action(
        val id: Int,
        val name: String,
        val level: Int,
        val menu: Int,
        val currency: Int,
        val free: Boolean,
        val illegal: Boolean
)