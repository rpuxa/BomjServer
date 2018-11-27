package ru.rpuxa.bomjserver.server.actions

class LocationParams(val level: Int, val days: Int, jobCost: Double, energyCost: Double, healthCost: Double) {
    val jobCost: Double = jobCost * 2 // цены за 30 единиц
    val energyCost: Double = energyCost * 2
    val healthCost: Double = healthCost * 2

}