package AirCarRental.Entities


enum class Building { TerminalOne, TerminalTwo, AirCarRental }

/**
 * @return distance to next building in meters
 */
fun Building.distanceToNext() = when(this){
    Building.TerminalOne  -> 500
    Building.TerminalTwo  -> 2500
    Building.AirCarRental -> 6400
}
