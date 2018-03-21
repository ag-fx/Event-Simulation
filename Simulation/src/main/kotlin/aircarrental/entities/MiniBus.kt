package aircarrental.entities

import aircarrental.AirCarRentalState
import core.StatisticQueue

/**
 * [averageSpeed] is in meters per second
 */
data class Minibus(
        val id : Int,
        val capacity: Int = 12,
        val averageSpeed: Double = 35 / 3.6, //  Priemerná rýchlosť pohybu minibusu je 35 km/h.  = 35* 60*60/1000
        val seats: StatisticQueue<Customer,AirCarRentalState>,
        var destination: Buildings,
        var source: Buildings,
        var leftAt:Double,
        var distanceToDestination: Double
) {
    fun isNotFull() = seats.size() <= capacity

    /**
     * @return distance in meters from source
     */
    fun distanceFromSource(currentSimTime:Double) :Double = (currentSimTime-leftAt) * averageSpeed

    fun distanceFromDestinatioN(currentSimTime: Double) = source.distanceToNext() - distanceFromSource(currentSimTime)
}