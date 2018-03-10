package AirCarRental.Entities

import java.util.*

/**
 * [averageSpeed] is in meters per second
 */
data class MiniBus(
        val capacity: Int = 12,
        val averageSpeed: Double = 35 / 3.6,
        val line: List<Costumer> = mutableListOf(),
        var location : Building
)