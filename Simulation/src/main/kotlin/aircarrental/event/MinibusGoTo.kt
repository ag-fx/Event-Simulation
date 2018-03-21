package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Minibus
import aircarrental.entities.distanceToNext

class MinibusGoTo(
        private val minibus: Minibus,
        private val destination: Buildings,
        private val source: Buildings,
        time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        minibus.apply {
            destination = this@MinibusGoTo.destination
            source = this@MinibusGoTo.source
            leftAt = currentTime
        }

        val timeToGetThere = (source.distanceToNext() / minibus.averageSpeed)
        when (destination) {
            Buildings.TerminalOne -> plan(MinibusArrivalTerminalOne(minibus, currentTime + timeToGetThere))
            Buildings.TerminalTwo -> plan(MinibusArrivalTerminalTwo(minibus, currentTime + timeToGetThere))
            Buildings.AirCarRental -> plan(MinibusArrivalAirCarRental(minibus, currentTime + timeToGetThere))
        }

    }

    override fun toString() = "Minibus ${minibus.id} go from  $source to $destination at $occurrenceTime"

}